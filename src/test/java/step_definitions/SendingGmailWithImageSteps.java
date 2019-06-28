package step_definitions;

import actions.EmailReceiverActions;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.testng.Assert;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SendingGmailWithImageSteps extends BaseSteps {
    private String senderEmail;
    private String receiverEmail;
    private String senderName;
    private String receiverName;
    private String Title;


    @Given("^(.*?) is sending an email to (.*?) from (.*?) to (.*?) with (.*?) title$")
    public void senderIsSendingEmail(String senderEmail,
                                     String receiverEmail,
                                     String senderName,
                                     String receiverName,
                                     String Title) throws GeneralSecurityException,
                                                            MessagingException,
                                                            IOException {

        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.Title = Title;

        sendMailActions.sendMailWithBikeImage(this.senderEmail,
                this.receiverEmail,
                this.senderName,
                this.receiverName,
                this.Title);
    }

    @And("^(.*?) retrieve the email$")
    public void receiverGetTheEmail(String receiver) throws IOException, GeneralSecurityException {
        emailReceiverActions = new EmailReceiverActions(receiver);

        Assert.assertTrue(emailReceiverActions.userSeeNewMail(), "There is no new mail");
    }

    @Then("^(.*?) received the right email$")
    public void receiverGetTheRightEmail(String receiver) throws IOException {
        Assert.assertTrue(emailReceiverActions.userSeeTheRightMail(receiver,
                                                                    this.senderEmail,
                                                                    this.Title,
                                                                    sendMailActions.createMessageBody
                                                                            (this.senderName, this.receiverName)));

    }
}