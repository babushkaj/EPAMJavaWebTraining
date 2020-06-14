CREATE DATABASE hospital_mng
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE hospital_mng;

CREATE TABLE user_role (
  id        BIGINT AUTO_INCREMENT,
  role_name VARCHAR(10) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE days_of_week (
  id       BIGINT AUTO_INCREMENT,
  day_name VARCHAR(9) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user_account (
  id         BIGINT AUTO_INCREMENT,
  login      VARCHAR(25) UNIQUE  NOT NULL,
  password   VARCHAR(35)         NOT NULL,
  is_blocked BOOLEAN DEFAULT "0" NOT NULL,
  role_id    BIGINT DEFAULT "2"  NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (role_id) REFERENCES user_role (id)
);

CREATE TABLE user_info (
  id         BIGINT AUTO_INCREMENT,
  first_name VARCHAR(25)        NOT NULL,
  last_name  VARCHAR(25)        NOT NULL,
  email      VARCHAR(30) UNIQUE NOT NULL,
  phone      VARCHAR(10),
  user_id    BIGINT             NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE
);

CREATE TABLE specialization (
  id        BIGINT AUTO_INCREMENT,
  spec_name VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE doctor_info (
  id          BIGINT AUTO_INCREMENT,
  description VARCHAR(700) NOT NULL,
  user_id     BIGINT        NOT NULL,
  spec_id     BIGINT        NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE,
  FOREIGN KEY (spec_id) REFERENCES specialization (id)
);

CREATE TABLE doc_working_days (
  id        BIGINT AUTO_INCREMENT,
  doctor_id BIGINT NOT NULL,
  day_id    BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (doctor_id) REFERENCES doctor_info (id) ON DELETE CASCADE,
  FOREIGN KEY (day_id) REFERENCES days_of_week (id)  ON DELETE CASCADE
);

CREATE TABLE address (
  id        BIGINT AUTO_INCREMENT,
  region    VARCHAR(15) NOT NULL,
  city      VARCHAR(25) NOT NULL,
  street    VARCHAR(25),
  house     VARCHAR(5),
  apartment VARCHAR(5),
  user_id   BIGINT      NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE
);

CREATE TABLE visit_status (
  id          BIGINT AUTO_INCREMENT,
  status_name VARCHAR(10) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE visit (
  id           BIGINT AUTO_INCREMENT,
  cause        VARCHAR(300) NOT NULL,
  result       VARCHAR(600),
  visit_date   BIGINT    NOT NULL,
  visitor_id   BIGINT       NOT NULL,
  doctor_id    BIGINT       NOT NULL,
  status_id    BIGINT       NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (visitor_id) REFERENCES user_account (id) ON DELETE CASCADE,
  FOREIGN KEY (doctor_id) REFERENCES user_account (id) ON DELETE CASCADE,
  FOREIGN KEY (status_id) REFERENCES visit_status (id)
);

CREATE TABLE visit_feedback (
  id           BIGINT AUTO_INCREMENT,
  visitor_mess VARCHAR(300),
  doctor_mess  VARCHAR(300),
  visit_id     BIGINT  NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (visit_id) REFERENCES visit (id) ON DELETE CASCADE
);

INSERT INTO user_role (role_name)
VALUES ("ADMIN"),
       ("VISITOR"),
       ("DOCTOR");

INSERT INTO days_of_week (day_name)
VALUES ("MONDAY"),
       ("TUESDAY"),
       ("WEDNESDAY"),
       ("THURSDAY"),
       ("FRIDAY"),
       ("SATURDAY"),
       ("SUNDAY");

INSERT INTO specialization (spec_name)
VALUES ("THERAPIST"),
       ("OCULIST"),
       ("OTOLARYNGOLOGIST"),
       ("SURGEON"),
       ("TRAUMATOLOGIST");

INSERT INTO visit_status (status_name)
VALUES ("PLANNED"),
       ("CANCELED"),
       ("COMPLETED");

INSERT INTO user_account (login, password, role_id)
VALUES ("VisitorOne", "8b421a5ee130e0b50c449ad07a27da3c", 2),
       ("VisitorTwo", "7764428c1b4efe3d2bcb79c1392108af", 2),
       ("VisitorThree", "a932e897ba14b49014cb035df282919f", 2),
       ("DoctorOne", "f960e2983c9cd82a5d06e6e7f75b9fe1", 3),
       ("DoctorTwo", "1721d4963c05a3082a0cdd29ef3df696", 3),
       ("DoctorThree", "8b72ae61add0c32fb5eff35d96c0a907", 3),
       ("DoctorFour", "adfca65a8fb8cd97c6e8d25e8a5db09a", 3),
       ("DoctorFive", "8c5666524e045caa5ed08867171dca6c", 3),
       ("Administrator", "200ceb26807d6bf99fd6f4f0d1ca54d4", 1);

INSERT INTO address (region, city, street, house, apartment, user_id)
VALUES ("Минская", "Солигорск", "Шахтёров", "12а", "21", "1"),
       ("Гомельская", "Мозырь", "Ленина", "32", "6", "2"),
       ("Витебская", "Полоцк", "Советская", "4", "34", "3"),
       ("Минская", "Минск", "Хоружей", "54", "4", "4"),
       ("Минская", "Минск", "Антоновская", "12", "24", "5"),
       ("Минская", "Минск", "Богдановича", "112", "2", "6"),
       ("Минская", "Минск", "Ленина", "2", "54", "7"),
       ("Минская", "Минск", "Пушкина", "87", "55", "8");

INSERT INTO user_info (first_name, last_name, email, phone, user_id)
VALUES ("Алексей", "Котенков", "visitorone1@gmail.com", "29-1234567", "1"),
       ("Алоизий", "Магарыч", "visitortwo2@gmail.com", "29-1231231", "2"),
       ("Остап", "Бендер", "visitorthree3@gmail.com", "29-9898765", "3"),
       ("Андрей", "Воробей", "doctorone1@gmail.com", "44-3232324", "4"),
       ("Евгений", "Уверенный", "doctorone2@gmail.com", "33-1231231", "5"),
       ("Елена", "Синяя", "doctorone3@gmail.com", "33-6531378", "6"),
       ("Сергей", "Молодец", "doctorone4@gmail.com", "29-1234325", "7"),
       ("Татьяна", "Молодец", "doctorone5@gmail.com", "25-9935383", "8");

INSERT INTO doctor_info (description, user_id, spec_id)
VALUES ("Здесь должно быть описание врача: образование (окончил университет, постоянно повышает квалификацию,
        посещает семинары и конференции), информация о научной деятельности (автор уникальной методики), достижениях
        ('Врач года 2011').", "4", "1"),
       ("Здесь должно быть описание врача: образование (окончил университет, постоянно повышает квалификацию,
        посещает семинары и конференции), информация о научной деятельности (автор уникальной методики), достижениях
        ('Врач года 2012').", "5", "2"),
       ("Здесь должно быть описание врача: образование (окончил университет, постоянно повышает квалификацию,
        посещает семинары и конференции), информация о научной деятельности (автор уникальной методики), достижениях
        ('Врач года 2013').", "6", "3"),
       ("Здесь должно быть описание врача: образование (окончил университет, постоянно повышает квалификацию,
        посещает семинары и конференции), информация о научной деятельности (автор уникальной методики), достижениях
        ('Врач года 2014').", "7", "4"),
       ("Здесь должно быть описание врача: образование (окончил университет, постоянно повышает квалификацию,
        посещает семинары и конференции), информация о научной деятельности (автор уникальной методики), достижениях
        ('Врач года 2015').", "8", "5");

INSERT INTO doc_working_days (doctor_id, day_id)
VALUES (1,1), (1,2), (1,3), (1,4), (1,5),
       (2,3), (2,4), (2,5), (2,6), (2,7),
       (3,1), (3,3), (3,4), (3,5), (3,7),
       (4,1), (4,2), (4,3), (4,6), (4,7),
       (5,1), (5,2), (5,3), (5,5), (5,6);

INSERT INTO visit (cause, result, visit_date, visitor_id, doctor_id, status_id)
VALUES ('Причина визита 1.', 'Диагноз и назначение 1.', 1575871200000, 1, 4, 3),
       ('Причина визита 2.', 'Диагноз и назначение 2.', 1576303200000, 1, 5, 3),
       ('Причина визита 3.', 'Диагноз и назначение 3.', 1576044000000, 1, 6, 3),
       ('Причина визита 4.', 'Диагноз и назначение 4.', 1575957600000, 1, 7, 3),
       ('Причина визита 5.', 'Диагноз и назначение 5.', 1576216800000, 1, 8, 3),
       ('Причина визита 6.', 'Диагноз и назначение 6.', 1575873000000, 2, 4, 3),
       ('Причина визита 7.', 'Диагноз и назначение 7.', 1576305000000, 2, 5, 3),
       ('Причина визита 8.', 'Диагноз и назначение 8.', 1576045800000, 2, 6, 3),
       ('Причина визита 9.', 'Диагноз и назначение 9.', 1575959400000, 2, 7, 3),
       ('Причина визита 10.', 'Диагноз и назначение 10.', 1576218600000, 2, 8, 3),
       ('Причина визита 11.', 'Диагноз и назначение 11.', 1575874800000, 3, 4, 3),
       ('Причина визита 12.', 'Диагноз и назначение 12.', 1576306800000, 3, 5, 3),
       ('Причина визита 13.', 'Диагноз и назначение 13.', 1576047600000, 3, 6, 3),
       ('Причина визита 14.', 'Диагноз и назначение 14.', 1575961200000, 3, 7, 3),
       ('Причина визита 15.', 'Диагноз и назначение 15.', 1576220400000, 3, 8, 3),

       ('Причина визита 16.', null, 1577167200000, 1, 4, 1),
       ('Причина визита 17.', null, 1577169000000, 2, 4, 1),
       ('Причина визита 18.', null, 1577170800000, 3, 4, 1),
       ('Причина визита 19.', null, 1577253600000, 1, 5, 1),
       ('Причина визита 20.', null, 1577255400000, 2, 5, 1),
       ('Причина визита 21.', null, 1577257200000, 3, 5, 1);

INSERT INTO visit_feedback (visitor_mess, doctor_mess, visit_id)
VALUES ("Большое спасибо врачу1.", "Благодарю за отзыв1.", 1),
       ("Большое спасибо врачу2.", "Благодарю за отзыв2.", 2),
       ("Большое спасибо врачу3.", "Благодарю за отзыв3.", 3),
       ("Большое спасибо врачу4.", "Благодарю за отзыв4.", 4),
       ("Большое спасибо врачу5.", "Благодарю за отзыв5.", 5),
       ("Большое спасибо врачу6.", "Благодарю за отзыв6.", 6),
       ("Большое спасибо врачу7.", "Благодарю за отзыв7.", 7),
       ("Большое спасибо врачу8.", "Благодарю за отзыв8.", 8),
       ("Большое спасибо врачу9.", "Благодарю за отзыв9.", 9),
       ("Большое спасибо врачу10.", "Благодарю за отзыв10.", 10);

# VisitorOne visitorone
# DoctorOne doctorone
# Administrator administrator