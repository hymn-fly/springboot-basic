package org.prgms;

import org.prgms.io.InOut;
import org.prgms.voucher.FixedAmountVoucher;
import org.prgms.voucher.PercentDiscountVoucher;
import org.prgms.voucher.service.VoucherService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.InputMismatchException;
import java.util.UUID;

@Profile({"local", "dev", "prod"})
@Component
public class CommandLineApplication {
    private final VoucherService service;
    private final InOut console;

    public CommandLineApplication(VoucherService service, InOut console) {
        this.service = service;
        this.console = console;
    }

    @PostConstruct
    public void execute() {
        while (true) {
            console.optionMessage();
            String inputText = console.input();
            try {
                switch (inputText) {
                    case "exit":
                        return;
                    case "create":
                        var opt = console.chooseVoucher();
                        switch (opt) {
                            case 1 -> service.createVoucher(new FixedAmountVoucher(UUID.randomUUID(), 10L));
                            case 2 -> service.createVoucher(new PercentDiscountVoucher(UUID.randomUUID(), 10L));
                            default -> throw new IllegalArgumentException(String.valueOf(opt));
                        }
                        break;
                    case "list":
                        service.listVoucher();
                        break;
                    default:
                        throw new IllegalArgumentException(inputText);
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                console.inputError(e.getMessage());
            }
        }
    }
}
