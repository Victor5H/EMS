package com.diorama.diorama;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class AdminView extends AppCompatActivity {

    Button registerRes,statusRes,userview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        registerRes=(Button) findViewById(R.id.resReg);
//       userview=(Button) findViewById(R.id.resUser);
        statusRes=(Button) findViewById(R.id.resView);
        registerRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ResponderRegister.class);
                startActivity(intent);
            }
        });
       /* userview.setOnClickListener(new View.OnClickListener() { //todo:put this menu back on
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),UserView.class);
                startActivity(intent);
            }
        });*/
        statusRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ViewResponder.class);
                startActivity(intent);
            }
        });
    }
}
