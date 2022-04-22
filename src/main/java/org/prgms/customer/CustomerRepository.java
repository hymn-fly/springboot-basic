package org.prgms.customer;

import org.prgms.utils.UuidUtils;
import org.prgms.validator.RepositoryValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepository {
    private final static String SELECT_ALL_QUERY = "SELECT * FROM customers;";
    private final static String SELECT_BY_NAME_QUERY = "SELECT * FROM customers WHERE name = ?;";
    private final static String SELECT_BY_ID = "SELECT * FROM customers WHERE customer_id = ?;";
    private final static String SELECT_BY_EMAIL = "SELECT * FROM customers WHERE email = ?;";

    private final static String INSERT_QUERY = "INSERT INTO customers(customer_id, name, email) values (?, ?, ?)";
    private final static String UPDATE_NAME_BY_ID_QUERY = "UPDATE customers SET name = ? WHERE customer_id = ?;";
    private final static String DELETE_QUERY = "DELETE FROM customers;";
    private final static String DELETE_QUERY_BY_ID = "DELETE FROM customers WHERE customer_id = ?;";

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> findAll() {
        return jdbcTemplate.query(SELECT_ALL_QUERY, this::mapToCustomer);
    }

    public List<Customer> findByName(String name) {
        return jdbcTemplate.query(SELECT_BY_NAME_QUERY, this::mapToCustomer, name);
    }

    public Optional<Customer> findByEmail(String email) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_EMAIL, this::mapToCustomer, email));
    }

    public Optional<Customer> findById(UUID id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, this::mapToCustomer, UuidUtils.uuidToBytes(id)));
    }

    public int save(Customer customer) {
        var update = jdbcTemplate.update(INSERT_QUERY, UuidUtils.uuidToBytes(customer.id()), customer.name(), customer.email());
        RepositoryValidator.affectedRowMustBeOne(update);
        return update;
    }

    public int deleteAll() {
        return jdbcTemplate.update(DELETE_QUERY);
    }

    public void deleteById(UUID id) {
        jdbcTemplate.update(DELETE_QUERY_BY_ID, UuidUtils.uuidToBytes(id));
    }

    public void update(Customer customer) {
        var update = jdbcTemplate.update(
                UPDATE_NAME_BY_ID_QUERY, customer.name(), UuidUtils.uuidToBytes(customer.id()));

        if (update != 1) {
            throw new DataIntegrityViolationException(MessageFormat.format("데이터 업데이트 실패, 유효 row 갯수 {0}", update));
        }
    }

    private Customer mapToCustomer(ResultSet resultSet, int rowNum) {
        try {
            var id = UuidUtils.bytesToUUID(resultSet.getBytes("customer_id"));
            var name = resultSet.getString("name");
            var email = resultSet.getString("email");

            return new Customer(name, email).withId(id);
        } catch (SQLException e) {
            throw new DataRetrievalFailureException(MessageFormat.format("데이터를 가져오는 데 실패했습니다. {0}", e.getMessage()));
        }
    }
}
