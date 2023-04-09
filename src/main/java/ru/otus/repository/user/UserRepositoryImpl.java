package ru.otus.repository.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.controller.dto.RegisterRequest;
import ru.otus.dto.UserDto;
import ru.otus.util.StringUtils;
import ru.otus.util.dto.UserSecurityDto;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate mysqlJdbcTemplate;

    @Override
    public Long addNewUser(RegisterRequest request) {
        String password = StringUtils.MD5(request.getPassword());
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            StringBuilder query = new StringBuilder()
                    .append("insert into ")
                    .append("social_media.users ")
                    .append("(first_name, second_name, age, biography, city, password) ")
                    .append("values (?, ?, ?, ?, ?, ?) ");

            mysqlJdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, request.getFirstName());
                ps.setString(2, request.getSecondName());
                ps.setInt(3, request.getAge());
                ps.setString(4, request.getBiography());
                ps.setString(5, request.getCity());
                ps.setString(6, password);
                return ps;
            }, keyHolder);

            return keyHolder.getKey().longValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public String findAndGetUserPaswordById(Long userId) {
        StringBuilder query = new StringBuilder()
                .append("select u.password ")
                .append("from social_media.users u ")
                .append("where id = ?");
        return mysqlJdbcTemplate.queryForList(query.toString(), String.class, userId).stream().findFirst().orElse(null);
    }

    @Override
    public UserDto findUserByIdUser(Long id) {
        StringBuilder query = new StringBuilder()
                .append("select u.first_name as firstName, u.second_name as secondName, u.age, u.biography, u.city ")
                .append("from social_media.users u ")
                .append("where id = ?");
        return mysqlJdbcTemplate.query(query.toString(), new BeanPropertyRowMapper<>(UserDto.class), id).stream().findFirst().orElse(null);
    }

    @Override
    public UserSecurityDto checkExistsUserAndGetUser(Long idUser) {
        StringBuilder query = new StringBuilder()
                .append("select u.id as userId ")
                .append("from social_media.users u ")
                .append("where id = ?");
        UserSecurityDto u = mysqlJdbcTemplate.query(query.toString(), new BeanPropertyRowMapper<>(UserSecurityDto.class), idUser).stream().findFirst().orElse(null);
        return u;
    }

    @Override
    public List<UserDto> findUsersByParams(String firstName, String secondName) {
        firstName = firstName + "%";
        secondName = secondName + "%";
        StringBuilder query = new StringBuilder()
                .append("select u.first_name as firstName, u.second_name as secondName, u.age, u.biography, u.city ")
                .append("from social_media.users u ")
                .append("where u.first_name like ? ")
                .append("and u.second_name like ? ")
                .append("order by u.id asc");
        return mysqlJdbcTemplate.query(query.toString(), new BeanPropertyRowMapper<>(UserDto.class), firstName, secondName);
    }
}
