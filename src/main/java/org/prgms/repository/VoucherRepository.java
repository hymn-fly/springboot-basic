package org.prgms.repository;

import org.prgms.domain.Voucher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository {
    void save(Voucher voucher);

    List<Voucher> findAll();

    default List<Voucher> findByType(String voucherType) {
        return new ArrayList<>();
    }

    Optional<Voucher> findById(UUID voucherId);

    void deleteAll();

    default void deleteById(UUID voucherId) {
    }

}
