package ua.nure.dudka.practice8.db;

import ua.nure.dudka.practice8.db.entity.Team;
import ua.nure.dudka.practice8.db.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class DBManager {
    private static final String INSERT_TEAMS_FOR_USER = "INSERT INTO users_teams VALUES(?, ?)";
    private static final String FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
    private static final String FIND_TEAM_BY_NAME = "SELECT * FROM teams WHERE name=?";
    private static final String INSERT_USER = "INSERT INTO users VALUES(DEFAULT, ?)";
    private static final String INSERT_TEAM = "INSERT INTO teams VALUES(DEFAULT, ?)";
    private static final String UPDATE_TEAM = "UPDATE teams SET name=? WHERE id=?";
    private static final String FIND_ALL_USER_TEAMS = "SELECT teams.id, teams.name FROM teams, users_teams, users" +
            " WHERE users_teams.user_id = ? AND users_teams.user_id = users.id AND users_teams.team_id = teams.id";
    private static final String DELETE_TEAM = "DELETE FROM teams WHERE id=?";
    private static final String FIND_ALL_TEAMS = "SELECT * FROM teams";
    private static final String FIND_ALL_USERS = "SELECT * FROM users";
    private static final String PROPERTY = "connection.url";
    private static DBManager instance;

    private DBManager() {
    }

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }

        return instance;
    }

    public void insertUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getLogin());

            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    long generatedUserId = resultSet.getInt(1);
                    user.setId(generatedUserId);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException("Can't insert user: " + user, e);
        } finally {
            DBUtils.close(resultSet);
            DBUtils.close(preparedStatement);
            DBUtils.close(connection);
        }
    }

    public User getUser(String login) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN);
            preparedStatement.setString(1, login);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long id = resultSet.getInt(1);
                user = new User(id, login);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException("Can't find user by login: " + login, e);
        } finally {
            DBUtils.close(resultSet);
            DBUtils.close(preparedStatement);
            DBUtils.close(connection);
        }

        return user;
    }

    public List<User> findAllUsers() {
        List<User> list = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_USERS);

            while (resultSet.next()) {
                long id = resultSet.getInt(1);
                String login = resultSet.getString(2);
                list.add(new User(id, login));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException("Can't find all users", e);
        } finally {
            DBUtils.close(resultSet);
            DBUtils.close(statement);
            DBUtils.close(connection);
        }

        return list;
    }

    public void insertTeam(Team team) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            preparedStatement = connection.prepareStatement(INSERT_TEAM, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, team.getName());

            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    long generatedGroupId = resultSet.getInt(1);
                    team.setId(generatedGroupId);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException("Can't insert team: " + team, e);
        } finally {
            DBUtils.close(resultSet);
            DBUtils.close(preparedStatement);
            DBUtils.close(connection);
        }
    }

    public Team getTeam(String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Team team = null;

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            preparedStatement = connection.prepareStatement(FIND_TEAM_BY_NAME);
            preparedStatement.setString(1, name);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long id = resultSet.getInt(1);
                team = new Team(id, name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException("Can't find team with name: " + name, e);
        } finally {
            DBUtils.close(resultSet);
            DBUtils.close(preparedStatement);
            DBUtils.close(connection);
        }

        return team;
    }

    public List<Team> findAllTeams() {
        List<Team> list = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_TEAMS);

            while (resultSet.next()) {
                long id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                list.add(new Team(id, name));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException("Can't find all groups", e);
        } finally {
            DBUtils.close(resultSet);
            DBUtils.close(statement);
            DBUtils.close(connection);
        }

        return list;
    }

    public void updateTeam(Team team) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            preparedStatement = connection.prepareStatement(UPDATE_TEAM);
            preparedStatement.setString(1, team.getName());
            preparedStatement.setInt(2, (int) team.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException("Can't update team: " + team, e);
        } finally {
            DBUtils.close(preparedStatement);
            DBUtils.close(connection);
        }
    }

    public void deleteTeam(Team team) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            preparedStatement = connection.prepareStatement(DELETE_TEAM);
            preparedStatement.setInt(1, (int) team.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException("Can't delete team: " + team, e);
        } finally {
            DBUtils.close(preparedStatement);
            DBUtils.close(connection);
        }
    }

    public void setTeamsForUser(User user, Team... teams) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int userId = (int) user.getId();

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            DBUtils.adjustConnectionForTransaction(connection);
            for (Team team : teams) {
                preparedStatement = connection.prepareStatement(INSERT_TEAMS_FOR_USER);
                preparedStatement.setInt(1, userId);

                int groupId = (int) team.getId();
                preparedStatement.setInt(2, groupId);
                preparedStatement.executeUpdate();
                DBUtils.close(preparedStatement);
            }

            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBUtils.rollback(connection);
            throw new DBException("Can't set group for user: " + user, e);
        } finally {
            DBUtils.close(preparedStatement);
            DBUtils.close(connection);
        }
    }

    public List<Team> getUserTeams(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Team> list = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(DBUtils.getProperty(PROPERTY));
            preparedStatement = connection.prepareStatement(FIND_ALL_USER_TEAMS);
            preparedStatement.setInt(1, (int) user.getId());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long groupId = resultSet.getInt(1);
                String name = resultSet.getString(2);
                list.add(new Team(groupId, name));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException("Can't get teams for user: " + user, e);
        } finally {
            DBUtils.close(resultSet);
            DBUtils.close(preparedStatement);
            DBUtils.close(connection);
        }

        return list;
    }

}
