package io.reflectoring.transferservice.database.api;

import io.reflectoring.transferservice.transfer.api.Transfer;
import io.reflectoring.transferservice.transfer.api.TransferId;

import java.util.Optional;

public interface TransferDatabase {

    Optional<Transfer> findById(TransferId id);

    void update(Transfer transfer);

}
