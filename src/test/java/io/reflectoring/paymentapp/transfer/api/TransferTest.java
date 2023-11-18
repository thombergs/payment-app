package io.reflectoring.paymentapp.transfer.api;

import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.OutgoingEvents;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransferTest {

    @Mock
    private OutgoingEvents outgoingEvents;

    @Test
    void startToEnd() {
        AccountId sourceAccountId = new AccountId("source");
        AccountId targetAccountId = new AccountId("target");
        Money amount = new Money(100, Currency.EUR);
        Transfer transfer = new Transfer(
                outgoingEvents,
                sourceAccountId,
                targetAccountId,
                amount,
                Locale.GERMANY
        );

        assertThat(transfer.getStatus())
                .isEqualTo(Transfer.TransferStatus.NOT_STARTED);

        transfer.startTransfer();
        verify(outgoingEvents).requestFraudCheck(any());
        assertThat(transfer.getStatus())
                .isEqualTo(Transfer.TransferStatus.WAITING_FOR_FRAUD_CHECK);

        transfer.onFraudChecked(
                new FraudCheckedEvent(
                        transfer.getId(), FraudCheckedEvent.FraudCheckResult.SUCCESS),
                false);
        assertThat(transfer.getStatus())
                .isEqualTo(Transfer.TransferStatus.FRAUD_CHECKED);

        transfer.continueTransfer();
        verify(outgoingEvents).requestAccountDebit(any());
        assertThat(transfer.getStatus())
                .isEqualTo(Transfer.TransferStatus.WAITING_FOR_DEBIT);

        transfer.onSourceAccountDebited(
                new AccountDebitedEvent(
                        transfer.getId(),
                        sourceAccountId,
                        amount,
                        AccountDebitedEvent.DebitResult.SUCCESS),
                false
        );
        assertThat(transfer.getStatus())
                .isEqualTo(Transfer.TransferStatus.SOURCE_ACCOUNT_DEBITED);

        transfer.continueTransfer();
        verify(outgoingEvents).requestAccountCredit(any());
        assertThat(transfer.getStatus())
                .isEqualTo(Transfer.TransferStatus.WAITING_FOR_CREDIT);

        transfer.onTargetAccountCredited(
                new AccountCreditedEvent(
                        transfer.getId(),
                        sourceAccountId,
                        amount,
                        AccountCreditedEvent.CreditResult.SUCCESS)
        );
        assertThat(transfer.getStatus())
                .isEqualTo(Transfer.TransferStatus.COMPLETE);
    }

}