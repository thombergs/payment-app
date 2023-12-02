package io.reflectoring.paymentapp.web.internal;

import io.reflectoring.paymentapp.transfer.api.StartTransferRequest;
import io.reflectoring.paymentapp.transfer.api.Transfer;
import io.reflectoring.paymentapp.transfer.api.TransferId;
import io.reflectoring.paymentapp.transfer.api.TransferNotFoundException;
import io.reflectoring.paymentapp.transfer.api.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfers/start")
    public String startTransfer(@RequestBody StartTransferRequest request) {
        // TODO: should we make this method idempotent? If yes, how?

        TransferId transferId = transferService.startNewTransfer(request);
        return transferId.uuid();
    }

    @PostMapping("/transfers/{transferId}/continue")
    public String continueTransfer(@PathVariable("transferId") String transferId) {
        Transfer transfer = transferService.findTransfer(new TransferId(transferId))
                .orElseThrow(TransferNotFoundException::new);

        transferService.continueTransfer(transfer);

        return transfer.workflowStatus().toString();
    }

    @GetMapping("/transfers/{transferId}/status")
    public String getTransferStatus(@PathVariable("transferId") String transferId) {
        Transfer transfer = transferService.findTransfer(new TransferId(transferId))
                .orElseThrow(TransferNotFoundException::new);

        return transfer.workflowStatus().toString();
    }

    @ExceptionHandler(TransferNotFoundException.class)
    ResponseEntity<String> notFound() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Transfer not found");
    }

}
