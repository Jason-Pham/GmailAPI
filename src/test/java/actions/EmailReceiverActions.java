package actions;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.common.io.BaseEncoding;
import helpers.Gmail.GmailService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static _base.Constant.DOWNLOAD_ATTACHMENT_FOLDER;
import static _base.Constant.IMAGE_FOLDER;

public class EmailReceiverActions extends BaseActions {
    private static final int TIMEOUT = 10000;
    private Gmail gmailService;

    public EmailReceiverActions(String userEmail) throws IOException, GeneralSecurityException {
        gmailService = new GmailService(userEmail).getService();
    }

    public boolean userSeeNewMail() {
        return getNewMailIds().size() > 0;
    }

    private String getEmailBody(String emailID) {
        try {
            String rawMailBody = gmailService.users().messages()
                    .get("me", emailID).setFormat("raw")
                    .execute().getRaw();
            return decodeEmail(rawMailBody);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot get Email. ERROR: " + ex.getMessage());
        }
    }

    private String getAttachments(Gmail service, String userId, String messageId)
                                                            throws IOException {
        String filename = null;
        Message message = service.users().messages().get(userId, messageId).execute();
        List<MessagePart> parts = message.getPayload().getParts();
        for (MessagePart part : parts) {
            if (part.getFilename() != null && part.getFilename().length() > 0) {
                filename = part.getFilename();
                String attId = part.getBody().getAttachmentId();
                MessagePartBody attachPart = service.users().messages().attachments().
                        get(userId, messageId, attId).execute();

                Base64 base64Url = new Base64(true);
                byte[] fileByteArray = base64Url.decodeBase64(attachPart.getData());

                File directory = new File(DOWNLOAD_ATTACHMENT_FOLDER);
                if (! directory.exists()){
                    directory.mkdir();
                }

                FileOutputStream fileOutFile =
                        new FileOutputStream(DOWNLOAD_ATTACHMENT_FOLDER + filename);
                fileOutFile.write(fileByteArray);
                fileOutFile.close();
            }
        }
        return filename;
    }

    private List<String> getNewMailIds() {
        List<String> newEmailIds = new ArrayList<>();
        try {
            List<Message> messages = gmailService.users().messages()
                    .list("me")
                    .execute().getMessages();
            for (Message m : messages) {
                if (!isNewMail(m.getId())) break;
                newEmailIds.add(m.getId());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Cannot get list email. ERROR: " + ex.getMessage());
        }
        return newEmailIds;
    }

    private boolean isNewMail(String emailId) {
        try {
            Long receiveTime = gmailService.users().messages()
                    .get("me", emailId).setMetadataHeaders(new ArrayList<>())
                    .execute().getInternalDate();
            return TIMEOUT < receiveTime;
        } catch (IOException ex) {
            throw new RuntimeException("Cannot get list email. ERROR: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public boolean userSeeTheRightMail(String receiver, String sender, String title, String content) throws IOException {
        List<String> cachedList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        do {
            List<String> newEmailIds = (List<String>) CollectionUtils.subtract(getNewMailIds(), cachedList);
            for (String emailId : newEmailIds) {
                cachedList.add(emailId);
                String emailContent = getEmailBody(emailId);

                File file1 = new File(DOWNLOAD_ATTACHMENT_FOLDER
                                    + getAttachments(gmailService, sender, emailId));
                File file2 = new File(IMAGE_FOLDER + ImageFileName);

                if (emailContent.contains("From: " + sender)
                        && emailContent.contains("To: " + receiver)
                        && emailContent.contains("Subject: " + title)
                        && emailContent.contains(content)
                        && FileUtils.contentEquals(file1, file2)) {
                    return true;
                }
            }
        } while ((System.currentTimeMillis() - startTime) < TIMEOUT);
        throw new RuntimeException(String.format("Time out waiting for email to come from user: %s with title: %s", sender, title));
    }

    private String decodeEmail(String input) {
        byte[] result = BaseEncoding.base64Url().decode(input);
        return new String(result, StandardCharsets.UTF_8);
    }
}
