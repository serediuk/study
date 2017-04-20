package db.springjdbctemplate;

import db.entity.Gender;
import db.entity.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * @author Vova Iatsyk
 *         Date: 4/20/17.
 */
public class Main {

    public static final RowMapper<Person> PERSON_ROW_MAPPER = new RowMapper<Person>() {
        @Override
        public Person mapRow(ResultSet rs, int i) throws SQLException {
            return new Person(rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    Gender.parse(rs.getString("gender")));
        }
    };

    public static void main(String[] args) throws SQLException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);

        jdbcTemplate.execute("DROP TABLE IF EXISTS person;");
        jdbcTemplate.execute("CREATE TABLE person (id INT PRIMARY KEY AUTO_INCREMENT, name varchar(64), age int, phone varchar(64), email varchar(64), gender varchar(64));");

        save(new Person("name", 10, "099", "n@g.c", Gender.FEMALE), jdbcTemplate);

        System.out.println(getByName(jdbcTemplate, "name"));

        System.out.println(getAll(jdbcTemplate));

        System.out.println(jdbcTemplate.queryForList("SELECT p.name FROM person AS p;", String.class));
    }

    private static List<Person> getAll(JdbcTemplate jdbcTemplate) throws SQLException {
        return jdbcTemplate.query("SELECT * FROM person AS p;", PERSON_ROW_MAPPER);
    }

    private static Person getByName(JdbcTemplate jdbcTemplate, String name) throws SQLException {
        return jdbcTemplate.queryForObject("SELECT * FROM person WHERE name = " + name + ";", PERSON_ROW_MAPPER);
    }

    private static void save(Person person, JdbcTemplate jdbcTemplate) throws SQLException {
        String format = String.format("INSERT INTO person (name, age, phone, email, gender) " +
                        "VALUES (\"%s\", \"%d\", \"%s\", \"%s\", \"%s\")",
                person.getName(),
                new Random().nextInt(),
                person.getPhone(),
                person.getEmail(),
                person.getGender().name());
        jdbcTemplate.update(format);
    }

}
