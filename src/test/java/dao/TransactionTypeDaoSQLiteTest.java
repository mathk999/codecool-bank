package dao;


import model.TransactionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DatabaseConnection;
import util.FileReader;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeDaoSQLiteTest {
    private DatabaseConnection dbConnect = new DatabaseConnection();
    private TransactionTypeDaoSQLite transactionTypeDaoSQLite;

    @BeforeEach
    void openConnection() throws SQLException {
        this.dbConnect.openConnection("jdbc:sqlite:src/test/resource/test_database.db");
        this.dbConnect.initDatabase();
        this.dbConnect.insertData();
        this.transactionTypeDaoSQLite = new TransactionTypeDaoSQLite(this.dbConnect);
    }

    @AfterEach
    void dropTables() throws SQLException {
        FileReader reader = new FileReader();
        String[] createTables = reader.getStringFromFile("/sql/dropTables.sql").split(";");
        Statement statement = this.dbConnect.getConnection().createStatement();
        for (String query : createTables) {
            if (!query.trim().equals("")) {
                statement.executeUpdate(query);
            }
        }
        this.dbConnect.closeConnection();
    }

    @Test
    void testFindTransactionType() throws SQLException {
        Integer testId = 1;
        TransactionType expextedTransactionType = new TransactionType(1, "testName", "testDescription");
        TransactionType actualTransactiontype = transactionTypeDaoSQLite.find(testId);
        assertAll("FoundTransactionTypeData",
                () -> assertEquals(expextedTransactionType.getTypeId(), actualTransactiontype.getTypeId()),
                () -> assertEquals(expextedTransactionType.getName(), actualTransactiontype.getName()),
                () -> assertEquals(expextedTransactionType.getDescription(), actualTransactiontype.getDescription())
        );
    }
}