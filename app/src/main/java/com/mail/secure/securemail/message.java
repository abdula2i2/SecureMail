package com.mail.secure.securemail;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class message extends AppCompatActivity {


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  //for status bar -- target Api --
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        int i = getIntent().getIntExtra("id",0);
        String ii = getIntent().getStringExtra("from");
        Realm realm;

        TextView to = (TextView) findViewById(R.id.from);
        TextView subject = (TextView) findViewById(R.id.subject);
        TextView messageb = (TextView) findViewById(R.id.messageb);

        realm = Realm.getDefaultInstance();


        RealmResults<User> result= realm.where(User.class).findAll();

        RealmList<Emails> emails;

        if(ii.equals("inbox")) {// if user come from inbox will take the email form inbox
            emails = result.first().getInbox();
        }else emails = result.first().getEmails(); //if he come from sent

        Emails email = emails.get(i);
            to.setText(email.getSender());
            subject.setText(email.getSubject());
            messageb.setText(email.getMessage());

        }


    }

