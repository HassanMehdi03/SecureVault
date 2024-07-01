package com.example.securevault;

import android.app.Dialog;
import android.content.Intent;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private TextInputEditText etEmail,etPassword;
    private Button btnSignin;
    private TextView tvRegister;
    private ImageView ivFingerprint;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        btnSignin.setOnClickListener(v->SingIn());
        tvRegister.setOnClickListener(v->moveToSingUp());
        ivFingerprint.setOnClickListener(v->showFingerprintDialog());

    }

    private void showFingerprintDialog()
    {
        if(sharedPreferences.getBoolean("key_touch_id",true))
        {
            fingerprintEnableDialog();
        }
        else
        {
            fingerprintNotEnableDialog();
        }
    }

    private void fingerprintEnableDialog()
    {
        View v= LayoutInflater.from(this).inflate(R.layout.fingerprint_enable_custom_dialog,null,false);

        ImageView ivDialogFingerprint;
        ivDialogFingerprint=v.findViewById(R.id.ivFingerprintColored);

        Dialog dialog=new Dialog(this);
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        BiometricVerification(dialog,ivDialogFingerprint);
    }

    private void fingerprintNotEnableDialog()
    {
        View v=LayoutInflater.from(this).inflate(R.layout.fingerprint_not_enable_custom_design,null,false);

        Button btnOk=v.findViewById(R.id.btnOk);

        Dialog dialog=new Dialog(this);
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        btnOk.setOnClickListener(v1 -> dialog.dismiss());
    }

    private void BiometricVerification(Dialog dialog,ImageView ivDialogFingerprint)
    {

        BiometricManager biometricManager=BiometricManager.from(this);

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            Toast.makeText(this, "No biometric features available on this device.", Toast.LENGTH_SHORT).show();
            return;
        }

        Executor executor= ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt=new BiometricPrompt(Login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                dialog.dismiss();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                ivDialogFingerprint.setImageResource(R.drawable.ic_tick);
                moveToHome();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                ivDialogFingerprint.setImageResource(R.drawable.ic_invalid);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivDialogFingerprint.setImageResource(R.drawable.ic_fingerprint_colored);
                    }
                },  1000);
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setNegativeButtonText("Cancel")
                .build();
        biometricPrompt.authenticate(promptInfo);

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
                editor.putString("key_name",u.getName());
                editor.putString("key_email",u.getEmail());
                editor.putString("key_mobile",u.getMobile());
                editor.apply();
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
        sharedPreferences=getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
}