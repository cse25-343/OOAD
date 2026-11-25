package boundary;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoryView {
    @FXML private TableView<BankData.HistoryItem> historyTable;
    @FXML private TableColumn<BankData.HistoryItem, String> dateColumn;
    @FXML private TableColumn<BankData.HistoryItem, String> descColumn;
    @FXML private TableColumn<BankData.HistoryItem, Number> amountColumn;
    @FXML private TableColumn<BankData.HistoryItem, String> typeColumn;

    @FXML
    private void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        if (BankData.getCurrentUser() != null) {
            historyTable.getItems().setAll(
                    BankData.getHistory(BankData.getCurrentUser().getCustomer().getAccount()));
        }
    }

    @FXML
    private void onBackClicked() {
        Main.show("dashboard.fxml", "Seabank - Dashboard", 900, 700);
    }
}
