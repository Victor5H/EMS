package com.diorama.diorama;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static String MY_PREFS_NAME="EMS";
    Button user,responder,admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        admin=(Button) findViewById(R.id.mainAdmin);
        responder=(Button) findViewById(R.id.mainResponder);
        user=(Button) findViewById(R.id.mainUser);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AdminLogin.class);
                startActivity(intent);
            }
        });
        responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ResponderLogin.class);
                startActivity(intent);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),UserLogin.class);
                startActivity(intent);
            }
        });

    }
}
