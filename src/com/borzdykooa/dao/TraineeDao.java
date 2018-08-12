package com.borzdykooa.dao;

import com.borzdykooa.entity.Trainee;
import com.borzdykooa.entity.Trainer;
import com.borzdykooa.connection.ConnectionPool;
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
Класс, сожержащий CRUD-методы для таблицы trainee
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TraineeDao {

    private static final TraineeDao INSTANCE = new TraineeDao();

    private static final Logger log = Logger.getLogger(TraineeDao.class);

    private static final String SAVE =
            "INSERT INTO test_database.trainee (name,trainer_id) VALUES (?,(SELECT id FROM test_database.trainer WHERE id = ?))";

    private static final String GET_ALL_TRAINEES =
            "SELECT " +
                    "te.id          AS trainee_id, " +
                    "te.name        AS trainee_name, " +
                    "tr.id AS trainer_id, " +
                    "tr.name AS trainer_name, " +
                    "tr.language AS trainer_language, " +
                    "tr.experience AS trainer_experience " +
                    "FROM test_database.trainee te " +
                    "INNER JOIN test_database.trainer tr ON te.trainer_id=tr.id " +
                    "ORDER BY trainee_name";

    private static final String DELETE = "DELETE FROM test_database.trainee WHERE name LIKE ?";

    private static final String UPDATE_TRAINER = "UPDATE test_database.trainee SET trainer_id=? WHERE id = ?";

    private static final String FIND_BY_ID = "SELECT " +
            "te.id          AS trainee_id, " +
            "te.name        AS trainee_name, " +
            "tr.id AS trainer_id, " +
            "tr.name AS trainer_name, " +
            "tr.language AS trainer_language, " +
            "tr.experience AS trainer_experience " +
            "FROM test_database.trainee te " +
            "INNER JOIN test_database.trainer tr ON te.trainer_id=tr.id " +
            "WHERE te.id=?";

    public Trainee getById(Long id) {
        log.info("Method getById is called in TraineeDao");
        Trainee trainee = null;
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                trainee = Trainee.builder()
                        .id(resultSet.getLong("trainee_id"))
                        .name(resultSet.getString("trainee_name"))
                        .trainer(Trainer.builder()
                                .id(resultSet.getLong("trainer_id"))
                                .name(resultSet.getString("trainer_name"))
                                .language(resultSet.getString("trainer_language"))
                                .experience(resultSet.getInt("trainer_experience"))
                                .build())
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
        if (trainee != null) {
            log.info(trainee.toString() + " has been found successfully!");
        }
        return trainee;
    }

    public void updateTrainee(Trainee trainee) {
        log.info("Method updateTrainer is called in TraineeDao");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRAINER)) {
            preparedStatement.setLong(1, trainee.getTrainer().getId());
            preparedStatement.setLong(2, trainee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
    }

    public void delete(String name) {
        log.info("Method delete is called in TraineeDao");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
        log.info("Trainees with name '" + name + "' have been deleted successfully!");
    }

    public List<Trainee> getAllTrainees() {
        log.info("Method getAllTrainees is called in TraineeDao");
        List<Trainee> trainees = new ArrayList<Trainee>();
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_TRAINEES);

            while (resultSet.next()) {
                Trainee trainee = Trainee.builder()
                        .id(resultSet.getLong("trainee_id"))
                        .name(resultSet.getString("trainee_name"))
                        .trainer(Trainer.builder()
                                .id(resultSet.getLong("trainer_id"))
                                .name(resultSet.getString("trainer_name"))
                                .language(resultSet.getString("trainer_language"))
                                .experience(resultSet.getInt("trainer_experience"))
                                .build())
                        .build();

                trainees.add(trainee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
        if (trainees.size() != 0) {
            log.info("List of trainees: " + trainees.toString());
        }
        return trainees;
    }

    public void save(Trainee trainee) {
        log.info("Method save is called in TraineeDao");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
            preparedStatement.setString(1, trainee.getName());
            preparedStatement.setLong(2, trainee.getTrainer().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getResultSet();
            log.info("Trainee " + trainee.getName() + " has been saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
    }

    public static TraineeDao getInstance() {
        return INSTANCE;
    }
}
