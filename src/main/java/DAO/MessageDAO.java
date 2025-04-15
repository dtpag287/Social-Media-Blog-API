package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

public class MessageDAO {
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        Message newMessage = null;
        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                newMessage = new Message(rs.getInt(1), message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return newMessage;
    }

    public Message getMessageByMessageID(int messageID){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, messageID);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                message = new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    public Message deleteMessage(int messageID){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try{
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            String deleteSql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement selectPs = connection.prepareStatement(selectSql);
            PreparedStatement deletePs = connection.prepareStatement(deleteSql);

            selectPs.setInt(1, messageID);

            deletePs.setInt(1, messageID);

            ResultSet rs = selectPs.executeQuery();
            if(rs.next()){
                message = new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4));
                deletePs.executeUpdate();
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    public Message updateMessage(int messageID, String messageText){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try{
            String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement updatePs = connection.prepareStatement(updateSql);
            PreparedStatement selectPs = connection.prepareStatement(selectSql);

            updatePs.setString(1, messageText);
            updatePs.setInt(2, messageID);

            selectPs.setInt(1, messageID);

            updatePs.executeUpdate();
            ResultSet rs = selectPs.executeQuery();
            if(rs.next()){
                message = new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message";
            Statement statement = connection.createStatement();
            
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                messages.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<Message> getMessagesByUserID(int userID){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                messages.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
