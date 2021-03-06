package model;


import java.sql.Date;

public class CreditAccount extends AbstractAccount {
    public CreditAccount(Integer customerId, String number, AccountType accountType, AccountStatus accountStatus, Date openDate, Long balance, Long debitLine, Integer interest) {
        super(customerId, number, accountType, accountStatus, openDate, balance, debitLine, interest);
    }

    public CreditAccount(Integer accountId, Integer customerId, String number, AccountType accountType, AccountStatus accountStatus, Date openDate, Long balance, Long debitLine, Integer interest) {
        super(accountId, customerId, number, accountType, accountStatus, openDate, balance, debitLine, interest);
    }
}
