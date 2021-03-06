package controller;

import dao.AccountDao;
import dao.AccountDaoSQLite;
import dao.AccountStatusDaoSQLite;
import dao.AccountTypeDaoSQLite;
import model.Account;
import model.AccountType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DatabaseConnection;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class AccountControllerTest {

    private DatabaseConnection dbConnect = new DatabaseConnection();
    private AccountDao accountDao;
    private AccountController accountController;

    @BeforeEach
    void openConnection() throws SQLException {
        dbConnect.openConnection("jdbc:sqlite:src/test/resource/test_database.db");
        dbConnect.dropTables();
        dbConnect.initDatabase();
        dbConnect.insertData();
        this.accountDao = new AccountDaoSQLite(dbConnect);
        this.accountController = new AccountController(dbConnect);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        dbConnect.closeConnection();
    }

    @Test
    public void testAddNewCreditAccount() throws SQLException {
        Integer testCustomerId = 1;
        Integer accountIdInDb = 2;
        AccountType testAccountType = new AccountType(1, "Saving Account", null);
        this.accountController.addNewAccount(testCustomerId, testAccountType);
        Account testedAccountFromDb = this.accountDao.find(accountIdInDb);
        assertAll("-",
                () -> assertEquals(testAccountType.getName(), testedAccountFromDb.getAccountType().getName()),
                () -> assertEquals(testCustomerId, testedAccountFromDb.getCustomerId())
        );
    }

    @Test
    public void testAddNewSavingAccount() throws SQLException {
        Integer testCustomerId = 1;
        Integer accountIdInDb = 2;
        Integer accountInterest = 10;
        AccountType testAccountType = new AccountType(1, "Saving Account", null);
        this.accountController.addNewAccount(testCustomerId, testAccountType, accountInterest);
        Account testedAccountFromDb = this.accountDao.find(accountIdInDb);
        assertAll("-",
                () -> assertEquals(testAccountType.getName(), testedAccountFromDb.getAccountType().getName()),
                () -> assertEquals(testCustomerId, testedAccountFromDb.getCustomerId()),
                () -> assertEquals(accountInterest, testedAccountFromDb.getInterest())
        );
    }

    @Test
    public void testBlockAnAccount() throws SQLException {
        Integer accountIdInDb = 1;
        Account accountToBlock = accountDao.find(accountIdInDb);
        accountController.blockAnAccount(accountToBlock);
        Account blockedAccountFromDb = accountDao.find(accountIdInDb);
        assertEquals("Invalid", blockedAccountFromDb.getAccountStatus().getName());
    }

    @Test
    public void testUnBlockAnAccount() throws SQLException {
        Integer accountIdInDb = 1;
        Account accountToUnBlock = accountDao.find(accountIdInDb);
        accountController.unBlockAnAccount(accountToUnBlock);
        Account unBlockedAccountFromDb = accountDao.find(accountIdInDb);
        assertEquals("Valid", unBlockedAccountFromDb.getAccountStatus().getName());
    }

}