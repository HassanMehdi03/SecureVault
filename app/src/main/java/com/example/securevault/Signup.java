package com.example.securevault;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Signup extends AppCompatActivity {

    private TextInputEditText etName,etMobile,etEmail,etPassword,etCPassword;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();

        btnSignup.setOnClickListener(v->registration());

    }

    private void registration()
    {
        String name=etName.getText().toString().trim();
        String email=etEmail.getText().toString().trim();
        String mobile=etMobile.getText().toString().trim();
        String password=etPassword.getText().toString();
        String confirmPassword=etCPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            etName.setError(getString(R.string.enter_name));
            return;
        }

        if(TextUtils.isEmpty(email))
        {
            etEmail.setError(getString(R.string.enter_email));
            return;
        }

        if(TextUtils.isEmpty(mobile))
        {
            etMobile.setError(getString(R.string.enter_mobile));
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            etPassword.setError(getString(R.string.enter_password));
            return;
        }

        if(TextUtils.isEmpty(confirmPassword))
        {
            etCPassword.setError(getString(R.string.enter_confirm_password));
            return;
        }

        if(!password.equals(confirmPassword))
        {
            etCPassword.setError(getString(R.string.password_not_match));
            return;
        }

        Database db=new Database(this);
        db.open();
        db.addUserInfo(name,email,password,mobile);
        db.close();

        startActivity(new Intent(Signup.this,Login.class));
        finish();
    }

    private void init()
    {
        etName=findViewById(R.id.etName);
        etMobile=findViewById(R.id.etMobile);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etCPassword=findViewById(R.id.etCPassword);
        btnSignup=findViewById(R.id.btnSignup);

    }
}