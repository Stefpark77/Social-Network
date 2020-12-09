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
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.GroupValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.FriendshipFile;
import socialnetwork.repository.file.GroupFile;
import socialnetwork.repository.file.MessageFile;
import socialnetwork.repository.file.UserFile;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.GroupService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;

public class MainFX extends Application {
    private UserService usercrt;
    private FriendshipService friendscrt;
    private MessageService messagecrt;
    private GroupService groupcrt;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/Menu.fxml"));
        AnchorPane root=loader.load();

        String fileName_users= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String fileName_friends=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friendships");
        String fileName_messages=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.messages");
        String fileName_groups=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.groups");
        //repository
        Repository<Long, User> userFileRepository = new UserFile(fileName_users, new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository= new FriendshipFile(fileName_friends, new FriendshipValidator());
        Repository<Long, Message> messageFileRepository = new MessageFile(fileName_messages, new MessageValidator());
        Repository<Long, Group> GroupFileRepository = new GroupFile(fileName_groups, new GroupValidator());
        //services
        this.usercrt=new UserService(userFileRepository);
        this.friendscrt=new FriendshipService(friendshipFileRepository);
        this.messagecrt=new MessageService(messageFileRepository,usercrt,friendscrt);
        this.groupcrt=new GroupService(GroupFileRepository);


        init1(primaryStage);

    }

    private void init1(Stage primaryStage) throws Exception{
        //main menu

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/Menu.fxml"));
        AnchorPane root=loader.load();

        LogIn_Controller ctrl=loader.getController();
        ctrl.setService(usercrt,friendscrt,messagecrt,groupcrt);

        primaryStage.setScene(new Scene(root, 750, 450));
        primaryStage.setTitle("My Social Network");
        primaryStage.show();

    }


}
