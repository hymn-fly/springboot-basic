package org.prgms.service;

import org.prgms.domain.Voucher;
import org.prgms.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public void createVoucher(Voucher voucher) {
        voucherRepository.save(voucher);
    }

    public List<Voucher> getVouchers() {
        return voucherRepository.findAll();
    }

    public List<Voucher> getVoucherByType(String voucherType) {
        return voucherRepository.findByType(voucherType);
    }

    public List<Voucher> getVoucherByCreatedTime(String begin, String end) {
        LocalDate beginDate = LocalDate.parse(begin, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return voucherRepository.findByCreatedAt(beginDate, endDate);
    }

    public Optional<Voucher> getVoucher(UUID id) {
        return voucherRepository.findById(id);
    }

    public void deleteAllVouchers() {
        voucherRepository.deleteAll();
    }

    public int deleteVoucher(UUID voucherId) {
        return voucherRepository.deleteById(voucherId);
    }

    public long useVoucher(long beforeDiscount, UUID voucherId) {
        Optional<Voucher> voucher = voucherRepository.findById(voucherId);

        if (voucher.isEmpty())
            return beforeDiscount;

        return voucher.get().apply(beforeDiscount);
    }
}
