package org.prgms.customer;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

public class Customer {
    /* 고객 아이디 */
    private UUID id = UUID.randomUUID();

    /* 고객 이름 */
    private String name;

    /* 고객 이메일 */
    private final String email;

    public Customer(String name, String email) {
        Assert.notNull(name, "이름 필수");
        Assert.isTrue(!name.isBlank(), "이름 공백 허용 안함");
        Assert.notNull(email, "이메일 필수");
        Assert.isTrue(!email.isBlank(), "이메일 공백 허용 안함");

        this.name = name;
        this.email = email;
    }

    Customer withId(UUID id) {
        this.id = id;

        return this;
    }

    public void update(String name) {
        Assert.notNull(name, "필수");
        Assert.isTrue(!name.isBlank(), "공백 허용 안함");

        this.name = name;
    }

    public UUID id() {
        return id;
    }

    public String email() {
        return email;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
