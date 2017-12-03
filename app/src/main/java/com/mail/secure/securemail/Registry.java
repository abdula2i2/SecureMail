package com.mail.secure.securemail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registry extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        getSupportActionBar().hide();  // hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen

        final EditText email = (EditText)findViewById(R.id.email_edt);
        final EditText pass = (EditText)findViewById(R.id.pass_edt);
        final EditText phone = (EditText)findViewById(R.id.phone_edt);

        Button btrRegister = (Button) findViewById(R.id.btrRegister);
        btrRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getEmail = email.getText().toString();
                String getPass = pass.getText().toString();
                String getPhone = phone.getText().toString();

                if(      analyser(getPhone,"phone")// اذا رقم التلفون صحيح بيسجل البيانات
                        &&analyser(getEmail,"email")// اذا الاميل  صحيح بيسجلw البيانات
                        &&!getPass.isEmpty()               )// الباسورد مب فاضي
                {

                    userInfo userinfo = new userInfo();
                    userinfo.pushInfo(getEmail, getPass, getPhone, Registry.this);

                }else Toast.makeText(Registry.this, "check your information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  boolean analyser(String input, String type){ // تعطيه المدخلات مع نوعها رقم او اميلي بعدين يقارن

        String emailptrn = "^([a-zA-Z]{1}[0-9a-zA-Z_]{0,15})$";// هنا يتاكد بس من انه يبدا بحرف والباقي مافيه خرابيط زي * و ""
        String phoneptrn = "^(\\+966{1}[0-9]{9})$";// يتاكد من الرقم يبدا بـ699 ويكون بعدها 9 ارقام
        String exprtion =null;

        if (type.equals("email"))exprtion = emailptrn;
        else if (type.equals("phone"))exprtion = phoneptrn;
        else return false;

        Pattern checkRegex = Pattern.compile(exprtion);
        Matcher regexMatcher = checkRegex.matcher( input );

        if(regexMatcher.find())return true; // اذا طابق يقوله مزبوط


        return false; // اذا ما كان فيه ماتش يرجع خطاء
    }

}
