package com.mail.secure.securemail;


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

    private ProgressDialog progressDialog;
    private Context context;
    private Realm realm;
    private String folder;// to set the folder will take emails from
    private boolean success;// if the code finish without Error will let user go to mailbox

    public GetEmails(Context context , String folder) {
        this.context = context;
        this.folder = folder;
    }
    public void setFolder  (String folder){this.folder=folder;}


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Display dialog for user to wait
      progressDialog = ProgressDialog.show(context,"signing in  ","Please wait...",false,false);

    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //if user have no problem to login will let him go to mailbox
        progressDialog.dismiss();
        if (this.success){
            ((Activity)context).finish();// to close the login activity and go mailbox
        }
        else {// if the user have a problems in login will delete his information form Realm and show a failed message
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
        final String email_id = user.getEmail(); // take email info from Realm database
        final String password = user.getPassword(); // take password from Realm database

        //تحديد خصائص الاتصال
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap"); //set the protocol
        properties.put("mail.imap.host", "192.168.1.87");//set the email server
        properties.put("mail.imap.port", "143");//set the port

        try

        {   // try connect to the email server
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imap");
            store.connect(email_id, password);
            IMAPFolder folder = (IMAPFolder) store.getFolder(this.folder);//set the folder we will take emails from like inbox
            folder.open(Folder.READ_ONLY);


            int messageCount = folder.getMessageCount(); // to store number of the messages

            Emails email = new Emails();
            realm.beginTransaction();
            //add emails to from messages folder to Realm Database
            for (int i = 1; i <= messageCount; i++) {

                Message m = folder.getMessage(i);
//              BodyPart bp = ((Multipart) m.getContent()).getBodyPart(0); this is for gmail
                email.setMessage(m.getContent().toString());
                email.setSubject(m.getSubject().toString());
                email.setSender(m.getFrom()[0].toString());
                user.getInbox().add(email);

            }
            realm.copyToRealmOrUpdate(user);//to save the new emails to realm
            realm.commitTransaction();
            realm.close();// close connection to realm
            folder.close(true);// close to folder connection
            store.close();

            this.success = true; // if the code got to here means it success
        } catch (MessagingException | IOException e) {
            e.printStackTrace();this.success = false; //if the code got to catch that means its fail then set to false
             }

            return null;

           }

        }










