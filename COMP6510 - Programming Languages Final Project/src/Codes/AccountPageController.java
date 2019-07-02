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
import java.util.ResourceBundle;

public class AccountPageController implements Initializable {

    User user;

    // Storing MainPage's controller
    MainPageController parentController;

    // TextField Members
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField incomeTextField;

    // Warning Labels
    @FXML private Label UsernameWarningLabel;
    @FXML private Label PasswordWarningLabel;
    @FXML private Label IncomeWarningLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

    public void initData(User temp){
        user = temp;
        usernameTextField.setText(user.getUsername());
        passwordTextField.setText(user.getPassword());
        incomeTextField.setText(Integer.toString(user.getIncome()));
    }

    public void setParentController(MainPageController controller){
        this.parentController = controller;
    }

    @FXML
    public void CancelButtonClicked(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void ConfirmButtonClicked(ActionEvent event){
        boolean flag = true;
        resetLabels(); // Resetting the Warning Labels

        // Validations for Username
        String username = usernameTextField.getText();
        UsernameWarningLabel.setTextFill(Color.RED); // Set UsernameWarningLabel color to red
        if (CheckUsername(username) == 1){ // If username is too short
            UsernameWarningLabel.setText("Username must contain at least 6 characters..");
            flag = false;
        } else if (CheckUsername(username) == 2){ // If username is too long
            UsernameWarningLabel.setText("Username must contain at most 20 characters..");
            flag = false;
        } else if (CheckUsername(username) == 3){ // If username contain unwanted characters
            UsernameWarningLabel.setText("Username can only contain alphabetical characters\nand !@#$%^&*() symbols..");
            flag = false;
        } else {
            UsernameWarningLabel.setTextFill(Color.GREEN); // Set UsernameWarningLabel color to green
            UsernameWarningLabel.setText("Username correct format..");
        }

        // Validations for Password
        String password = passwordTextField.getText();
        PasswordWarningLabel.setTextFill(Color.RED); // Set PasswordWarningLabel color to red
        if (CheckPassword(password) == 1){ // If password too short
            PasswordWarningLabel.setText("Password must contain at least 8 character..");
            flag = false;
        } else if (CheckPassword(password) == 2){ // If password too long
            PasswordWarningLabel.setText("Password must contain at most 20 characters..");
            flag = false;
        } else if (CheckPassword(password) == 3) { // If password contain unwanted characters
            PasswordWarningLabel.setText("Password can only contain alphabetical characters\nand !@#$%^&*() symbols..");
            flag = false;
        } else {
            PasswordWarningLabel.setTextFill(Color.GREEN); // Set PasswordWarningLabel color to green
            PasswordWarningLabel.setText("Username correct format..");
        }

        // Check if Income is empty or non-int
        if (incomeTextField.getText().equals("")) { // if income is empty
            IncomeWarningLabel.setTextFill(Color.RED);
            IncomeWarningLabel.setText("Income is empty..");
            return;
        } else if (!(incomeTextField.getText().matches("[0-9]+"))){ // if income have non-int value
            IncomeWarningLabel.setTextFill(Color.RED);
            IncomeWarningLabel.setText("Income can only be a number..");
            return;
        }

        // Validations for Income
        int income = Integer.parseInt(incomeTextField.getText());
        IncomeWarningLabel.setTextFill(Color.RED); // Set IncomeWarningLabel color to red
        if (CheckIncome(income) == 1){ // If income too low
            IncomeWarningLabel.setText("Income must be at least Rp 500000..");
            flag = false;
        } else {
            IncomeWarningLabel.setTextFill(Color.GREEN); // Set IncomeWarningLabel to green
            IncomeWarningLabel.setText("Income is acceptable..");
        }

        if (flag){
            Database.updateUser(user.getID(), user.getUsername(), username, password, income);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            // Creating new user object (with new data)
            User newuserdata = new User(user.getID(), username, password, income);

            // Refresh table in Main Page
            parentController.initData(newuserdata);
        }
    }

    // Method to reset all the Warning Labels
    private void resetLabels(){
        UsernameWarningLabel.setText("");
        PasswordWarningLabel.setText("");
        IncomeWarningLabel.setText("");
    }

    // Check Methods
    /*
    Username Conditions:
     - 6 - 20 Characters
     - Only Alphabets, Numbers and Non-Special Characters (!@#$%^&*())
     */
    private int CheckUsername(String username){
        if (username.length() < 6){
            return 1; // If the username is too short
        } else if (username.length() > 20){
            return 2; // If the username is too long
        } else if (!username.matches("[a-zA-z0-9!@#$%^&*()]+")) {
            return 3; // If the username contains unwanted characters
        } else {
            return 0; // if the username is actually correct in format
        }
    }

    /*
    Password Conditions:
     - 8 - 20 Characters
     - Only Alphabets, Numbers and Non-Special Characters (!@#$%^&*())
     */
    private int CheckPassword(String password){
        if (password.length() < 8){
            return 1; // If the username is too short
        } else if (password.length() > 20){
            return 2; // If the username is too long
        } else if (!password.matches("[a-zA-z0-9!@#$%^&*()]+")) {
            return 3; // If the username contains unwanted characters
        } else {
            return 0; // if the username is actually correct in format
        }
    }

    /*
    Income Conditions:
     - At least Rp 500000
     */
    private int CheckIncome(int income){
        if (income < 500000){
            return 1; // If income is too low
        } else {
            return 0; // If income format is correct
        }
    }

}
