package socialnetwork.repository.database;

import socialnetwork.domain.Entity;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class AbstractBDRepository <ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    private String url;
    private String username;
    private String password;
    private Validator<E> validator;

    public AbstractBDRepository(String url, String username, String password, Validator<E> validator) {
        super(validator);
        this.validator=validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

   /* @Override
    public E findOne(long id) {

        if (id==null)
            throw new ValidationException("id must not be null");

        try (Connection connection = DriverManager.getConnection(url, username, password)){

            PreparedStatement statement = connection.prepareStatement("SELECT * from users where id = ?");
            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();

            return extractEntity(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes  the parts of the entity
     * @return an entity of type E
     */
   /* public abstract E extractEntity(ResultSet resultSet);
    public abstract E createEntityasStatement(ResultSet resultSet);*/


    /*@Override
    public Iterable<E> findAll() {
        Set<E> entities = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                E entity = extractEntity(resultSet);
                entities.add(entity);
            }
            return entities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }*/

    /*@Override
    public E save(E entity) {
        if (entity==null)
            throw new ValidationException("entity must not be null");
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String query = " INSERT INTO users (id, first_name, last_name, age, fav_food)" + " values (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.execute();
            return null;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }*/

    /*@Override
    public E delete(ID id) {

        if (id==null)
            throw new ValidationException("id must be not null");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            E entity = findOne(id);
            if( entity == null)
                return null;

            String query = " DELETE FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.execute();

            return entity;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*@Override
    public E update(E entity) {
        if (entity==null)
            throw new ValidationException("entity must be not null");
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String query = " UPDATE users SET first_name = ?, last_name = ? where id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());
            int nrRows = statement.executeUpdate();
            if(nrRows == 0)
                return entity;
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
