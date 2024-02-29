package io.reflectoring.paymentapp.database.internal;

import io.reflectoring.paymentapp.database.api.TransferDatabase;
import io.reflectoring.paymentapp.transfer.api.Transfer;
import io.reflectoring.paymentapp.transfer.api.TransferId;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryTransferDatabase implements TransferDatabase {

    private final Map<TransferId, Transfer> database = new HashMap<>();

    @Override
    public Optional<Transfer> findById(TransferId id) {
        return Optional.of(database.get(id));
    }

    @Override
    public void update(Transfer transfer) {
        this.database.put(transfer.getId(), transfer);
    }
}
