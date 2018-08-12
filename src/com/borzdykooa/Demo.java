package com.borzdykooa;

import com.borzdykooa.dao.TraineeDao;
import com.borzdykooa.dao.TrainerDao;
import com.borzdykooa.entity.Trainee;
import com.borzdykooa.entity.Trainer;
import org.apache.log4j.Logger;

/*
Класс для демонстрации работы приложения
 */
public class Demo {

    private static final Logger log = Logger.getLogger(Demo.class);

    public static void main(String[] args) {
        Trainer andreiReut = new Trainer("Andrei Reut", "Java", 6);
        Trainer ivanIvanov = new Trainer("Ivan Ivanov", "C++", 4);
        Trainer petrPetrov = new Trainer("Petr Petrov", "C#", 5);
        TrainerDao.getInstance().save(andreiReut);
        TrainerDao.getInstance().save(ivanIvanov);
        TrainerDao.getInstance().save(petrPetrov);
        TrainerDao.getInstance().getAllTrainers();

        Trainer savedAndreiReut = TrainerDao.getInstance().getById(1L);
        Trainee olgaBorzdyko = new Trainee("Olga Borzdyko", savedAndreiReut);
        TraineeDao.getInstance().save(olgaBorzdyko);
        Trainee denisByhovsky = new Trainee("Denis Byhovsky", savedAndreiReut);
        TraineeDao.getInstance().save(denisByhovsky);
        Trainee sergeiSergeev = new Trainee("Sergei Sergeev", savedAndreiReut);
        TraineeDao.getInstance().save(sergeiSergeev);

        TrainerDao.getInstance().getAllTrainers();
        TrainerDao.getInstance().delete("petr");
        TrainerDao.getInstance().getAllTrainers();

        TraineeDao.getInstance().getAllTrainees();
        TraineeDao.getInstance().delete("serg");
        TraineeDao.getInstance().getAllTrainees();

        Trainer savedIvanIvanov = TrainerDao.getInstance().getById(2L);
        Trainee savedOlgaBorzdyko = TraineeDao.getInstance().getById(1L);
        savedOlgaBorzdyko.setTrainer(savedIvanIvanov);
        savedIvanIvanov.setExperience(22);
        TrainerDao.getInstance().updateExperience(savedIvanIvanov);
        TrainerDao.getInstance().getById(2L);
        TraineeDao.getInstance().updateTrainee(savedOlgaBorzdyko);
        TraineeDao.getInstance().getById(1L);
    }
}
