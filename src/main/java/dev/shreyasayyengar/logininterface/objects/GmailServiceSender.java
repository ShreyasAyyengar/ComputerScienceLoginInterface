package dev.shreyasayyengar.logininterface.objects;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import dev.shreyasayyengar.logininterface.LoginProgram;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

public class GmailServiceSender {
    private static final String HOST_EMAIL = "ilovepastaalot48@gmail.com";
    private static final GsonFactory FACTORY = GsonFactory.getDefaultInstance();
    private final Gmail service;

    public GmailServiceSender(LoginUser user) {
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Gmail.Builder(HTTP_TRANSPORT, FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName("OBV Secret Santa Mailer")
                    .build();
            sendEmail(user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        System.out.println(LoginProgram.class.getResourceAsStream("/client_secret.json"));
        // Load client secrets.
        System.out.println(new InputStreamReader(LoginProgram.class.getResourceAsStream("/client_secret.json")));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(FACTORY, new InputStreamReader(LoginProgram.class.getResourceAsStream("/client_secret.json")));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, FACTORY, clientSecrets, Set.of(GmailScopes.GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private void sendEmail(LoginUser user) throws Exception {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(HOST_EMAIL));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(user.email()));

        email.setSubject("shreyasayyengar.dev Password Retrieval");
        email.setText("""
                Dear {0},

                You have requested your password for shreyasayyengar.dev. Your password is: {1}
                                
                """
                .replace("{0}", user.username())
                .replace("{1}", user.password())
        );

        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = com.google.api.client.util.Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        try {
            // Create send message
            service.users().messages().send("me", message).execute();
        } catch (GoogleJsonResponseException e) {
            // TODO (developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }
}