package com.mail.secure.securemail;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.realm.Realm;

public class SetEmails extends AsyncTask<Void,Void,Void> {

    private String content, to, subject;
    private Context context;
    private Realm realm;
    private boolean success;

    public SetEmails(Context context, String to, String subject, String content) {
        this.content = content;
        this.subject = subject;
        this.to = to;
        this.context = context;
        this.success = true;
    }



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //show a toast if messages sent or not
        if (success){
        Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();}
        else Toast.makeText(context, "Message Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        // take email and password from database
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("status", "active").findFirst();
        final String email_id = user.getEmail();
        final String password = user.getPassword();

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");//for auth
            props.put("mail.smtp.starttls.enable", "true");//for TLS secure connection
//            props.put("mail.smtp.ssl.trust", "192.168.1.87");//for Netbeans and SSL
            props.put("mail.smtp.host", "192.168.1.87");//set the email server
            props.put("mail.smtp.port", "25");// set the port

            // get session with email and password
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(email_id, password);
                        }
                    });

            try {
                // set a new message
                Message message = new MimeMessage(session);

                // set email from
                message.setFrom(new InternetAddress(email_id));

                // Set To: header field of the header
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));

                message.setSubject(subject);// subject
                message.setText(content);//content
                Transport.send(message);// try to send the message

            } catch (MessagingException e) {this.success = false; // if the code get to catch and fail to send change the value to false
                     e.printStackTrace();
            }

            return null;
        }



}










