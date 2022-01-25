package com.diorama.diorama;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.*;

import java.util.ArrayList;

import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;


public class UserView extends AppCompatActivity {

    Button back, submit;
    EditText user;
    String usn;
    Connection con;
    String us, mo, ad, se, arr[];
    ArrayAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        back = (Button) findViewById(R.id.backHome);
        submit = findViewById(R.id.sub_user);
        user = findViewById(R.id.searchtext);
        ListView userList = (ListView) findViewById(R.id.userlist);
        DbQuery query = new DbQuery(getApplicationContext());
        ArrayList<UserDetailsBo> userDetails = query.getUserDetails();
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
        CustomUserView list = new CustomUserView(getApplicationContext(), userDetails);
        userList.setAdapter(list);
        Toast.makeText(this, "Adapter set", Toast.LENGTH_SHORT).show();
        userList.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminView.class);
                startActivity(intent);
            }
        });
        /*submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                usn = user.getText().toString();
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con = DriverManager.getConnection("jdbc:mysql://" + hostName + "/" + dbName + "", dbUser, dbPassword);
                    PreparedStatement ps = con.prepareStatement("Select username,mobile,address,sex from registration where username text='"+usn+"'");
                    ResultSet rs =ps.executeQuery();
                    while(rs.next()){
                        us=rs.getString(1);
                        mo=rs.getString(2);
                        ad=rs.getString(3);
                        se=rs.getString(4);
                    }

                }
                catch (Exception e){
                    System.out.println(e);
                }
                String arr[]={us,ad,mo,se};
                adp = new ArrayAdapter<String>(getApplicationContext(),R.map_v.user_list_view,arr);
                userList.setAdapter(adp);
            }
        });
    }
    */
    }
}