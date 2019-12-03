package com.example.safeauto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextOtp;
    private String mVerificationCode;
    private Button verifyButton;
    private TextView phoneInfo;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        FirebaseApp.initializeApp(VerifyPhoneActivity.this);
        mAuth =FirebaseAuth.getInstance();
        editTextOtp=(EditText)findViewById(R.id.otpEditText);
        verifyButton=(Button)findViewById(R.id.verifyButton);
        phoneInfo=(TextView)findViewById(R.id.phoneInfo);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        Intent intent = getIntent();
        String number=intent.getStringExtra("mobile");
        Log.v("number yuva",number+number.length());
        phoneInfo.append("\n                  +91"+number);
        sendVerificationCode(number);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=editTextOtp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {

                    editTextOtp.setError("Enter code...");
                    editTextOtp.requestFocus();
                    return;
                }
                verifyVerificationCode(code);
            }
        });
    }
    private void sendVerificationCode(String mobile)
    {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+mobile,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationCode=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code =phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                editTextOtp.setText(code);
                verifyVerificationCode(code);
            }


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(VerifyPhoneActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationCode,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Intent intent = new Intent(VerifyPhoneActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    String message="something is wrong , we will\n fix it soon";
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        message="Invalid code entered";
                    }
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.parent),message,Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
            }
        });

    }

}
