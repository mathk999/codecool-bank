package dao;


import model.Account;
import model.Customer;
import util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerDaoSQLite implements CustomerDao {
    DatabaseConnection dbConnect;

    public CustomerDaoSQLite(DatabaseConnection dbConnect) {
        this.dbConnect = dbConnect;
    }

    public Customer find(Integer customerId) throws SQLException {
        String query = "SELECT * FROM customers WHERE customerID = ?;";
        PreparedStatement preparedStatement = this.dbConnect.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, customerId);
        return resultSetToCustomer(preparedStatement.executeQuery());
    }

    public Customer find(String login, String password) throws SQLException {
        String query = "SELECT * FROM customers WHERE login = ? AND password = ?";
        PreparedStatement preparedStatement = this.dbConnect.getConnection().prepareStatement(query);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        return resultSetToCustomer(preparedStatement.executeQuery());
    }

    public Integer addOrUpadte(Customer customer) throws SQLException {
        String insertQuery = "INSERT INTO customers (firstname, lastname, login, password, createdate, isactive, lastlogin) VALUES (?, ?, ?, ?, ?, ?, ?);";
        String updateQuery = "UPDATE customers SET password = ?, isactive = ?, lastlogin = ?;";

        Integer customerId = customer.getCustomerId();
        PreparedStatement preparedStatement;

        if (customerId == null) {
            preparedStatement = this.dbConnect.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getLogin());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.setDate(5, (java.sql.Date) customer.getCreateDate());
            preparedStatement.setBoolean(6, customer.isActive());
            preparedStatement.setDate(7, (java.sql.Date) customer.getLastLogin());
        } else {
            preparedStatement = this.dbConnect.getConnection().prepareStatement(updateQuery);
            preparedStatement.setString(1, customer.getPassword());
            preparedStatement.setBoolean(2, customer.isActive());
            preparedStatement.setDate(3, (java.sql.Date) customer.getLastLogin());
        }
        return preparedStatement.executeUpdate();
    }

    private Customer resultSetToCustomer(ResultSet resultSet) throws SQLException {
        Customer resultCustomer = null;
        List<Account> accounts;

        while (resultSet.next()) {
            Integer customerId = resultSet.getInt("customerID");
            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            Date createDate = resultSet.getDate("createdate");
            Boolean isActive = resultSet.getBoolean("isactive");
            Date lasLogin = resultSet.getDate("lastlogin");
            accounts = new AccountDaoSQLite(this.dbConnect).getByCustomerId(customerId);
            resultCustomer = new Customer(customerId, firstName, lastName, login, password, createDate, isActive, lasLogin, accounts);
        }
        return resultCustomer;
    }
}
