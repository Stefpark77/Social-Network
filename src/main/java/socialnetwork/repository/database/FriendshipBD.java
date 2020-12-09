package socialnetwork.repository.database;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.file.AbstractFileRepository;
import socialnetwork.repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipBD extends InMemoryRepository<Tuple<Long,Long>, Friendship> {

    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;

    public FriendshipBD(String url, String username, String password, Validator<Friendship> validator) {
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
    public Friendship findOne(Tuple<Long,Long> id) {

        if (id==null)
            throw new ValidationException("id must not be null");

        try (Connection connection = DriverManager.getConnection(url, username, password)){

            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships where id1 = ?,id2 = ?");
            statement.setLong(1,id.getLeft());
            statement.setLong(2,id.getRight());
            ResultSet resultSet = statement.executeQuery();

            return extractEntity(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Friendship entity = extractEntity(resultSet);
                friendships.add(entity);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }
    @Override
    public Friendship save(Friendship entity) {
        if (entity==null)
            throw new ValidationException("entity must not be null");
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String query = " INSERT INTO friendships (id1, id2)" + " values (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, entity.getId1());
            statement.setLong(2, entity.getId2());
            statement.execute();
            return null;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Friendship delete(Tuple<Long,Long> id) {

        if (id==null)
            throw new ValidationException("id must be not null");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            Friendship friendship = findOne(id);
            if( friendship == null)
                return null;

            String query = " DELETE FROM friendships WHERE id1 = ?,id2 = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id.getLeft());
            statement.setLong(2, id.getRight());
            statement.execute();

            return friendship;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Friendship extractEntity(ResultSet resultSet) {
     /*   try {
            if(!resultSet.next())
                return null;

            Long id1 = resultSet.getLong("id1");
            Long id2 = resultSet.getLong("id2");
            return new Friendship(id1, id2);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }*/
        return null;
    }
}