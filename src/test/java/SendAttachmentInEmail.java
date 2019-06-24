import com.google.api.services.gmail.Gmail;
import helpers.Gmail.GmailService;
import helpers.Utils;
import helpers.test_data.GmailInfo;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Random;

import static helpers.Gmail.GmailUtils.createEmailWithAttachment;
import static helpers.Gmail.GmailUtils.sendMessage;

public class SendAttachmentInEmail {
    public static void main(String[] args) throws Exception {
        File[] fileList = Utils.getFileList();

        MimeMessage mimeMessage = createEmailWithAttachment(GmailInfo.gmail_user_email_2,
                GmailInfo.gmail_user_email_1,
                "Test message 2",
                "Body test 2",
                        fileList[new Random().nextInt(fileList.length - 1)]);

        Gmail gmailService = new GmailService().getService();

        sendMessage(gmailService, GmailInfo.gmail_user_email_1, mimeMessage);
    }
}