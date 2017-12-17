package com.mail.secure.securemail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

                if(      analyser(getPhone,"phone")// check phone number format
                        &&analyser(getEmail,"email")// check email format
                        &&!getPass.isEmpty()               )// not empty password
                {

                    userInfo userinfo = new userInfo();
                    userinfo.pushInfo(getEmail, getPass, getPhone, Registry.this);

                }else Toast.makeText(Registry.this, "check your information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  boolean analyser(String input, String type){ // check if the format correct

        String emailptrn = "^([a-zA-Z]{1}[0-9a-zA-Z_]{0,15})$";// to make sure email start with letter and have no ;./@!$%
        String phoneptrn = "^(\\+966{1}[0-9]{9})$";// check phone number format start with +966 and have 9 numbers
        String exprtion =null;

        if (type.equals("email"))exprtion = emailptrn;
        else if (type.equals("phone"))exprtion = phoneptrn;
        else return false;

        Pattern checkRegex = Pattern.compile(exprtion);
        Matcher regexMatcher = checkRegex.matcher( input );

        if(regexMatcher.find())return true; // return true


        return false;
    }

}
