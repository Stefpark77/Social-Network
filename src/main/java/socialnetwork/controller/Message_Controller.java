package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.Group;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.GroupService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;
import socialnetwork.utils.events.MessageorGroupChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Message_Controller implements Observer<MessageorGroupChangeEvent> {
    Stage primaryStage;
    private UserService user_crt;
    private MessageService messagecrt;
    private GroupService groupcrt;
    private User user;
    private User user2=null;
    private Group group=null;
    private String typeofchat="privatemessage";
    ObservableList<Group> modelGrade = FXCollections.observableArrayList();
    @Override
    public void update(MessageorGroupChangeEvent messageChangeEvent) {
        showChat();
        initModel();
    }
    public void setPrimaryStage(Stage s){
        primaryStage=s;
    }

    public void setService(UserService service , MessageService messagecrt, GroupService groupcrt, User u) {
        this.user_crt= service;
        this.messagecrt=messagecrt;
        this.groupcrt=groupcrt;
        this.user=u;
        messagecrt.addObserver(this);
        groupcrt.addObserver(this);
        initModel();
    }

    private List<Group> getAllGroupsofUser(Long id){
        return StreamSupport.stream(groupcrt.getAll().spliterator(),false)
                //.filter(c -> c.findid(id))
                .filter(c -> c.getIds().contains(id))
                .collect(Collectors.toList());
    }

    private void initModel() {
        modelGrade.setAll(getAllGroupsofUser(user.getId()));
    }

    @FXML
    public void initialize() {
        GroupTable.setItems(modelGrade);
        DateColumn.setCellValueFactory(new PropertyValueFactory<Group, LocalDateTime>("date"));
        Group_NameColumn.setCellValueFactory(new PropertyValueFactory<Group, String>("name"));
    }

    @FXML
    void handleCreateGroup(ActionEvent event) {
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/CreateGroup.fxml"));
            AnchorPane root=loader.load();

            CreateGroup_Controller ctrl=loader.getController();
            ctrl.setService(user_crt,groupcrt,user);

            Stage stage=new Stage();
            stage.setScene(new Scene(root, 500, 440));
            stage.setTitle("Create Group as " + user.getLastName() + " " + user.getFirstName());
            ctrl.setPrimaryStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ValidationException | IllegalArgumentException e){
            Allert_Controller.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    void handleExit(ActionEvent event) {
        messagecrt.removeObserver(this);
        primaryStage.close();
    }

    @FXML
    void handleGroupChat(ActionEvent event) {
        group=GroupTable.getSelectionModel().getSelectedItem();
        if(group==null){
            Allert_Controller.showErrorMessage(null, "No group was selected!");
            return;
        }
        //TODO
        //Validare grup??
        user2=null;
        typeofchat="groupmessage";
        showChat();

    }

    @FXML
    void handleLeaveGroup(ActionEvent event) {
        Group group_to_leave=GroupTable.getSelectionModel().getSelectedItem();
        if(group_to_leave==null){
            Allert_Controller.showErrorMessage(null, "No group was selected!");
            return;
        }
        messagecrt.addMessage(String.valueOf(user.getId()),String.valueOf(group_to_leave.getId()),"-left the Group","groupmessage");
        groupcrt.leaveGroup(String.valueOf(group_to_leave.getId()),String.valueOf(user.getId()));
        if(group==group_to_leave){
            group=null;
        }
        showChat();
    }

    private void showChat(){
        List<String> messages;
        TheChat.setText("");
        if(typeofchat.equals("groupmessage") && group!=null){
            messages=messagecrt.Conv(String.valueOf(user.getId()),String.valueOf(group.getId()),typeofchat);
            for(String line:messages){
                TheChat.appendText(line+"\n");
            }
        }
        if(typeofchat.equals("privatemessage") && user2!=null){
            messages=messagecrt.Conv(String.valueOf(user.getId()),String.valueOf(user2.getId()),typeofchat);
            for(String line:messages){
                TheChat.appendText(line+"\n");
            }
        }

    }

    @FXML
    void handlePrivateChat(ActionEvent event) {
        try {
            String id2 =privatechatidtext.getText();
            if(id2.equals("")){
                Allert_Controller.showErrorMessage(null, "No Id was given!");
                return;
            }
            UserValidator.idValidate(id2);
            UserValidator.idExistenceValidate(id2,user_crt.getAll());
            user2=user_crt.getUser(id2);
            typeofchat="privatemessage";
            group=null;
            showChat();
        }  catch (ValidationException | IllegalArgumentException e){
            Allert_Controller.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    void handleSendMessage(ActionEvent event) {
        try {
            String mess =MessageField.getText();
            MessageField.setText("");
            if(mess.equals("")){
                Allert_Controller.showErrorMessage(null, "No Message was written!");
                return;
            }
            if(user2==null && typeofchat.equals("privatemessage")){
                Allert_Controller.showErrorMessage(null, "No Chat was selected!");
                return;
            }
            if(group==null && typeofchat.equals("groupmessage")){
                Allert_Controller.showErrorMessage(null, "No Chat was selected!");
                return;
            }
            if(user2!=null){
                messagecrt.addMessage(String.valueOf(user.getId()),String.valueOf(user2.getId()),mess,typeofchat);
            }
            if(group!=null){
                messagecrt.addMessage(String.valueOf(user.getId()),String.valueOf(group.getId()),mess,typeofchat);
            }
        }  catch (ValidationException | IllegalArgumentException e){
            Allert_Controller.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    private Button ExitButton;

    @FXML
    private TableView<Group> GroupTable;

    @FXML
    private TableColumn<Group, LocalDateTime> DateColumn;

    @FXML
    private TableColumn<Group, String> Group_NameColumn;

    @FXML
    private TextField privatechatidtext;

    @FXML
    private Button CreateGroupButton;

    @FXML
    private Button LeaveGroupButton;

    @FXML
    private Button OpenPrivateChatButton;

    @FXML
    private TextArea TheChat;

    @FXML
    private Button GroupChatButton;

    @FXML
    private Button SendMessageButton;

    @FXML
    private TextField MessageField;

}
