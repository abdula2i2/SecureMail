package com.mail.secure.securemail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Registry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        getSupportActionBar().hide();  // hide top bar
        Button btrRegister = (Button) findViewById(R.id.btrRegister);
        btrRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Registry.this, "Done", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
