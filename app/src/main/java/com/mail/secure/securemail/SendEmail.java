package com.mail.secure.securemail;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class SendEmail extends AppCompatActivity {

    private Realm realm;
    private Emails email = new Emails();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) //for status bar -- target Api --

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        realm = Realm.getDefaultInstance();
        int l = getIntent().getIntExtra("idDraft",-1);
        final    EditText semail = (EditText) findViewById(R.id.semail);
        final    EditText ssjubect = (EditText) findViewById(R.id.ssubject);
        final    EditText smessage = (EditText) findViewById(R.id.smessage);

        // اذا جا من الدرافت ياخذ القيم حقه الاميل الدرافت ويحطها في خانات الكتابه
        if (l >= 0){

            RealmResults<User> result2= realm.where(User.class).findAll();

            RealmList<Emails> drafts = result2.first().getDrafts();
            Emails draft = drafts.get(l);

            semail.setText(draft.getSender());
            ssjubect.setText((draft.getSubject()));
            smessage.setText(draft.getMessage());


        }
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                realm.beginTransaction();

                email.setMessage(smessage.getText().toString());
                email.setSubject(ssjubect.getText().toString());
                email.setSender(semail.getText().toString());

                 User user = realm.where(User.class).equalTo("status", "active").findFirst();
                 user.getEmails().add(email);
                 SetEmails send = new SetEmails(SendEmail.this, semail.getText().toString(), ssjubect.getText().toString(), smessage.getText().toString());
                 send.execute();
                 Toast.makeText(SendEmail.this, R.string.message_sent, Toast.LENGTH_SHORT).show();
                 realm.commitTransaction();
                 realm.close();
                 finish();

            }
        });
    }

    @Override
    public void onBackPressed() //عند الضغط على زر الرجوع
    {
        int l = getIntent().getIntExtra("idDraft",-1);
        final    EditText semail = (EditText) findViewById(R.id.semail);
        final    EditText ssjubect = (EditText) findViewById(R.id.ssubject);
        final    EditText smessage = (EditText) findViewById(R.id.smessage);
        email.setMessage(smessage.getText().toString());
        email.setSubject(ssjubect.getText().toString());
        email.setSender(semail.getText().toString());

        final RealmResults<User> result2= realm.where(User.class).findAll();
        final Emails draft;
        if (l >= 0){

            RealmList<Emails> drafts = result2.first().getDrafts();
            draft = drafts.get(l);

            if (draft.getMessage().equals(email.getMessage())) finish();
            else {

                new AlertDialog.Builder(SendEmail.this) //رسالة تنبيه
                        .setTitle(getString(R.string.attention))
                        .setTitle(getString(R.string.update_message))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                realm.beginTransaction();
                                draft.setMessage(email.getMessage());
                                realm.copyToRealmOrUpdate(result2);
                                realm.commitTransaction();
                                realm.close();
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();

            }
        }

        if(l < 0){

            new AlertDialog.Builder(SendEmail.this) //رسالة تنبيه
                    .setTitle(getString(R.string.attention))
                    .setTitle(getString(R.string.save_message))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            realm.beginTransaction();

                            User user = realm.where(User.class).equalTo("status", "active").findFirst();
                            user.getDrafts().add(email);

                            realm.commitTransaction();

                            Toast.makeText(SendEmail.this,R.string.message_saved_to_drafts , Toast.LENGTH_SHORT).show();
                            realm.close();
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }


}















