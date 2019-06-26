package step_definitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SendingGmailWithImageSteps extends BaseSteps {
    @Given("^(.*?) is sending an email to (.*?) from (.*?) to (.*?) with (.*?) title$")
    public void senderIsSendingEmail(String senderEmail,
                                     String receiverEmail,
                                     String senderName,
                                     String receiverName,
                                     String Title) throws GeneralSecurityException,
            MessagingException,
            IOException {

        sendMailActions.sendMailWithBikeImage(senderEmail,
                receiverEmail,
                senderName,
                receiverName,
                Title);
    }

    @And("^(.*?) retrieve the email$")
    public void receiverGetTheEmail(String receiver) {

    }

    @Then("^(.*?) received the right email$")
    public void receiverGetTheRightEmail(String receiver) {

    }
}