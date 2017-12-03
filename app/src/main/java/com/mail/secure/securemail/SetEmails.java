package com.mail.secure.securemail;

/**
 * Created by zeez on 10/25/2017.
 */

import android.app.ProgressDialog;
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
//    private static final String email_id = "msaproj@gmail.com";
//    private static final String password = ".1234567";
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
        //اظهار رسالة على حسب اذا نجح او اذا وصل للكاتش
        if (success){
        Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();}
        else Toast.makeText(context, "Message Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        realm = Realm.getDefaultInstance();
        // تاخذ من قاعده البيانات الايوزر والباسورد وتحطهم في مخازن فاينل عشان الاتصال
        User user = realm.where(User.class).equalTo("status", "active").findFirst();
        final String email_id = user.getEmail();
        final String password = user.getPassword();

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");//كلام جرايد
            props.put("mail.smtp.starttls.enable", "true");//كلام جرايد مدري وش
            props.put("mail.smtp.ssl.trust", "192.168.1.87");//هذي ما اتوقع منها فايده الا في النت بينز
            props.put("mail.smtp.host", "192.168.1.87");//تحدد السيرفر
            props.put("mail.smtp.port", "25");// تحدد البورت

            // يحاول يسوي اتصال باليوزر والباسورد
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(email_id, password);
                        }
                    });

            try {
                // اذا قبل الاتصال تسوي رساله بالاتصال اذا مشى
                Message message = new MimeMessage(session);

                // تحدد الاميل الي مرسله منه الرسالة
                message.setFrom(new InternetAddress(email_id));

                // Set To: header field of the header. مدري كلام كويس
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));

                message.setSubject(subject);// العنوان
                message.setText(content);// المحتوى الي هو الرسالة
                Transport.send(message);// بدأ الارسال

            } catch (MessagingException e) {this.success = false; // اذا صارت مشكلة يجي هنا ويغير القيمه عشان فوق تطلع رساله انه لم يتم الارسال
                     e.printStackTrace();
            }

            return null;
        }



}










