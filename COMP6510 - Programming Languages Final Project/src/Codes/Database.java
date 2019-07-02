package Codes;

import java.sql.*;
import java.time.LocalDate;

public class Database {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/db_monage_app";
    static final String USER = "root";
    static final String PASS = "";
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;

    public static Connection connect(){
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Checks if username has exists
    public static boolean checkUser(String username){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM tb_users WHERE username = '%s'";
            sql = String.format(sql, username);

            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                stmt.close();
                conn.close();
                return true;
            } else {
                stmt.close();
                conn.close();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Checks if user's password is correct
    public static boolean checkPassword(String username, String password){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM tb_users WHERE username = '%s' AND password = '%s'";
            sql = String.format(sql, username, password);

            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                stmt.close();
                conn.close();
                return true;
            } else {
                stmt.close();
                conn.close();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add new user to the table as well as making a new table for the user
    public static void addUser(String username, String password, int income){
        try {
            conn = connect();
            stmt = conn.createStatement();

            // Create new table for user
            String sql = String.format("CREATE TABLE %s (\n" +
                    "    id int AUTO_INCREMENT,\n" +
                    "    type varchar(255),\n" +
                    "    item varchar(255),\n" +
                    "    amount int(3),\n" +
                    "    price int(20),\n" +
                    "    date date,\n" +
                    "    PRIMARY KEY(id)\n" +
                    ");" , username) ;
            stmt.execute(sql);

            // Add new user to the tb_users
            sql = String.format("INSERT INTO tb_users (username, password, income) VALUE('%s', '%s', '%d')", username, password, income);
            stmt.execute(sql);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update username data
    public static void updateUser(int id, String oldusername, String newusername, String password, int income){
        try {
            conn = connect();
            stmt = conn.createStatement();

            // Updating the userdata in tb_users
            String sql = "UPDATE tb_users SET username='%s', password='%s', income='%d' WHERE id = %d";
            sql = String.format(sql, newusername, password, income, id);
            stmt.execute(sql);

            // Changing the old table name to new one
            sql = "RENAME TABLE %s TO %s";
            sql = String.format(sql, oldusername, newusername);
            stmt.execute(sql);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Table has already exists");
        }
    }

    // Gets ID of a particular user
    public static int getID(String username){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM tb_users WHERE username = '%s'";
            sql = String.format(sql, username);

            rs = stmt.executeQuery(sql);
            rs.next();
            int id = rs.getInt("id");

            stmt.close();
            conn.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getIncome(String username){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM tb_users WHERE username = '%s'";
            sql = String.format(sql, username);

            rs = stmt.executeQuery(sql);
            rs.next();
            int income = rs.getInt("income");

            stmt.close();
            conn.close();
            return income;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void addItem(String username, String type, String item, int amount, int price, LocalDate date){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "INSERT INTO %s (type, item, amount, price, date) VALUE('%s', '%s', '%d', '%d', '%s')";
            sql = String.format(sql, username, type, item, amount, price, Date.valueOf(date));

            stmt.execute(sql);
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateItem(String username, int id, String type, String item, int amount, int price, LocalDate date){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "UPDATE %s SET type='%s', item='%s', amount='%d', price='%d', date='%s' WHERE id = %d";
            sql = String.format(sql, username, type, item, amount, price, Date.valueOf(date), id);

            stmt.execute(sql);
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteItem(String username, int id){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "DELETE FROM %s WHERE id = %d";
            sql = String.format(sql, username, id);

            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}