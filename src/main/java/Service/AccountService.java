package Service;

import DAO.AccountDAO;

import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public static boolean isValidAccount(Account account){
        //Check if username is blank or empty
        if(account.getUsername().isBlank()){
            return false;
        }

        //Check if password is at least 4 characters long
        if(account.getPassword().length() < 4){
            return false;
        }

        return true;
    }

    public Account registerUser(Account account){
        //Check if username is not blank and password is at least 4 characters long
        if(!isValidAccount(account)){
            return null;
        }

        //Check if username already exists
        if(accountDAO.getAccountByUsername(account.getUsername()) != null){
            return null;
        }

        return accountDAO.insertUser(account);
    }

    public Account loginUser(Account account){
        //Check if account with given username exists
        Account retrievedAccount = accountDAO.getAccountByUsername(account.getUsername());
        if(retrievedAccount == null){
            return null;
        }

        //Check if given password matches found password
        if(retrievedAccount.getPassword().equals(account.getPassword())){
            return retrievedAccount;
        }
        return null;
    }
}
