package com.mail.secure.securemail;

/**
 * Created by zeez on 10/25/2017.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.sun.mail.imap.IMAPFolder;
import java.io.IOException;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

import io.realm.Realm;
import io.realm.RealmResults;

public class GetEmails extends AsyncTask<Void,Void,Void> {
   // private static final String email_id = "msaproj@gmail.com";
   // private static final String password = ".1234567";
    private ProgressDialog progressDialog;
    private Context context;
    private Realm realm;
    private String folder;//هذا عشان تحدد القسم الي بتاخذ منه الرسايل
    private boolean success;// عشان اذا نجح يقفل الاكتفتي واذا فشل يمسح البيانات حق الدخول

    public GetEmails(Context context , String folder) {
        this.context = context;
        this.folder = folder;
    }
    public void setFolder  (String folder){this.folder=folder;}


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //هذا عشان يطلع للمستخدم وقت محاوله تسجيل الدخول
      progressDialog = ProgressDialog.show(context,"signing in  ","Please wait...",false,false);

    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //هنا عشان يوقف شاشة التحميل ويقرر يوديه للميل بوكس او يمسح البيانات الخطاء الي سجلها
        progressDialog.dismiss();
        if (this.success){
            ((Activity)context).finish();// عشان يقفل الاكتفتي ويروح لين الميل بوكس
        }
        else {// تمسح البيانات الخطاء الي دخلها المستخدم وتطلع له رساله
            realm = Realm.getDefaultInstance();
            RealmResults<User> result = realm.where(User.class).findAll();
            User user = result.first();

            realm.beginTransaction();
            user.deleteFromRealm();
            realm.commitTransaction();
            Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();        //Showing a success message
        }

    }

    @Override
    protected Void doInBackground(Void... params) {

        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("status", "active").findFirst();
        final String email_id = user.getEmail(); // تاخذ الاسم واليوزر وتحفظهم في مخازن فاينل للاتصال
        final String password = user.getPassword();

        //تحديد خصائص الاتصال
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap"); //تحديد البروتوكول
        properties.put("mail.imap.host", "192.168.1.87");//تحديد الاميل سيرفر
        properties.put("mail.imap.port", "143");//تحديد البورت
        //properties.setProperty( "mail.imaps.socketFactory.class", "com.mail.secure.securemail.MySSLSocketFactory" );// هذا عشان يحل مشكلة الشهادة

        try

        {   // محاولة الاتصال بالاميل سيرفر
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imap");
            store.connect(email_id, password);
            IMAPFolder folder = (IMAPFolder) store.getFolder(this.folder);//هنا تحدد القسم الي بتاخذ منه الرسايل مثلا الانبوكس
            folder.open(Folder.READ_ONLY);


            int messageCount = folder.getMessageCount(); // تطلع عدد الرسائل

            Emails email = new Emails();
            realm.beginTransaction();
            //تاخذ الاميلات وتخزنها في قاعدة البيانات
            for (int i = 1; i <= messageCount; i++) {

                Message m = folder.getMessage(i);
//                BodyPart bp = ((Multipart) m.getContent()).getBodyPart(0); هذا مع اميلات قوقل يزبط
                email.setMessage(m.getContent().toString());
                email.setSubject(m.getSubject().toString());
                email.setSender(m.getFrom()[0].toString());
                user.getInbox().add(email);

            }
            realm.copyToRealmOrUpdate(user);// بعد ما تاخذ كل الاميلات توديها في قاعدة البيانات
            realm.commitTransaction();
            realm.close();// تقفل قاعدة البيانات
            folder.close(true);// تقفل اتصالك
            store.close();
            this.success = true; // معناها نجح وراح يطلع رسالة انه دخل

        } catch (MessagingException | IOException e) {
            e.printStackTrace();this.success = false; // اذا جاء للكاتش معناها انه ما دخل فا بتطلع رسالة انه فشل في الدخول
             }

            return null;

           }

        }










