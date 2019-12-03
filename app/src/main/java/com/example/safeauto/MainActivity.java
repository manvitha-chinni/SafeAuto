package com.example.safeauto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText autoNumberEditText;
    private Button qrScanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        FirebaseApp.initializeApp(MainActivity.this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAuto();
            }
        });
        qrScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qrIntent=new Intent(MainActivity.this,QRcodeActivity.class);
                startActivity(qrIntent);
            }
        });
    }
    public void gotoLoginScreen()
    {
        Intent loginIntent= new Intent(this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            gotoLoginScreen();
        }
    }

    public void initialize()
    {


        autoNumberEditText=(EditText)findViewById(R.id.autoNumberEditText);
        searchButton=(Button)findViewById(R.id.searchButton);
        qrScanButton=(Button)findViewById(R.id.scanButton);
        
    }
    public void searchAuto( )
    {

        String number=autoNumberEditText.getText().toString();
        if (number!=null) {
            Intent searchIntent = new Intent(MainActivity.this, ResultActivity.class);
            searchIntent.putExtra("auto number", number);
            startActivity(searchIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.logout:
                logout();
                return true;
            default:
                    return super.onOptionsItemSelected(item);

        }

    }
    public void logout()
    {
        FirebaseAuth.getInstance().signOut();
        gotoLoginScreen();
    }
}
