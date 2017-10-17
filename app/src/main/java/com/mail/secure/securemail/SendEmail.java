package com.mail.secure.securemail;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import io.realm.Realm;

public class SendEmail extends AppCompatActivity {

        private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        realm = Realm.getDefaultInstance();
        final EditText semail = (EditText) findViewById(R.id.semail);
        final EditText ssjubect = (EditText) findViewById(R.id.ssubject);
        final EditText smessage = (EditText) findViewById(R.id.smessage);

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                realm.beginTransaction();

                Emails email = new Emails();

                email.setMessage(smessage.getText().toString());
                email.setSubject(ssjubect.getText().toString());
                email.setSender(semail.getText().toString());

                User user = realm.where(User.class).equalTo("email", "google.com").findFirst();
                user.getEmails().add(email);

                realm.commitTransaction();

                Toast.makeText(SendEmail.this, "Done", Toast.LENGTH_SHORT).show();
                realm.close();
                finish();
            }
        });
    }




}
