package helpers.test_data;

import _base.Constant;

import java.util.HashMap;
import java.util.Map;

public class GmailInfo {
    private static final String gmailUserEmail1 = "jason.pham.qa.1@gmail.com";
    private static final String credentialsFilePath1 = Constant.ROOT_DIR
            + "/src/main/resources/gmail_api_credential/credentials1.json";
    private static final String gmailUserEmail2 = "jason.pham.qa.2@gmail.com";
    private static final String credentialsFilePath2 = Constant.ROOT_DIR
            + "/src/main/resources/gmail_api_credential/credentials2.json";
    private static final Map<String, String> mailInfo = new HashMap<>();

    public GmailInfo() {
        mailInfo.put(gmailUserEmail1, credentialsFilePath1);
        mailInfo.put(gmailUserEmail2, credentialsFilePath2);
    }

    public String getCredentialsFilePath(String email) {
        return mailInfo.get(email);
    }
}
