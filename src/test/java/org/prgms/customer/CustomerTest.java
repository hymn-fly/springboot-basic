package org.prgms.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.prgms.io.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.groups.Tuple.tuple;

@SpringJUnitConfig(value = CustomerTest.Config.class)
class CustomerTest {

    @Configuration
    @ComponentScan(basePackages = "org.prgms.io")
    static class Config {
    }

    @Autowired
    private FileReader fileReader;

    @Test
    @DisplayName("고객 블랙리스트 조회를 통한 Customer객체 테스트")
    void customerTest() throws Exception {
        List<Customer> customers = fileReader.readFile();

        assertThat(customers)
                .extracting(Customer::name, Customer::email)
                .containsExactly(
                        tuple("홍길동", "hongil@gmail.com"),
                        tuple("박철수", "cheolsu@naver.com"),
                        tuple("김지영", "jeeyoung@yahoo.co.kr")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Null 인자, 빈 공백으로 Customer 초기화 시킬 수 없음")
    void nullTest(String arg) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer(arg, arg));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenNullOrBlankThenIllegalArgumentException(String name) {
        var customer = new Customer("name", "email@gmail.com");

        assertThatIllegalArgumentException().isThrownBy(() -> customer.update(name));
    }
}
