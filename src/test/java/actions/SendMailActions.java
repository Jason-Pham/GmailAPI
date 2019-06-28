package actions;

import com.google.api.services.gmail.Gmail;
import helpers.Gmail.GmailService;
import helpers.Utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Random;

import static _base.Constant.COMMA_AND_SPACE;
import static _base.Constant.NEXT_LINE;
import static helpers.Gmail.GmailUtils.createEmailWithAttachment;
import static helpers.Gmail.GmailUtils.sendMessage;

public class SendMailActions extends BaseActions {

    public void sendMailWithBikeImage(String senderEmail,
                         String receiverEmail,
                         String senderName,
                         String receiverName,
                         String Title) throws IOException, MessagingException, GeneralSecurityException {
        MimeMessage mimeMessage = prepareMessage(senderEmail,
                                                    receiverEmail,
                                                    senderName,
                                                    receiverName,
                                                    Title);
        Gmail gmailService = prepareGmailService(senderEmail);
        sendMessage(gmailService, senderEmail, mimeMessage);
    }

    private File getRandomImage() {
        File[] fileList = Utils.getFileList();
        File chosenFile = fileList[new Random().nextInt(fileList.length - 1)];
        ImageFileName = chosenFile.getName();

        return chosenFile;
    }

    private MimeMessage prepareMessage(String senderEmail,
                                       String receiverEmail,
                                       String senderName,
                                       String receiverName,
                                       String Title) throws MessagingException {

        return createEmailWithAttachment(receiverEmail,
                senderEmail,
                Title,
                createMessageBody(senderName, receiverName),
                getRandomImage());
    }

    public static String createMessageBody(String senderName,
                                           String receiverName){
        return Greetings + COMMA_AND_SPACE + receiverName
                + NEXT_LINE
                + NEXT_LINE
                + MyBikeImageIsAttached
                + NEXT_LINE
                + NEXT_LINE
                + Cheers + COMMA_AND_SPACE
                + NEXT_LINE
                + senderName
                ;
    }

    private Gmail prepareGmailService(String email) throws IOException, GeneralSecurityException {
        return new GmailService(email).getService();
    }
}
