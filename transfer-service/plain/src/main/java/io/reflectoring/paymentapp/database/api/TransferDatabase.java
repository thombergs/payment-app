package io.reflectoring.paymentapp.database.api;

import io.reflectoring.paymentapp.transfer.api.Transfer;
import io.reflectoring.paymentapp.transfer.api.TransferId;

import java.util.Optional;

public interface TransferDatabase {

    Optional<Transfer> findById(TransferId id);

    void update(Transfer transfer);

}
