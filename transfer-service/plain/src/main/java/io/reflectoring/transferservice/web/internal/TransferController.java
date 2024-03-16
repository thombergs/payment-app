package io.reflectoring.transferservice.web.internal;

import io.reflectoring.transferservice.transfer.api.StartTransferRequest;
import io.reflectoring.transferservice.transfer.api.Transfer;
import io.reflectoring.transferservice.transfer.api.TransferId;
import io.reflectoring.transferservice.transfer.api.TransferNotFoundException;
import io.reflectoring.transferservice.transfer.api.TransferService;
import io.reflectoring.transferservice.transfer.api.UnexpectedTransferStateException;
import io.reflectoring.transferservice.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.transferservice.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.transferservice.transfer.internal.incoming.api.FraudCheckedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer/{id}")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startTransfer(
            @PathVariable("id") String transferId,
            @RequestBody StartTransferRequest request) {
        // TODO: should we make this method idempotent? If yes, how?

        transferService.startNewTransfer(new TransferId(transferId), request);
        return ResponseEntity.ok("transfer started");
    }

    @PostMapping("/on-fraud-checked")
    public ResponseEntity<String> onFraudChecked(
            @PathVariable("id") String transferId,
            @RequestBody FraudCheckedEvent event) {

        Transfer transfer = transferService.findTransfer(new TransferId(transferId))
                .orElseThrow(TransferNotFoundException::new);

        if (transfer.getStatus() != Transfer.TransferStatus.WAITING_FOR_FRAUD_CHECK) {
            throw new UnexpectedTransferStateException();
        }

        transferService.onFraudChecked(event);
        return ResponseEntity.ok("transfer progressed");
    }

    @PostMapping("/on-source-account-debited")
    public ResponseEntity<String> onSourceAccountDebited(
            @PathVariable("id") String transferId,
            @RequestBody AccountDebitedEvent event) {
        Transfer transfer = transferService.findTransfer(new TransferId(transferId))
                .orElseThrow(TransferNotFoundException::new);

        if (transfer.getStatus() != Transfer.TransferStatus.WAITING_FOR_DEBIT) {
            throw new UnexpectedTransferStateException();
        }

        transferService.onSourceAccountDebited(event);
        return ResponseEntity.ok("transfer progressed");
    }

    @PostMapping("/on-target-account-credited")
    public ResponseEntity<String> onTargetAccountCredited(
            @PathVariable("id") String transferId,
            @RequestBody AccountCreditedEvent event) {
        Transfer transfer = transferService.findTransfer(new TransferId(transferId))
                .orElseThrow(TransferNotFoundException::new);

        if (transfer.getStatus() != Transfer.TransferStatus.WAITING_FOR_CREDIT) {
            throw new UnexpectedTransferStateException();
        }

        transferService.onTargetAccountCredited(event);
        return ResponseEntity.ok("transfer progressed");
    }

    @GetMapping("/status")
    public String getTransferStatus(@PathVariable("id") String transferId) {
        Transfer transfer = transferService.findTransfer(new TransferId(transferId))
                .orElseThrow(TransferNotFoundException::new);

        return transfer.getStatus().toString();
    }

    @ExceptionHandler(TransferNotFoundException.class)
    ResponseEntity<String> notFound() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Transfer not found");
    }

    @ExceptionHandler(UnexpectedTransferStateException.class)
    ResponseEntity<String> unexpectedState() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Transfer is not in the right state.");
    }

}
