package actions;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import helpers.Gmail.GmailService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static _base.Constant.DOWNLOAD_ATTACHMENT_FOLDER;
import static _base.Constant.IMAGE_FOLDER;
import static helpers.Gmail.GmailUtils.decodeEmail;
import static helpers.Gmail.GmailUtils.getAttachments;

public class EmailReceiverActions extends BaseActions {
    private static final int TIMEOUT = 10000;
    private Gmail gmailService;

    public EmailReceiverActions(String userEmail) throws IOException, GeneralSecurityException {
        gmailService = new GmailService(userEmail).getService();
    }

    public boolean userSeeNewMail() {
        return getNewMailIds().size() > 0;
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
}
