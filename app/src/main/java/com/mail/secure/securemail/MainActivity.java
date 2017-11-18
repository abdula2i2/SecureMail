package com.mail.secure.securemail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import io.realm.Realm;



public class MainActivity extends AppCompatActivity {

    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide(); //hide action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen


        Thread thread = new Thread();

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
            public void onClick(View v) {// اذا سجل دخول هنا يحفظ البيانات حقه ويحاول يسجل الدخول فيها
                realm = Realm.getDefaultInstance();

                realm.beginTransaction();// هنا يوم يضغط تحط اليوزر حقك

                User user = new User();

                user.setEmail("test2@localhost.com");
                user.setPassword("1234");
                user.setStatus("active");

                realm.copyToRealmOrUpdate(user);
                realm.commitTransaction();

                realm.close();
                GetEmails getEmails = new GetEmails(MainActivity.this,"inbox");//هنا تحاول تتصل بالاميل مع تحديد مكان الرسايل الي تبيها مثلا الانبوكس
                getEmails.execute();


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





}
