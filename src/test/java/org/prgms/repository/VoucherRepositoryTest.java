package org.prgms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgms.domain.FixedAmountVoucher;
import org.prgms.domain.PercentDiscountVoucher;
import org.prgms.domain.Voucher;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class VoucherRepositoryTest extends RepositoryTest {
    @Autowired
    private VoucherRepository voucherRepository;

    private final Voucher voucher = new FixedAmountVoucher(UUID.randomUUID(), 10L);
    private final Voucher voucher2 = new PercentDiscountVoucher(UUID.randomUUID(), 10L);

    @BeforeEach
    void setup() {
        voucherRepository.save(voucher);
        voucherRepository.save(voucher2);
    }

    @Test
    @DisplayName("바우처 저장 테스트")
    void saveTest() {
        final Voucher newVoucher = new PercentDiscountVoucher(UUID.randomUUID(), 10L);
        voucherRepository.save(newVoucher);

        Optional<Voucher> maybeVoucher = voucherRepository.findById(newVoucher.getVoucherId());

        assertThat(maybeVoucher.isPresent()).isTrue();
        assertThat(maybeVoucher.get()).isEqualTo(newVoucher);
    }

    @Test
    @DisplayName("바우처 조회 테스트")
    void findAllTest() {
        List<Voucher> vouchers = voucherRepository.findAll();

        assertThat(vouchers).containsExactlyInAnyOrder(voucher, voucher2);
    }

    @Test
    @DisplayName("타입별 바우처 조회 테스트")
    void findByTypeTest() {
        List<Voucher> fixedVouchers = voucherRepository.findByType("FIXED");
        List<Voucher> percentVouchers = voucherRepository.findByType("PERCENT");

        assertThat(fixedVouchers).containsExactly(voucher);
        assertThat(percentVouchers).containsExactly(voucher2);
    }


    @Test
    @DisplayName("생성일자 기간으로 조회 테스트")
    void findByCreatedAtTest() {

        List<Voucher> noVouchers = voucherRepository.findByCreatedAt(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));
        List<Voucher> existVouchers = voucherRepository.findByCreatedAt(LocalDate.now().minusDays(1), LocalDate.now());

        assertThat(noVouchers).hasSize(0);
        assertThat(existVouchers).containsExactlyInAnyOrder(voucher, voucher2);
    }

    @Test
    @DisplayName("바우처 id로 조회 테스트")
    void findByIdTest() {
        Optional<Voucher> unknown = voucherRepository.findById(UUID.randomUUID());
        Optional<Voucher> foundVoucher = voucherRepository.findById(voucher.getVoucherId());

        assertThat(unknown.isEmpty()).isTrue();
        assertThat(foundVoucher.orElseThrow()).isEqualTo(voucher);
    }

    @Test
    @DisplayName("바우처 id로 삭제 테스트")
    void deleteByIdTest() {
        int deleteRow = voucherRepository.deleteById(voucher.getVoucherId());
        Optional<Voucher> maybeVoucher = voucherRepository.findById(voucher.getVoucherId());

        assertThat(maybeVoucher.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("모두 삭제 테스트")
    void deleteAllTest() {
        voucherRepository.deleteAll();
        List<Voucher> all = voucherRepository.findAll();

        assertThat(all).hasSize(0);
    }


}