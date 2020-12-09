package socialnetwork.service;

import socialnetwork.domain.Group;
import socialnetwork.domain.validators.GroupValidator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.ChangeEvent;
import socialnetwork.utils.events.MessageorGroupChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GroupService implements Observable<MessageorGroupChangeEvent> {
    private Repository<Long, Group> repo;
    private List<Observer<MessageorGroupChangeEvent>> observers=new ArrayList<>();

    public GroupService(Repository<Long, Group> repo) {
        this.repo = repo;
    }

    /**
     * create an id which isn't used at the moment by noone
     * @return a new id
     */
    public long getNewId(){
        Iterable<Group> groups=getAll();
        long i=0;
        for(Group g:groups){
            if(g.getId()>i){
                i=g.getId();
            }
        }
        return i+1;
    }

    /**
     * adds a new user
     * @param name
     *          must be a string not null
     * @param ids
     *          must have at least 2 ids
     * @param nr
     *          must be a number greater than 0
     * @return Group
     *          if the Group was added succesfully
     * @throws ValidationException
     *            if the params are invalid
     */
    public Group addGroup(String name, List<Long> ids,Long nr) {
        Group new_group=new Group(ids,nr,name,LocalDateTime.now());
        new_group.setId(getNewId());
        Group rez=repo.save(new_group);
        notifyObservers(new MessageorGroupChangeEvent(ChangeEvent.ADD,rez));
        return rez;
    }

    public Group leaveGroup(String id_group,String id_user){
        Group group_to_update;
        for(Group g:getAll()){
            if(g.getId()==Long.parseLong(id_group)){
                group_to_update=g;
                for(Long id_index:g.getIds()){
                    if(id_index==Long.parseLong(id_user)){
                        g.getIds().remove(id_index);
                        if(g.getIds().isEmpty()){
                            group_to_update=removeGroup(id_group);
                        }
                        notifyObservers(new MessageorGroupChangeEvent(ChangeEvent.UPDATE,group_to_update));
                        return group_to_update;
                    }
                }
            }
        }
        return null;

    }
    /**
     * removes a User
     * @param id_to_remove
     *          must not be null
     * @return User
     *          if the User was removed succesfully
     * @throws ValidationException
     *            if the id_to_remove is invalid
     */
    public Group removeGroup(String id_to_remove) {
        GroupValidator.idValidate(id_to_remove);
        Group rez=repo.delete(Long.parseLong(id_to_remove));
        notifyObservers(new MessageorGroupChangeEvent(ChangeEvent.DELETE,rez));
        return rez;
    }

    /**
     * get the Group from the list of Groups by their ip
     * @param id the id we want to find in the list of Users
     * @return the Group we were searching for
     *
     * @throws ValidationException
     *            if the id is not valid
     */
    public Group getGroup(String id){
        GroupValidator.idValidate(id);
        Group g=repo.findOne(Long.parseLong(id));
        return g;
    }

    /**
     * @return all entities
     */
    public Iterable<Group> getAll(){
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
