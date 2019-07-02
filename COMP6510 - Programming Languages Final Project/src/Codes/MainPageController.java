package Codes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainPageController implements Initializable {

    // Stores user's data
    private User userloggedin;

    // Class Members
    int DailySpendings;
    int MonthlySpendings;

    // MenuItems Members
    @FXML private MenuBar MenuBar;

    // Labels and DatePicker
    @FXML private Label usernameLabel;
    @FXML private Label useridLabel;
    @FXML private Label incomeLabel;
    @FXML private Label dailySpLabel;
    @FXML private Label monthlySpLabel;
    @FXML private Label netCapitalLabel;
    @FXML private DatePicker DatePicker;

    // TableView Members
    @FXML private TableView<ModelTable> TableView;
    @FXML private TableColumn<ModelTable, String> col_type;
    @FXML private TableColumn<ModelTable, String> col_item;
    @FXML private TableColumn<ModelTable, Integer> col_amount;
    @FXML private TableColumn<ModelTable, Integer> col_price;
    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();

    // Warning Label
    @FXML private Label WarningLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

    /*
    This function receives user object passed from the login page and stores it.
    It also initializes some of the labels.
    */
    public void initData(User user){
        userloggedin = user;
        usernameLabel.setText(userloggedin.getUsername());
        useridLabel.setText(Integer.toString(userloggedin.getID()));
        incomeLabel.setText("Rp " + userloggedin.getIncome());
        refreshTable();
    }

    @FXML
    public void LogoButtonClicked(ActionEvent event) throws IOException {
        // Loads the login page
        Parent SignUpPageParent = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));

        // This line gets the Stage information
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setResizable(false);
        stage.setTitle("MonageLoginPage");
        stage.setScene(new Scene(SignUpPageParent));
        stage.show();
    }

    @FXML
    public void LogoutMenuClicked() throws IOException {
        // Loads the login page
        Parent SignUpPageParent = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));

        // This line gets the Stage information
        Stage stage = (Stage) MenuBar.getScene().getWindow();

        stage.setResizable(false);
        stage.setTitle("MonageLoginPage");
        stage.setScene(new Scene(SignUpPageParent));
        stage.show();
    }

    @FXML
    public void AccountMenuClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AccountPage.fxml"));
        Parent AccountPageParent = loader.load();

        Stage stage = new Stage(); // New stage (window)

        // Passing data to the AccountPageController class
        AccountPageController controller = loader.getController();
        controller.initData(userloggedin);
        controller.setParentController(this);

        // Setting the stage up
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("AccountSettingsPage");
        stage.setScene(new Scene(AccountPageParent));
        stage.showAndWait();
    }

    @FXML
    public void AddItem() throws IOException {
        if (getDatePickerDate() == null){ // If no date is being picked
            ShowWarningLabel("Please pick a date!");
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AddItemPage.fxml"));
            Parent AddItemPageParent = loader.load();

            Stage stage = new Stage(); // New stage (window)

            // Passing data to the AddItemPageController class
            AddItemPageController controller = loader.getController();
            controller.initData(userloggedin.getUsername(), getDatePickerDate());
            controller.setParentController(this);

            // Setting the stage up
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("AddItemPage");
            stage.setScene(new Scene(AddItemPageParent));
            stage.showAndWait();
        }
    }

    @FXML
    public void EditItem() throws IOException {
        ModelTable tempItem = TableView.getSelectionModel().getSelectedItem();
        if (!(tempItem == null)) { // If something is selected from the table
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EditItemPage.fxml"));
            Parent AddItemPageParent = loader.load();
            Stage stage = new Stage();

            // Passing data to the AddItemPageController class
            EditItemPageController controller = loader.getController();
            controller.initData(userloggedin.getUsername(), tempItem);
            controller.setParentController(this);

            // Setting the stage up
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("EditItemPage");
            stage.setScene(new Scene(AddItemPageParent));
            stage.showAndWait();
        } else { // If nothing is selected
            ShowWarningLabel("Please select an item!"); // Set a warning message
        }
    }

    @FXML
    public void DeleteItem() {
        ModelTable selectedRow = TableView.getSelectionModel().getSelectedItem(); // Get Selected Row
        if (!(selectedRow == null)){
            // Get Username, for the table
            String username = userloggedin.getUsername();

            // Get id from selected item
            int id = selectedRow.getId();

            // Deleting data from the database
            Database.deleteItem(username, id);

            refreshTable();
        } else {
            ShowWarningLabel("Please select an item!"); // Set a warning message
        }

    }

    // Utitility Methods
    public LocalDate getDatePickerDate(){
        return DatePicker.getValue();
    }

    private void ShowWarningLabel(String str){
        WarningLabel.setTextFill(Color.RED);
        WarningLabel.setText(str);
    }

    /*
    This function gets the date from the Date Picker and refres the table according to the the date choosen. This is
    done by filtering the data taken from the database, so only the data with the same date will be taken and shown
    into the table.
    Furthermore, the function is also responsible to count the Monthly & Daily spendings.
     */
    @FXML
    public void refreshTable() {
        ShowWarningLabel(""); // Resets the Warning Label
        LocalDate tempDate = getDatePickerDate();
        if (tempDate != null){ // Checks if DatePicker is set already

            // Clearing/Resetting all data
            oblist.clear(); // Clearing the list before adding data into it
            DailySpendings = 0; // Resetting daily spending
            MonthlySpendings = 0; // Resetting monthly spending

            try {
                Connection conn = Database.connect();
                String sql = String.format("SELECT * FROM %s", userloggedin.getUsername()); // Get data from user's table
                ResultSet rs = conn.createStatement().executeQuery(sql); // Store data in ResultSet rs

                while(rs.next()) {
                    // Compare months to get monthly spending
                    if (tempDate.getMonth().compareTo(rs.getDate("date").toLocalDate().getMonth()) == 0){
                        MonthlySpendings += rs.getInt("amount") * rs.getInt("price");
                    }
                    // Compare date to get items and daily spending
                    if (tempDate.compareTo(rs.getDate("date").toLocalDate()) == 0) {
                        oblist.add(new ModelTable(rs.getInt("id"), rs.getString("type"), rs.getString("item"),
                                rs.getInt("amount"), rs.getInt("price"), rs.getDate("date").toLocalDate()));
                        DailySpendings += rs.getInt("amount") * rs.getInt("price");
                    }
                }

                rs.close();
                conn.close();
            } catch (SQLException e) {
                Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, e);
            }

            col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
            col_item.setCellValueFactory(new PropertyValueFactory<>("item"));
            col_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

            // Showing all the items
            TableView.setItems(oblist); // Set table to show oblist data
            dailySpLabel.setText("Rp " + DailySpendings);
            monthlySpLabel.setText("Rp " + MonthlySpendings);
            netCapitalLabel.setText("Rp " + (userloggedin.getIncome() - MonthlySpendings));
        }
    }

}