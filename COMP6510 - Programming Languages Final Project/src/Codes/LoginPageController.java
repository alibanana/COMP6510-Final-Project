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

public class LoginPageController implements Initializable {

    private User user = new User();

    // Input TextFields
    @FXML private TextField UsernameInput;
    @FXML private PasswordField PasswordInput;

    // Warning Labels
    @FXML private Label UsernameWarningLabel;
    @FXML private Label PasswordWarningLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb){ }

    @FXML
    public void SignUpButtonClicked(ActionEvent event) throws IOException {
        // Loads the sign up page
        Parent SignUpPageParent = FXMLLoader.load(getClass().getResource("SignUpPage.fxml"));

        // This line gets the Stage information
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setResizable(false);
        stage.setTitle("MonageSignUpPage");
        stage.setScene(new Scene(SignUpPageParent));
        stage.show();
    }

    @FXML
    public void LoginButtonClicked(ActionEvent event) throws IOException {
        boolean flag = true;

        // Reset the Labels
        UsernameWarningLabel.setText("");
        PasswordWarningLabel.setText("");

        // Stores user's input (username & password)
        String username = UsernameInput.getText();
        String password = PasswordInput.getText();

        // Checks if TextField is empty
        if (username.equals("") && password.equals("")){ // If both TextFields are empty
            UsernameWarningLabel.setTextFill(Color.RED);
            UsernameWarningLabel.setText("Username is empty..");
            PasswordWarningLabel.setTextFill(Color.RED);
            PasswordWarningLabel.setText("Password is empty..");
            return;
        } else if (username.equals("")){ // If only username TextField is empty
            UsernameWarningLabel.setTextFill(Color.RED);
            UsernameWarningLabel.setText("Username is empty..");
            return;
        } else if (password.equals("")){ // If only password TextField is empty
            PasswordWarningLabel.setTextFill(Color.RED);
            PasswordWarningLabel.setText("Password is empty..");
            return;
        }

        // Checks if username exists
        if (!Database.checkUser(username)) { // if username does not exist
            UsernameWarningLabel.setTextFill(Color.RED);
            UsernameWarningLabel.setText("Username does not exist..");
            flag = false;
        } else { // if username is correct
            UsernameWarningLabel.setTextFill(Color.GREEN);
            UsernameWarningLabel.setText("Username exist..");
        }

        // Checks if password is correct
        if (!Database.checkPassword(username, password)) { // if password is wrong
            PasswordWarningLabel.setTextFill(Color.RED);
            PasswordWarningLabel.setText("Wrong Password..");
            flag = false;
        } else { // if password is correct
            PasswordWarningLabel.setTextFill(Color.GREEN);
            PasswordWarningLabel.setText("Correct Password..");
        }

        if (flag){
            // Gets user id and income (only if username & password is correct)
            int id = Database.getID(username);
            int income = Database.getIncome(username);

            // Sets up the user object to be passed on
            user.setUsername(username);
            user.setPassword(password);
            user.setID(id);
            user.setIncome(income);

            // Prints out user's data (username & password)
            System.out.println("ID      : " + id);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("Income  : " + income);
            System.out.println();

            // Loads the main page
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainPage.fxml"));
            Parent MainPageParent = loader.load();

            // Passing object user to the MainPageController class
            MainPageController controller = loader.getController();
            controller.initData(user);

            // Gets stage's info and setting it up
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setResizable(false);
            stage.setTitle("MonageMainPage");
            stage.setScene(new Scene(MainPageParent));
            stage.show();
        }
    }

}
