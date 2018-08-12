package com.borzdykooa.dao;

import com.borzdykooa.connection.ConnectionPool;
import com.borzdykooa.entity.Trainer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
Класс, сожержащий CRUD-методы для таблицы trainer
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TrainerDao {

    private static final Logger log = Logger.getLogger(TrainerDao.class);

    private static final TrainerDao INSTANCE = new TrainerDao();

    private static final String SAVE =
            "INSERT INTO test_database.trainer (name,language,experience) VALUES (?,?,?)";

    private static final String GET_ALL_TRAINERS =
            "SELECT " +
                    "  t.id          AS trainer_id, " +
                    "  t.name        AS trainer_name, " +
                    "  t.language    AS trainer_language, " +
                    "  t.experience  AS trainer_experience " +
                    "FROM test_database.trainer t " +
                    "ORDER BY trainer_name";

    private static final String DELETE = "DELETE FROM test_database.trainer WHERE name LIKE ?";

    private static final String UPDATE_EXPERIENCE = "UPDATE test_database.trainer SET experience=? WHERE id = ?";

    private static final String FIND_BY_ID = "SELECT " +
            "  t.id          AS trainer_id, " +
            "  t.name        AS trainer_name, " +
            "  t.language    AS trainer_language, " +
            "  t.experience  AS trainer_experience " +
            "FROM test_database.trainer t " +
            "WHERE id=?";

    public Trainer getById(Long id) {
        log.info("Method getById is called in TrainerDao");
        Trainer trainer = null;
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                trainer = Trainer.builder()
                        .id(resultSet.getLong("trainer_id"))
                        .name(resultSet.getString("trainer_name"))
                        .language(resultSet.getString("trainer_language"))
                        .experience(resultSet.getInt("trainer_experience"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
        if (trainer != null) {
            log.info(trainer.toString() + " has been found successfully!");
        }
        return trainer;
    }

    public void updateExperience(Trainer trainer) {
        log.info("Method updateExperience is called in TrainerDao");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXPERIENCE)) {
            preparedStatement.setInt(1, trainer.getExperience());
            preparedStatement.setLong(2, trainer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
    }

    public void delete(String name) {
        log.info("Method delete is called in TrainerDao");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error!", e);
            e.printStackTrace();
        }
        log.info("Trainers with name '" + name + "' have been deleted successfully!");
    }

    public List<Trainer> getAllTrainers() {
        log.info("Method getAllTrainers is called in TrainerDao");
        List<Trainer> trainers = new ArrayList<Trainer>();
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_TRAINERS);

            while (resultSet.next()) {
                Trainer trainer = Trainer.builder()
                        .id(resultSet.getLong("trainer_id"))
                        .name(resultSet.getString("trainer_name"))
                        .language(resultSet.getString("trainer_language"))
                        .experience(resultSet.getInt("trainer_experience"))
                        .build();
                trainers.add(trainer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
        if (trainers.size() != 0) {
            log.info("List of trainees: " + trainers.toString());
        }
        return trainers;
    }

    public void save(Trainer trainer) {
        log.info("Method save is called in TrainerDao");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
            preparedStatement.setString(1, trainer.getName());
            preparedStatement.setString(2, trainer.getLanguage());
            preparedStatement.setInt(3, trainer.getExperience());
            preparedStatement.executeUpdate();
            log.info("Trainer " + trainer.getName() + " has been saved successfully ");
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
    }

    public static TrainerDao getInstance() {
        return INSTANCE;
    }
}
