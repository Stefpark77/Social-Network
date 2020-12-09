package socialnetwork.repository.file;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.database.AbstractBDRepository;
import socialnetwork.repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserBD  extends InMemoryRepository<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserBD(String url, String username, String password, Validator<User> validator) {
        super(validator);
        this.validator=validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes  the parts of the entity
     * @return an entity of type E
     */
    @Override
    public User findOne(Long id) {

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
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User entity = extractEntity(resultSet);
                users.add(entity);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    @Override
    public User save(User entity) {
        if (entity==null)
            throw new ValidationException("entity must not be null");
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String query = " INSERT INTO users (id, first_name, last_name, age, fav_food)" + " values (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getAge());
            statement.setString(5, entity.getFavouriteFood());
            statement.execute();
            return null;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public User delete(Long id) {

        if (id==null)
            throw new ValidationException("id must be not null");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            User user = findOne(id);
            if( user == null)
                return null;

            String query = " DELETE FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.execute();

            return user;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User entity) {
        if (entity==null)
            throw new ValidationException("entity must be not null");
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String query = " UPDATE users SET first_name = ?, last_name = ?, age = ?,fav_food = ? where id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getAge());
            statement.setString(4, entity.getFavouriteFood());
            statement.setLong(5, entity.getId());
            int nrRows = statement.executeUpdate();
            if(nrRows == 0)
                return entity;
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User extractEntity(ResultSet resultSet) {
        try {
            if(!resultSet.next())
                return null;

            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String age = resultSet.getString("age");
            String fav_food = null;
                fav_food = resultSet.getString("fav_food");
            User user = new User(firstName, lastName,age,fav_food);
            user.setId(id);
            return user;
        }
         catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

}
