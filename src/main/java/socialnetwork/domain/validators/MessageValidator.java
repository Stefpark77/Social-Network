package socialnetwork.domain.validators;

import socialnetwork.domain.Message;
import socialnetwork.domain.User;

import java.util.List;

public class MessageValidator  implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        //TODO
    }
    /**
     * @param id_s -the id of the entity to be validated
     *                 id must not be null
     * @throws ValidationException
     *            if the id is not valid
     */
    public static void idValidate(String id_s) throws ValidationException{
        Long id;
        try{
            id=Long.parseLong(id_s);
        }catch(NumberFormatException exp){
            throw new ValidationException("The id is invalid!(the id needs to be a number greater than 0)");
        }
        if(id<=0){
            throw new ValidationException("The id needs to be greater than 0!");
        }
    }

    public static void NumberValidate(String nr) throws ValidationException{
        Long Nr;
        try{
            Nr=Long.parseLong(nr);
        }catch(NumberFormatException exp){
            throw new ValidationException("You need to enter a number!");
        }
        if(Nr<=0){
            throw new ValidationException("The number needs to be greater than 0!");
        }
    }

    /*public static void differentidsValidate(String id,List<String> id_list,String id_from) throws ValidationException{
        if(id.equals(id_from)){
            throw new ValidationException("The receiver id cant have the same id that the sender has!");
        }
        for(String otherid:id_list){
            if(id.equals(otherid)){
                throw new ValidationException("The Receivers ids must be different!");
            }
        }
    }
*/
    /**
     * @param id_s the id we need to search in the list
     * @param messages the list in which we verify the existence of a group chat with the given id
     * @param from the id of the person who should be in the group chat
     * @throws ValidationException
     *            if the id is not valid
     */
    /*public static void idExistenceValidatefrom(String id_s,Iterable<Message> messages,String from)throws ValidationException{
        idValidate(id_s);
        Long id=Long.parseLong(id_s);
        for(Message m:messages){
            if(m.getId().equals(id)){
                for(User u:m.getTo()){
                    if(u.getId()==Long.parseLong(from)){
                        return;
                    }
                }
                throw new ValidationException("That Group Chat doesnt have the user in it!");
            }
        }
        throw new ValidationException("There is no Group Chat with that id!");
    }*/
}
