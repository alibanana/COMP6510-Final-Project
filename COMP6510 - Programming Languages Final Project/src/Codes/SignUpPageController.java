package Codes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpPageController implements Initializable {

    // Input TextFields
    @FXML private TextField UsernameInput;
    @FXML private PasswordField PasswordInput;
    @FXML private TextField IncomeInput;

    // Warning Labels
    @FXML private Label UsernameWarningLabel;
    @FXML private Label PasswordWarningLabel;
    @FXML private Label IncomeWarningLabel;


    /*
    This function is called when the login page is initialized, it sets all the color of the labels to white and set the
    default text for each of the labels.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        UsernameWarningLabel.setTextFill(Color.WHITE);
        UsernameWarningLabel.setText("Only 6 - 20 characters, no non-alphabetical\ncharacters beside !@#$%^&*()");
        PasswordWarningLabel.setTextFill(Color.WHITE);
        PasswordWarningLabel.setText("Only 8 - 20 characters, no non-alphabetical\ncharacters beside !@#$%^&*()");
        IncomeWarningLabel.setTextFill(Color.WHITE);
        IncomeWarningLabel.setText("Must be at least Rp 500000");
    }

    @FXML
    public void SignUpButtonClicked(ActionEvent event) throws IOException {
        boolean flag = true;

        // Stores user's input (username & password)
        String username = UsernameInput.getText();
        String password = PasswordInput.getText();

        // Validations for Username
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
            PasswordWarningLabel.setText("Password correct format..");
        }

        int income = 0; // Initialize int income
        // Check if the IncomeTextField is empty
        try {
            income = Integer.parseInt(IncomeInput.getText()); // Getting user input (Income)
        } catch (NumberFormatException e) {
            IncomeWarningLabel.setTextFill(Color.RED);
            IncomeWarningLabel.setText("Income cannot be empty...");
            return;
        }

        // Validations for Income
        IncomeWarningLabel.setTextFill(Color.RED); // Set IncomeWarningLabel color to red
        if (CheckIncome(income) == 1){ // If income too low
            IncomeWarningLabel.setText("Income must be at least Rp 500000..");
            flag = false;
        } else {
            IncomeWarningLabel.setTextFill(Color.GREEN); // Set IncomeWarningLabel to green
            IncomeWarningLabel.setText("Income is acceptable..");
        }

        while(flag){
            // Check if username exists in Database
            if (Database.checkUser(username)) {
                UsernameWarningLabel.setTextFill(Color.RED); // Set IncomeWarningLabel color to red again
                UsernameWarningLabel.setText("Username has been taken..");
                break;
            } else {
                // Adds user to the Database
                Database.addUser(username, password, income);

                // Prints user's username & password
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
                System.out.println("Income:   " + income);
                System.out.println();

                // Loads the login page
                Parent SignUpPageParent = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));

                // This line gets the Stage information
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setResizable(false);
                stage.setTitle("MonageLoginPage");
                stage.setScene(new Scene(SignUpPageParent));
                stage.show();
                break;
            }
        }
    }

    @FXML
    public void CancelButtonClicked(ActionEvent event) throws IOException {
        // Loads the login page
        Parent SignUpPageParent = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));

        // This line gets the Stage information
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setResizable(false);
        stage.setTitle("MonageLoginPage");
        stage.setScene(new Scene(SignUpPageParent));
        stage.show();
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