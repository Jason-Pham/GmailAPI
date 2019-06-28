package helpers.Gmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import helpers.test_data.GmailInfo;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GmailService {

    private static final String applicationName = "Gmail API";
    private static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private static final String tokenDirectoryPath = "tokens";
    private static final List<String> scope = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static String credentialsFilePath = null;
    private static Gmail service;

    public GmailService(String user_email) {
        GmailInfo gmailInfo = new GmailInfo();
        credentialsFilePath = gmailInfo.getCredentialsFilePath(user_email);
    }

    private Credential loadCredential(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        File initialFile = new File(credentialsFilePath);
        InputStream inputStream = new FileInputStream(initialFile);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(inputStream));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, jsonFactory, clientSecrets, scope)
                .setDataStoreFactory(new FileDataStoreFactory(new File(tokenDirectoryPath)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Gmail getService() throws IOException, GeneralSecurityException {
        if (service == null) {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            service = new Gmail.Builder(httpTransport, jsonFactory, loadCredential(httpTransport)).setApplicationName(applicationName).build();
        }
        return service;
    }
}