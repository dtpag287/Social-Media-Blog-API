package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {
    public Account insertUser(Account account){
        Connection connection = ConnectionUtil.getConnection();
        Account newAccount = null;
        try{
            String sql = "INSERT INTO account(username, password) VALUES(?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                newAccount = new Account(rs.getInt(1), account.getUsername(), account.getPassword());
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return newAccount;
    }

    public Account getAccountByUserID(int userID){
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try{
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                account = new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try{
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                account = new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return account;
    }
}
