package socialnetwork;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import socialnetwork.config.ApplicationContext;
import socialnetwork.controller.LogIn_Controller;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.*;
import socialnetwork.repository.file.*;
import socialnetwork.service.*;

public class MainFX extends Application {
    private UserService usercrt;
    private FriendshipService friendscrt;
    private MessageService messagecrt;
    private GroupService groupcrt;
    private RealEventService eventcrt;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //repository in file
        /*String fileName_users= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String fileName_friends=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friendships");
        String fileName_messages=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.messages");
        String fileName_groups=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.groups");
        String fileName_events=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.events");

        Repository<Long, User> userFileRepository = new UserFile(fileName_users, new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository= new FriendshipFile(fileName_friends, new FriendshipValidator());
        Repository<Long, Message> messageFileRepository = new MessageFile(fileName_messages, new MessageValidator());
        Repository<Long, Group> GroupFileRepository = new GroupFile(fileName_groups, new GroupValidator());
        RealEventFile realEventRepository = new RealEventFile(fileName_events, new RealEventValidator());*/

        //repository in db
        Repository<Long, User> userBDRepository = new UserBD("jdbc:postgresql://localhost:5432/postgres","postgres","password", new UserValidator());
        Repository<Long, Message> messageBDRepository = new MessageBD("jdbc:postgresql://localhost:5432/postgres","postgres","password", new MessageValidator());
        Repository<Tuple<Long,Long>, Friendship> friendshipBDRepository= new FriendshipBD("jdbc:postgresql://localhost:5432/postgres","postgres","password", new FriendshipValidator());
        Repository<Long, Group> GroupBDRepository = new GroupBD("jdbc:postgresql://localhost:5432/postgres","postgres","password", new GroupValidator());
        RealEventBD realEventBDRepository = new RealEventBD("jdbc:postgresql://localhost:5432/postgres","postgres","password", new RealEventValidator());

        //services
        this.usercrt=new UserService(userBDRepository);
        this.friendscrt=new FriendshipService(friendshipBDRepository);
        this.messagecrt=new MessageService(messageBDRepository,usercrt,friendscrt);
        this.groupcrt=new GroupService(GroupBDRepository);
        this.eventcrt=new RealEventService(realEventBDRepository);
        init1(primaryStage);

    }

    private void init1(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/Menu.fxml"));
        AnchorPane root=loader.load();

        LogIn_Controller ctrl=loader.getController();
        ctrl.setService(usercrt,friendscrt,messagecrt,groupcrt,eventcrt);
        ctrl.setPrimaryStage(primaryStage);

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setTitle("My Social Network");
        primaryStage.show();
    }


}
