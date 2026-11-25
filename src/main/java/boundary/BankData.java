package boundary;

import database.DatabaseManager;
import model.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.Base64;

public final class BankData {
    private BankData() {}

    public static final class User {
        private final Customer customer;
        private final String password;
        private final String salt;
        private final String passwordHash;

        User(Customer customer, String password, String salt, String passwordHash) {
            this.customer = customer;
            this.password = password;
            this.salt = salt;
            this.passwordHash = passwordHash;
        }

        public Customer getCustomer() { return customer; }
        // Kept for compatibility with any existing code that calls this getter.
        public String getPassword() { return password; }
    }

    public static final class HistoryItem {
        private final LocalDate date;
        private final String description;
        private final double amount;
        private final String type;

        public HistoryItem(LocalDate date, String description, double amount, String type) {
            this.date = date;
            this.description = description;
            this.amount = amount;
            this.type = type;
        }

        public LocalDate getDate() { return date; }
        public String getDescription() { return description; }
        public double getAmount() { return amount; }
        public String getType() { return type; }
    }

    private static final Map<String, User> usersByAccount = new LinkedHashMap<>();
    private static final Map<String, Account> accounts = new LinkedHashMap<>();
    private static final Map<String, List<HistoryItem>> historyByAccount = new HashMap<>();
    private static User currentUser;
    private static int nextAccountNumber = 100001;

    static {
        try {
            DatabaseManager.initialize();
            loadSavedAccounts();
            nextAccountNumber = DatabaseManager.getNextAccountNumber();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static User getCurrentUser() { return currentUser; }
    public static void setCurrentUser(User user) { currentUser = user; }

    public static String generateAccountNumber() {
        return String.valueOf(nextAccountNumber++);
    }

    public static void register(Customer customer, Account account, String password) {
        String accountNumber = account.getAccountNumber();
        if (usersByAccount.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account number already exists.");
        }

        try {
            String salt = createSalt();
            String hash = hashPassword(password, salt);
            String firstName = "";
            String surname = "";

            if (customer instanceof Person person) {
                firstName = person.getFirstName();
                surname = person.getLastName();
            } else if (customer instanceof Company company) {
                firstName = company.getCompanyName();
                surname = "Company";
            }

            String type = accountType(account);
            String employer = account instanceof ChequeAccount cheque
                    ? cheque.getEmployerDetails() : null;

            DatabaseManager.saveRegistration(
                    accountNumber, firstName, surname, customer.getCustomerID(),
                    customer.getEmail(), customer.getPhone(), customer.getAddress(),
                    salt, hash, type, account.getBalance(), account.getBranch(),
                    java.sql.Date.valueOf(account.getDateOpened()), employer
            );

            customer.setAccount(account);
            User user = new User(customer, password, salt, hash);
            usersByAccount.put(accountNumber, user);
            accounts.put(accountNumber, account);
            historyByAccount.put(accountNumber, new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException("Could not save registration to the database: "
                    + e.getMessage(), e);
        }
    }

    public static User login(String accountNumber, String password) {
        if (accountNumber == null || password == null) return null;
        User user = usersByAccount.get(accountNumber.trim());
        if (user == null) return null;

        try {
            if (verifyPassword(password, user.salt, user.passwordHash)) {
                currentUser = user;
                return user;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not verify login: " + e.getMessage(), e);
        }
        return null;
    }

    private static void loadSavedAccounts() throws SQLException {
        for (Map<String, Object> row : DatabaseManager.loadRegisteredAccounts()) {
            String number = (String) row.get("account_number");
            String firstName = (String) row.get("first_name");
            String surname = (String) row.get("surname");
            String customerRef = (String) row.get("customer_ref");
            String email = (String) row.get("email");
            String phone = (String) row.get("phone");
            String address = (String) row.get("address");
            String salt = (String) row.get("password_salt");
            String hash = (String) row.get("password_hash");

            // Old database rows without credentials cannot be used to log in.
            if (salt == null || hash == null) continue;

            Person customer = new Person(
                    customerRef == null ? "" : customerRef,
                    email, phone, address == null ? "" : address,
                    firstName == null ? "" : firstName,
                    surname == null ? "" : surname
            );

            double balance = ((java.math.BigDecimal) row.get("balance")).doubleValue();
            String branch = (String) row.get("branch");
            java.sql.Date sqlDate = (java.sql.Date) row.get("date_opened");
            LocalDate opened = sqlDate == null ? LocalDate.now() : sqlDate.toLocalDate();
            String type = (String) row.get("account_type");
            Account account;

            if ("Cheque".equalsIgnoreCase(type)) {
                account = new ChequeAccount(number, balance, opened, branch,
                        (String) row.get("employer_details"));
            } else if ("Investment".equalsIgnoreCase(type)) {
                account = new InvestmentAccount(number, balance, opened, branch);
            } else {
                account = new SavingsAccount(number, balance, opened, branch);
            }

            customer.setAccount(account);
            usersByAccount.put(number, new User(customer, "", salt, hash));
            accounts.put(number, account);

            List<HistoryItem> history = new ArrayList<>();
            for (Map<String, Object> tx : DatabaseManager.loadTransactions(number)) {
                java.sql.Timestamp timestamp = (java.sql.Timestamp) tx.get("transaction_date");
                LocalDate date = timestamp == null ? LocalDate.now()
                        : timestamp.toLocalDateTime().toLocalDate();
                history.add(new HistoryItem(date, (String) tx.get("description"),
                        ((java.math.BigDecimal) tx.get("amount")).doubleValue(),
                        (String) tx.get("transaction_type")));
            }
            historyByAccount.put(number, history);
        }
    }

    private static String accountType(Account account) {
        if (account instanceof ChequeAccount) return "Cheque";
        if (account instanceof InvestmentAccount) return "Investment";
        return "Savings";
    }

    private static String createSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private static String hashPassword(String password, String salt) throws Exception {
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 120000, 256);
        try {
            byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } finally {
            spec.clearPassword();
        }
    }

    private static boolean verifyPassword(String password, String salt, String expected)
            throws Exception {
        return hashPassword(password, salt).equals(expected);
    }

    public static Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public static Collection<Account> getCurrentAccounts() {
        if (currentUser == null || currentUser.getCustomer().getAccount() == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(currentUser.getCustomer().getAccount());
    }

    public static void addHistory(Account account, String description, double amount, String type) {
        if (account == null) return;
        HistoryItem item = new HistoryItem(LocalDate.now(), description, amount, type);
        historyByAccount.computeIfAbsent(account.getAccountNumber(), k -> new ArrayList<>()).add(item);
        try {
            DatabaseManager.addTransaction(account.getAccountNumber(), type, amount, description);
        } catch (SQLException e) {
            throw new RuntimeException("Could not save transaction: " + e.getMessage(), e);
        }
    }

    public static List<HistoryItem> getHistory(Account account) {
        if (account == null) return Collections.emptyList();
        return new ArrayList<>(historyByAccount.getOrDefault(
                account.getAccountNumber(), Collections.emptyList()));
    }

    public static void saveBalance(Account account) {
        if (account == null) return;
        try {
            DatabaseManager.updateBalance(account.getAccountNumber(), account.getBalance());
        } catch (SQLException e) {
            throw new RuntimeException("Could not save account balance: " + e.getMessage(), e);
        }
    }

    public static void logout() {
        currentUser = null;
    }
}
