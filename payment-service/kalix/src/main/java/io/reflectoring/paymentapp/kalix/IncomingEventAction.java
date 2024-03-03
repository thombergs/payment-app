package io.reflectoring.paymentapp.kalix;

import io.reflectoring.paymentapp.transfer.internal.incoming.api.RequestAccountCreditEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.RequestAccountDebitEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.RequestFraudCheckEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.FraudCheckedEvent;
import kalix.javasdk.action.Action;
import kalix.javasdk.annotations.Publish;
import kalix.javasdk.annotations.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncomingEventAction extends Action {

//    private static final Logger logger = LoggerFactory.getLogger(IncomingEventAction.class);
//
//    @Subscribe.Topic(Topics.REQUEST_FRAUD_CHECK)
//    @Publish.Topic(Topics.FRAUD_CHECKED)
//    public Effect<FraudCheckedEvent> onRequestFraudCheckEvent(RequestFraudCheckEvent event) {
//        logger.info("received event {} via topic", event);
//        return effects().reply(new FraudCheckedEvent(
//                event.transferId(),
//                FraudCheckedEvent.FraudCheckResult.SUCCESS
//        ));
//    }
//
//    @Subscribe.Topic(Topics.REQUEST_ACCOUNT_DEBIT)
//    @Publish.Topic(Topics.ACCOUNT_DEBITED)
//    public Effect<AccountDebitedEvent> onRequestFraudCheckEvent(RequestAccountDebitEvent event) {
//        logger.info("received event {} via topic", event);
//        return effects().reply(new AccountDebitedEvent(
//                event.transferId(),
//                event.sourceAccountId(),
//                event.amount(),
//                AccountDebitedEvent.DebitResult.SUCCESS
//        ));
//    }
//
//    @Subscribe.Topic(Topics.REQUEST_ACCOUNT_CREDIT)
//    @Publish.Topic(Topics.ACCOUNT_CREDITED)
//    public Effect<AccountCreditedEvent> onRequestFraudCheckEvent(RequestAccountCreditEvent event) {
//        logger.info("received event {} via topic", event);
//        return effects().reply(new AccountCreditedEvent(
//                event.transferId(),
//                event.targetAccountId(),
//                event.amount(),
//                AccountCreditedEvent.CreditResult.SUCCESS
//        ));
//    }

}
