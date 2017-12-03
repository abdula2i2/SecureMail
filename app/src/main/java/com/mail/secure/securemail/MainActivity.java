package com.mail.secure.securemail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide(); //hide action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen


        final EditText email = (EditText)findViewById(R.id.email_edt);
        final EditText pass = (EditText)findViewById(R.id.pass_edt);
        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registry.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// اذا ضغط المستخدم على لوج ان يستدعي الدالة ويرسلها ملعومات الاميل

                String getEmail = email.getText().toString();
                String getPass = pass.getText().toString();

                getPhoneNum phoneNum  = new getPhoneNum();
                phoneNum.getNumber(getEmail,getPass,MainActivity.this);

            }
        });


    }
// عشان اذا المستخدم جديد وضغط على زر الخلف يطلعه من البرنامج
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this, Mailbox.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override//هذي عشان اذا كان فيه ملعومات في قاعدة البيانات يسجل الدخول
    protected void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();
        RealmResults<User> result= realm.where(User.class).findAll();

        if (!result.isEmpty()){
            GetEmails getEmails = new GetEmails(MainActivity.this,"inbox");//هنا تحاول تتصل بالاميل مع تحديد مكان الرسايل الي تبيها مثلا الانبوكس
             getEmails.execute();}
    }

}
