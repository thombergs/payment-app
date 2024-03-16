package io.reflectoring.transferservice.database.internal;

import io.reflectoring.transferservice.database.api.TransferDatabase;
import io.reflectoring.transferservice.transfer.api.Transfer;
import io.reflectoring.transferservice.transfer.api.TransferId;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryTransferDatabase implements TransferDatabase {

    private final Map<TransferId, Transfer> database = new HashMap<>();

    @Override
    public Optional<Transfer> findById(TransferId id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public void update(Transfer transfer) {
        this.database.put(transfer.getId(), transfer);
    }
}
