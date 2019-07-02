package Codes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddItemPageController implements Initializable {

    // Storing MainPage's controller
    private MainPageController parentController;

    private String username;
    private LocalDate date;

    // Textfield Members
    @FXML private TextField typeTextField;
    @FXML private TextField itemTextField;
    @FXML private TextField amountTextField;
    @FXML private TextField priceTextField;

    // Warning Label
    @FXML private Label TypeWarningLabel;
    @FXML private Label ItemWarningLabel;
    @FXML private Label AmountWarningLabel;
    @FXML private Label PriceWarningLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb){
    }

    public void initData(String username, LocalDate date){
        this.username = username;
        this.date = date;
    }

    public void setParentController(MainPageController parentController){
        this.parentController = parentController;
    }

    @FXML
    public void CancelButtonClicked(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void AddButtonClicked(ActionEvent event) {
        boolean isEmpty = true; // Boolean variable to check if any of the TextFields are empty
        boolean isInt = true; // Boolean variable to check if int data contains non-int value
        resetLabels(); // Resetting the Warning Labels

        // Assigning String type data, checking if TextFields are empty and check if int data have non-int type
        String type = typeTextField.getText();
        if (type.equals("")){ // if type is empty
            TypeWarningLabel.setTextFill(Color.RED);
            TypeWarningLabel.setText("Type is empty..");
            isEmpty = false;
        }
        String item = itemTextField.getText();
        if (item.equals("")){ // if item is empty
            ItemWarningLabel.setTextFill(Color.RED);
            ItemWarningLabel.setText("Item is empty..");
            isEmpty = false;
        }
        if (amountTextField.getText().equals("")){ // if amount is empty
            AmountWarningLabel.setTextFill(Color.RED);
            AmountWarningLabel.setText("Amount is empty..");
            isEmpty = false;
        } else if (!(amountTextField.getText().matches("[0-9]+"))){ // if amount have non-int value
            AmountWarningLabel.setTextFill(Color.RED);
            AmountWarningLabel.setText("Amount can only be an number..");
            isInt = false;
        }
        if (priceTextField.getText().equals("")){ // if price is empty
            PriceWarningLabel.setTextFill(Color.RED);
            PriceWarningLabel.setText("Price is empty..");
            isEmpty = false;
        } else if (!(priceTextField.getText().matches("[0-9]+"))){ // if price have non-int value
            PriceWarningLabel.setTextFill(Color.RED);
            PriceWarningLabel.setText("Price can only be an number..");
            isInt = false;
        }

        if (isEmpty && isInt){
            // Assigning int data to temporary variables
            int amount = Integer.parseInt(amountTextField.getText());
            int price = Integer.parseInt(priceTextField.getText());

            Database.addItem(username, type, item, amount, price, date);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            // Refresh table in Main Page
            parentController.refreshTable();
        }
    }

    // Utility Methods
    // Method to reset all the Warning Labels
    private void resetLabels(){
        TypeWarningLabel.setText("");
        ItemWarningLabel.setText("");
        AmountWarningLabel.setText("");
        PriceWarningLabel.setText("");
    }
}