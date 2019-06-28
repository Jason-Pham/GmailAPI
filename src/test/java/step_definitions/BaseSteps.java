package step_definitions;

import actions.EmailReceiverActions;
import actions.SendMailActions;

class BaseSteps {
    SendMailActions sendMailActions = new SendMailActions();
    EmailReceiverActions emailReceiverActions;
}
