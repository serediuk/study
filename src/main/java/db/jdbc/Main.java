package db.jdbc;

import db.entity.Gender;
import db.entity.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Tetiana Serediuk
 *         Date: 20.04.17.
 */
public class Main {

    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Statement statement = connection.createStatement();

        statement.execute("DROP TABLE IF EXISTS person;");
        statement.execute("CREATE TABLE person (id INT PRIMARY KEY AUTO_INCREMENT, name varchar(64), age int, phone varchar(64), email varchar(64), gender varchar(64));");

        Person person = new Person("name", 10, "099", "n@g.c", Gender.FEMALE);

        save(person, statement);

        System.out.println(getByName(statement, "name"));

        System.out.println(getAll(statement));
    }

    private static List<Person> getAll(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM person;");
        List<Person> people = new ArrayList<>();
        while (rs.next()) {
            people.add(new Person(rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    Gender.parse(rs.getString("gender"))));
        }
        return people;
    }

    private static Person getByName(Statement statement, String name) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM person WHERE name = " + name + ";");
        rs.next();
        return new Person(rs.getString("name"),
                rs.getInt("age"),
                rs.getString("phone"),
                rs.getString("email"),
                Gender.parse(rs.getString("gender")));
    }

    private static void save(Person person, Statement statement) throws SQLException {
        String format = String.format("INSERT INTO person (name, age, phone, email, gender) " +
                        "VALUES (\"%s\", \"%d\", \"%s\", \"%s\", \"%s\")",
                person.getName(),
                new Random().nextInt(),
                person.getPhone(),
                person.getEmail(),
                person.getGender().name());

        statement.execute(format);
    }

}

