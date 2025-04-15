package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public static boolean isValidMessage(Message message){
        //Check if message is blank or empty
        if(message.getMessage_text().isBlank()){
            return false;
        }

        //Check if message is less than 255 characters long
        if(message.getMessage_text().length() >= 255){
            return false;
        }

        return true;
    }

    public Message createMessage(Message message){
        //Check if message is less than 255 characters long and is not blank
        if(!isValidMessage(message)){
            return null;
        }

        //Will return null if posted_by does not reference existing account
        return messageDAO.insertMessage(message);
    }

    public Message getMessageByMessageID(int messageID){
        return messageDAO.getMessageByMessageID(messageID);
    }

    public Message deleteMessage(int messageID){
        return messageDAO.deleteMessage(messageID);
    }

    public Message updateMessage(int messageID, String messageText){
        //Check if message text is not blank or longer than 255 characters ()
        if(!isValidMessage(new Message(0, messageText, 0))){
            return null;
        }

        return messageDAO.updateMessage(messageID, messageText);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public List<Message> getMessagesByUserID(int userID){
        return messageDAO.getMessagesByUserID(userID);
    }
}
