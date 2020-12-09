package socialnetwork;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Message;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.FriendshipFile;
import socialnetwork.repository.file.MessageFile;
import socialnetwork.repository.file.UserFile;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;

public class Main {
    public static void main(String[] args) {
        /*//files
        String fileName_users=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String fileName_friends=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friendships");
        String fileName_messages=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.messages");
        //repository
        Repository<Long, User> userFileRepository = new UserFile(fileName_users, new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository= new FriendshipFile(fileName_friends, new FriendshipValidator());
        Repository<Long, Message> messageFileRepository = new MessageFile(fileName_messages, new MessageValidator());
        //services
        UserService usercrt=new UserService(userFileRepository);
        FriendshipService friendscrt=new FriendshipService(friendshipFileRepository);
        MessageService messagecrt=new MessageService(messageFileRepository,usercrt,friendscrt);
        //UI start:
        //UI ui=new UI(usercrt,friendscrt,messagecrt);
        //ui.run();

        //GUI start:*/
        MainFX.main(args);
    }
}


