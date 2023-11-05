package io.reflectoring.paymentapp.transfer.api;

import java.util.Optional;

public interface TransferService {

    /**
     * Retrieves a given transfer.
     */
    Optional<Transfer> findTransfer(TransferId transferId);

    /**
     * Updates the state of a transfer in the data store.
     */
    void updateTransfer(Transfer transfer);

    /**
     * Starts or continues a transfer workflow. If the transfer has been started before, the processing
     * will pick up at the state it was left in. The workflow is idempotent, so this method can
     * be called multiple times.
     */
    void processTransfer(Transfer transfer);
}
