package org.prgms.customer.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.prgms.customer.Customer;
import org.prgms.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository jdbcCustomerRepository;

    private final Customer newCustomer = new Customer("user-test", "user-test@gmail.com");
    private final Customer newCustomer2 = new Customer("user-test2", "user-test2@gmail.com");

    @BeforeEach
    void insertData() {
        jdbcCustomerRepository.save(newCustomer);
        jdbcCustomerRepository.save(newCustomer2);
    }

    @Test
    @DisplayName("모두 조회 기능 테스트")
    void findAllTest() {
        var customers = jdbcCustomerRepository.findAll();

        assertThat(customers).containsExactlyInAnyOrder(newCustomer, newCustomer2);
    }

    @Test
    @DisplayName("이름으로 조회 테스트")
    void findByNameTest() {
        var sameNameCustomer = new Customer("user-test", "sameNameCustomer@gmail.com");

        jdbcCustomerRepository.save(sameNameCustomer);

        var customers = jdbcCustomerRepository.findByName("user-test");

        assertThat(customers).containsExactlyInAnyOrder(newCustomer, sameNameCustomer);
    }

    @Test
    @DisplayName("메일로 조회 테스트")
    void findByEmailTest() {
        var customer = jdbcCustomerRepository.findByEmail("user-test2@gmail.com").orElseThrow();

        assertThat(customer).isEqualTo(newCustomer2);
    }

    @Test
    @DisplayName("ID로 조회 테스트")
    void findByIdTest() {
        var customer = jdbcCustomerRepository.findById(newCustomer.id()).orElseThrow();

        assertThat(customer).isEqualTo(newCustomer);
    }

    @Test
    @DisplayName("고객 데이터 insert 테스트")
    void insertTest() {
        var customer = new Customer("new-insert", "insert@gmail.com");

        jdbcCustomerRepository.save(customer);

        var foundCustomer = jdbcCustomerRepository.findById(customer.id()).orElseThrow();

        assertThat(foundCustomer).isEqualTo(customer);
    }

    @Test
    @DisplayName("고객 정보 업데이트 테스트")
    public void updateTest() {
        newCustomer.update("update-user");

        jdbcCustomerRepository.update(newCustomer);

        var actual = jdbcCustomerRepository.findById(newCustomer.id()).orElseThrow();

        assertThat(actual.name()).isEqualTo("update-user");
    }
}