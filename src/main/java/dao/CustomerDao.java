package dao;


import model.Customer;

import java.sql.SQLException;

public interface CustomerDao {
    Customer find(Integer customerId) throws SQLException;
}
