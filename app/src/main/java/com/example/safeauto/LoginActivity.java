package com.example.safeauto;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ImageView loginButton=(ImageView)findViewById(R.id.loginButton);
        TextView  appName= (TextView)findViewById(R.id.appName);
        final EditText mobileNumberEditText=(EditText)findViewById(R.id.mobileNumberEditText);
        mobileNumberEditText.setSelection(5);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/safeauto.ttf");
        appName.setTypeface(type);

        mobileNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number;
                number=(s.toString()).replace(" +91 ","");
                if (isValidNumber(number)){
                    loginButton.setVisibility(View.VISIBLE);
                } else {
                    loginButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(" +91 ")) {
                    String temp=mobileNumberEditText.getText().toString();

                    mobileNumberEditText.setText(" +91 ");
                    Selection.setSelection(mobileNumberEditText.getText(), mobileNumberEditText.getText().length());
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String number=mobileNumberEditText.getText().toString();
                number=number.replace(" +91 ","");
                Intent verifyActivityIntent= new Intent(LoginActivity.this,VerifyPhoneActivity.class);
                verifyActivityIntent.putExtra("mobile",number);
                startActivity(verifyActivityIntent);
            }
        });
    }
    public  boolean isValidNumber(String mobileNumber)
    {
        return Pattern.matches("\\d{10}",mobileNumber);
    }

}