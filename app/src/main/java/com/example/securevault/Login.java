package com.example.securevault;

import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class Login extends AppCompatActivity {

    TextInputEditText etEmail,etPassword;
    Button btnSignin;
    TextView tvRegister;
    ImageView ivFingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        btnSignin.setOnClickListener(v->SingIn());
        tvRegister.setOnClickListener(v->moveToSingUp());
        ivFingerprint.setOnClickListener(v->bioMetricAuthentication());

    }

    private void bioMetricAuthentication()
    {
//        BiometricManager biometricManager=BiometricManager.from(this);
//
//        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
//            Toast.makeText(this, "No biometric features available on this device.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Executor executor= ContextCompat.getMainExecutor(this);
//
//        BiometricPrompt biometricPrompt=new BiometricPrompt(Login.this, executor, new BiometricPrompt.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
//                super.onAuthenticationError(errorCode, errString);
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
//                super.onAuthenticationSucceeded(result);
//                moveToHome();
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                super.onAuthenticationFailed();
//                Toast.makeText(Login.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
//                .setTitle("Biometric Authentication")
//                .setNegativeButtonText("Cancel")
//                .build();
//        biometricPrompt.authenticate(promptInfo);

    }

    private void moveToSingUp() {

        startActivity(new Intent(Login.this,Signup.class));
    }
    private void moveToHome() {
        startActivity(new Intent(Login.this,MainActivity.class));
        finish();
    }


    private void SingIn()
    {

        String email=etEmail.getText().toString().trim();
        String password=etPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            etEmail.setError(getString(R.string.enter_email));
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            etPassword.setError(getString(R.string.enter_password));
            return;
        }

        Database db=new Database(this);
        db.open();
        ArrayList<UserInfo> userInfos=db.readUserInfos();
        db.close();

        boolean flag=false;

        for(UserInfo u:userInfos)
        {
            if(u.getEmail().equals(email) && u.getPassword().equals(password))
            {
                Toast.makeText(this, R.string.login_successful, Toast.LENGTH_SHORT).show();
                moveToHome();
                flag=true;
                break;
            }
        }

        if(!flag)
        {
            Toast.makeText(this, "wrong email or password", Toast.LENGTH_SHORT).show();
        }

    }

    private void init()
    {
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnSignin=findViewById(R.id.btnSignin);
        tvRegister=findViewById(R.id.tvRegister);
        ivFingerprint=findViewById(R.id.ivFingerprint);
    }
}