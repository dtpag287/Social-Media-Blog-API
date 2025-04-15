package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        app.get("/messages", this::getMessagesHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByMessageIDHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        app.get("/accounts/{account_id}/messages", this::getMessagesByUserIDHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account newAccount = accountService.registerUser(account);

        if(newAccount == null){
            ctx.status(400);
        }
        else{
            ctx.json(mapper.writeValueAsString(newAccount));
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account retrievedAccount = accountService.loginUser(account);

        if(retrievedAccount == null){
            ctx.status(401);
        }
        else{
            ctx.json(mapper.writeValueAsString(retrievedAccount));
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        Message postedMessage = messageService.createMessage(message);

        if(postedMessage == null){
            ctx.status(400);
        }
        else{
            ctx.json(mapper.writeValueAsString(postedMessage));
        }
    }

    private void getMessageByMessageIDHandler(Context ctx) throws JsonProcessingException{
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByMessageID(messageID);

        if(message != null){
            ObjectMapper mapper = new ObjectMapper();
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(messageID);

        if(message != null){
            ObjectMapper mapper = new ObjectMapper();
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    private void patchMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        String messageText = message.getMessage_text();

        Message updatedMessage = messageService.updateMessage(messageID, messageText);
        if(updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
        else{
            ctx.status(400);
        }
    }

    private void getMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessagesByUserIDHandler(Context ctx){
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUserID(accountID);
        ctx.json(messages);
    }

}