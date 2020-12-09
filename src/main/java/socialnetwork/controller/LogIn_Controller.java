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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.GroupService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.events.UserChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LogIn_Controller implements Observer<UserChangeEvent> {

    ObservableList<User> modelGrade = FXCollections.observableArrayList();
    private UserService user_crt;
    private FriendshipService friendscrt;
    private MessageService messagecrt;
    private GroupService groupcrt;

    @FXML
    TableColumn<User, Long> tableColumnID;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableColumn<User, String> tableColumnAge;
    @FXML
    TableColumn<User, String> tableColumnFavFood;
    @FXML
    TableView<User> TableViewUsers;
    @FXML
    TextField TextFIeldID;
    @FXML
    Button logInButton;

    @FXML
    public void initialize() {
        TableViewUsers.setItems(modelGrade);
        tableColumnID.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnAge.setCellValueFactory(new PropertyValueFactory<User, String>("age"));
        tableColumnFavFood.setCellValueFactory(new PropertyValueFactory<User, String>("favouriteFood"));

    }

    private List<User> getUserList() {
        return StreamSupport.stream(user_crt.getAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    public void setService(UserService service, FriendshipService friendscrt ,MessageService messagecrt,GroupService groupcrt) {
        this.user_crt= service;
        this.friendscrt=friendscrt;
        this.messagecrt=messagecrt;
        this.groupcrt=groupcrt;
        initModel();
    }
    private void initModel(){
        modelGrade.setAll(getUserList());
    }
    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }

    private void showProfile(User u) throws IOException{
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/Profile.fxml"));
            AnchorPane root=loader.load();

            Profile_Controller ctrl=loader.getController();
            ctrl.setService(user_crt,friendscrt,messagecrt,groupcrt,TextFIeldID.getText());

            Stage stage=new Stage();
            stage.setScene(new Scene(root, 800, 740));
            stage.setTitle("Profile of User " + u.getLastName() + " " + u.getFirstName());
            ctrl.setPrimaryStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogIn(ActionEvent actionEvent) {
        try {
            String id =TextFIeldID.getText();
            if(id.equals("")){
                Allert_Controller.showErrorMessage(null, "No Id was given!");
                return;
            }
            User u=user_crt.getUser(id);
            UserValidator.idValidate(id);
            UserValidator.idExistenceValidate(id,user_crt.getAll());
            showProfile(u);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ValidationException | IllegalArgumentException e){
            Allert_Controller.showErrorMessage(null,e.getMessage());
        }
    }
}
