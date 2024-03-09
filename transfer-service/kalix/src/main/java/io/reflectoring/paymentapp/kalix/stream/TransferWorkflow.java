package io.reflectoring.paymentapp.kalix.stream;

import io.reflectoring.paymentapp.kalix.Message;
import io.reflectoring.paymentapp.transfer.api.AccountId;
import io.reflectoring.paymentapp.transfer.api.Currency;
import io.reflectoring.paymentapp.transfer.api.Money;
import io.reflectoring.paymentapp.transfer.api.SimpleTransferState;
import io.reflectoring.paymentapp.transfer.api.StartTransferRequest;
import io.reflectoring.paymentapp.transfer.api.TransferId;
import io.reflectoring.paymentapp.transfer.api.TransferState;
import io.reflectoring.paymentapp.transfer.api.WorkflowStatus;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import kalix.javasdk.annotations.Id;
import kalix.javasdk.annotations.TypeId;
import kalix.javasdk.client.ComponentClient;
import kalix.javasdk.workflow.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

/**
 * This class is removed by the build process when run with the profile "-P topic".
 */
@TypeId("transfer")
@Id("id")
@RequestMapping("/transfer/{id}")
public class TransferWorkflow extends Workflow<TransferState> {

    private static final Logger logger = LoggerFactory.getLogger(TransferWorkflow.class);

    private final ComponentClient componentClient;

    public TransferWorkflow(
            ComponentClient componentClient
    ) {
        this.componentClient = componentClient;
    }

    @PostMapping("/start")
    public Effect<Message> startTransfer(
            @PathVariable("id") String id,
            @RequestBody StartTransferRequest request
    ) {
        if (currentState() != null) {
            // TODO: This check only works because the client has to provide the
            // transfer id even when starting the transfer. Does this mean
            // that the client MUST control the transfer ID?
            // Is there a way for the server to control the transfer ID?
            return effects().error("Transfer already started!");
        }

        TransferState initialState = new SimpleTransferState(
                new TransferId(id),
                WorkflowStatus.NOT_STARTED,
                null,
                new AccountId(request.sourceAccountId()),
                new AccountId(request.targetAccountId()),
                new Money(request.amount(), Currency.valueOf(request.currency())),
                Locale.forLanguageTag(request.transactionLocation())
        );

        return effects()
                .updateState(initialState)
                .transitionTo("fraudCheck", initialState)
                .thenReply(new Message("Transfer started"));
    }

    @GetMapping("/status")
    public Effect<Message> status(@PathVariable("id") String id){
        if (currentState() == null) {
            return effects().error("Transfer not found!");
        }

        return effects().reply(new Message(currentState().workflowStatus().toString()));
    }

    @PostMapping("/on-fraud-checked")
    public Effect<Message> onFraudChecked(@RequestBody FraudCheckedEvent event) {
        logger.info("received event {}", event);
        if (currentState() == null) {
            return effects().error("transfer not started");
        }

        if (currentState().workflowStatus() != WorkflowStatus.WAITING_FOR_FRAUD_CHECK) {
            return effects().error("transfer is not in the right state");
        }

        return effects()
                .transitionTo("debitSourceAccount", currentState())
                .thenReply(new Message("transfer progressed "));
    }

    @PostMapping("/on-source-account-debited")
    public Effect<Message> onSourceAccountDebited(@RequestBody AccountDebitedEvent event) {
        if (currentState() == null) {
            return effects().error("transfer not started");
        }

        if (currentState().workflowStatus() != WorkflowStatus.WAITING_FOR_DEBIT) {
            return effects().error("transfer is not in the right state");
        }

        return effects()
                .transitionTo("creditTargetAccount", currentState())
                .thenReply(new Message("transfer progressed "));
    }

    @PostMapping("/on-target-account-credited")
    public Effect<Message> onTargetAccountCredited(@RequestBody AccountCreditedEvent event) {
        if (currentState() == null) {
            return effects().error("transfer not started");
        }

        if (currentState().workflowStatus() != WorkflowStatus.WAITING_FOR_CREDIT) {
            return effects().error("transfer is not in the right state");
        }

        return effects()
                .updateState(SimpleTransferState
                        .fromTransferState(currentState())
                        .withStatus(WorkflowStatus.COMPLETE))
                .end()
                .thenReply(new Message("transfer finished"));
    }

    @Override
    public WorkflowDef<TransferState> definition() {

        Step fraudCheck = step("fraudCheck")
                .call(TransferState.class, transferState -> {
                    logger.info("executing step 'fraudCheck'");
                    return componentClient.forAction()
                            .call(OutgoingEventsToStreamAction::requestFraudCheck)
                            .params(new RequestFraudCheckEvent(
                                    transferState.id(),
                                    transferState.sourceAccountId(),
                                    transferState.transactionLocation()
                            ));
                })
                .andThen(RequestFraudCheckEvent.class, __ ->
                        effects()
                                .updateState(SimpleTransferState
                                        .fromTransferState(currentState())
                                        .withStatus(WorkflowStatus.WAITING_FOR_FRAUD_CHECK))
                                .pause());

        Step debitAccount = step("debitSourceAccount")
                .call(TransferState.class, transferState ->
                        componentClient.forAction()
                                .call(OutgoingEventsToStreamAction::requestAccountDebit)
                                .params(new RequestAccountDebitEvent(
                                        transferState.id(),
                                        transferState.sourceAccountId(),
                                        transferState.amount()
                                )))
                .andThen(RequestAccountDebitEvent.class, __ ->
                        effects()
                                .updateState(SimpleTransferState
                                        .fromTransferState(currentState())
                                        .withStatus(WorkflowStatus.WAITING_FOR_DEBIT))
                                .pause());

        Step creditAccount = step("creditTargetAccount")
                .call(TransferState.class, transferState ->
                        componentClient.forAction()
                                .call(OutgoingEventsToStreamAction::requestAccountCredit)
                                .params(new RequestAccountCreditEvent(
                                        transferState.id(),
                                        transferState.targetAccountId(),
                                        transferState.amount()
                                )))
                .andThen(RequestAccountCreditEvent.class, __ ->
                        effects()
                                .updateState(SimpleTransferState
                                        .fromTransferState(currentState())
                                        .withStatus(WorkflowStatus.WAITING_FOR_CREDIT))
                                .pause());

        return workflow()
                .addStep(fraudCheck)
                .addStep(debitAccount)
                .addStep(creditAccount);
    }


}
