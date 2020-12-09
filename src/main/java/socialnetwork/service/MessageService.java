package socialnetwork.service;

import socialnetwork.domain.Message;
import socialnetwork.domain.Messagetype;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.ChangeEvent;
import socialnetwork.utils.events.MessageorGroupChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageService implements Observable<MessageorGroupChangeEvent> {
    private UserService user_crt;
    private FriendshipService friends_crt;
    private Repository<Long, Message> repo;
    private List<Observer<MessageorGroupChangeEvent>> observers=new ArrayList<>();

    public MessageService(Repository<Long, Message> repo, UserService user_crt, FriendshipService friends_crt) {
        this.repo = repo;
        this.user_crt = user_crt;
        this.friends_crt = friends_crt;
    }
    /**
     * create an id which isn't used at the moment by noone
     * @return a new id
     */
    public long getNewId(){
        Iterable<Message> messages=getAll();
        long i=0;
        for(Message m:messages){
            if(m.getId()>i){
                i=m.getId();
            }
        }
        return i+1;
    }

    /**
     * add a new chat message /modify an existing chat message with a reply
     * @param id_from_s the id of the sender
     * @param id_to_s the id of the User or Group who will get the message
     * @param message the content of the message
     * @param type the type of message(group or user)
     * @return The New added message
     * @throws ValidationException
     *            if the params are invalid
     */
    public Message addMessage(String id_from_s, String id_to_s, String message, String type) {
        MessageValidator.idValidate(id_from_s);
        MessageValidator.idValidate(id_to_s);
        Message mess=new Message(Long.parseLong(id_from_s),Long.parseLong(id_to_s),message,type,LocalDateTime.now());
        mess.setId(getNewId());
        Message rez=repo.save(mess);
        notifyObservers(new MessageorGroupChangeEvent(ChangeEvent.ADD,rez));
        return rez;
    }

    /**
     * reply in a group chat
     * @param id_from_s the id of the User who wants to reply
     * @param id_group the id of the Group Chat
     * @param message the reply of the User
     * @return the the Group Chat with the new reply
     * @throws ValidationException
     *            if the params are invalid
     *//*
    public Message respondGroupchat(String id_from_s,String id_group, String message){
        Long id_aux;
        User from_aux=new User();
        List<User> to_aux=new ArrayList<>();
        Message mess=new Message();
        for(Message msg : getAll()){
            if(msg.getId()==Long.parseLong(id_group)){
                id_aux = msg.getId();
                from_aux=msg.getFrom();
                String m = msg.getMessage();
                String r = msg.getReply();//it works ,dont touch it!

                repo.delete(id_aux);//le stergem

                mess = new Message(from_aux,msg.getTo(),m+"*",r+"*"+id_from_s+":"+message);
                mess.setDate(LocalDateTime.now());
                mess.setId(id_aux);//le inlocuim
                break;
            }
        }//Update Existing Chat
        Message rez=repo.save(mess);
        notifyObservers(new MessageChangeEvent());
        return rez;
    }*/

    /**
     * get the conversation beetween two users(by their ip)
     * @param id1 the id of the 1st user
     * @param id2 the id of the second user
     * @param type the type of the chat
     * @return String(the conversation beetween the two given users)
     * @throws ValidationException
     *            if the ids are invalid
     */
    public List<String> Conv(String id1, String id2,String type){
        int index = 0;
        MessageValidator.idValidate(id1);
        String aux;
        List<String> Conversation=new ArrayList<>();
        if(type.equals("groupmessage")){
            for(Message msg : getAll()){
                if(msg!=null){
                    if(String.valueOf(msg.getTo_id()).equals(id2) && msg.getType()==Messagetype.groupmessage){
                        String id1m=String.valueOf(msg.getFrom_id());
                        Conversation.add(user_crt.getUser(id1m).getFirstName() + " " +user_crt.getUser(id1m).getLastName() + ": " + msg.getMessage());
                    }
                }
            }
        }else if(type.equals("privatemessage")){
            for(Message msg : getAll()){
                if(msg!=null){
                    if(String.valueOf(msg.getTo_id()).equals(id2) && String.valueOf(msg.getFrom_id()).equals(id1) && msg.getType()==Messagetype.privatemessage){
                        Conversation.add(user_crt.getUser(id1).getFirstName() + " " +user_crt.getUser(id1).getLastName() + ": " + msg.getMessage());
                    }
                    if(String.valueOf(msg.getTo_id()).equals(id1) && String.valueOf(msg.getFrom_id()).equals(id2)&& msg.getType()==Messagetype.privatemessage){
                        Conversation.add(user_crt.getUser(id2).getFirstName() + " " +user_crt.getUser(id2).getLastName() + ": " + msg.getMessage());
                    }
                }
            }
        }
        return Conversation;
    }

    /**
     * @return all entities
     */
    public Iterable<Message> getAll(){
        return repo.findAll();
    }

    @Override
    public void addObserver(Observer<MessageorGroupChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageorGroupChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageorGroupChangeEvent t) {
        observers.stream()
                .forEach(x->x.update(t));
    }
}
