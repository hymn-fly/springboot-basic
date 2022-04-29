package org.prgms.controller;

import org.prgms.controller.dto.CreateVoucherRequest;
import org.prgms.domain.Voucher;
import org.prgms.service.VoucherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Controller
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/vouchers")
    public String vouchers(Model model) {
        model.addAttribute("vouchers", voucherService.getVouchers());

        return "vouchers";
    }

    @GetMapping("/vouchers/{voucherId}")
    public String vouchers(@PathVariable UUID voucherId, Model model, HttpServletResponse httpServletResponse) {
        Optional<Voucher> voucherOptional = voucherService.getVoucher(voucherId);

        if (voucherOptional.isEmpty()) {
            return "redirect:/vouchers";
        }

        model.addAttribute("voucher", voucherOptional.get());
        return "voucher-detail";
    }

    @PostMapping("/new-voucher")
    public String createNewVoucher(CreateVoucherRequest createVoucherRequest) {
        Voucher voucher = createVoucherRequest.toVoucher();

        voucherService.createVoucher(voucher);

        return "redirect:/vouchers";
    }

    @GetMapping("/new-voucher")
    public String newVoucher() {
        return "new-voucher";
    }

    // HTML에서 Delete Method 호출이 안되어.. 후에 react로 변경 시 @DeleteMapping 사용 해볼 수 있을듯합니다.
    @GetMapping("/vouchers/delete/{voucherId}")
    public String deleteVoucher(@PathVariable UUID voucherId) {
        voucherService.deleteVoucher(voucherId);

        return "redirect:/vouchers";
    }

}
