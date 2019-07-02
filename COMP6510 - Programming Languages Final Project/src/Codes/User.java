package Codes;

public class User {

    // Class Members
    private int id;
    private String username;
    private String password;
    private int income;

    public User(){
        this.id = -1;
        this.username = "";
        this.password = "";
        this.income = -1;
    }

    public User(int id, String username, String password, int income) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.income = income;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

}
