package com.example.securevault;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SavedLogins extends AppCompatActivity {

    private ListView lvLogins;
    private FloatingActionButton fabAdd;
    private SavedLoginsAdapter adapter;
    private Database db;
    private ArrayList<SavedLoginsRecord> savedLoginsRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_logins);
        init();

        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.add_save_logins_design, null, false);

        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etUrl = view.findViewById(R.id.etUrl);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        btnAdd.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString();
            String url = etUrl.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                etUsername.setError(getString(R.string.username_required));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError(getString(R.string.password_required));
                return;
            }

            if (TextUtils.isEmpty(url)) {
                etUrl.setError(getString(R.string.url_required));
                return;
            }

            db.open();
            db.addSavedLoginInfo(username, password, url,0);
            savedLoginsRecords.add(new SavedLoginsRecord(username, password, url));
            adapter.notifyDataSetChanged();
            db.close();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void init() {
        lvLogins = findViewById(R.id.lvLogins);
        fabAdd = findViewById(R.id.fabAdd);
        db = new Database(this);
        db.open();
        savedLoginsRecords = db.readSavedLoginsRecords();
        adapter = new SavedLoginsAdapter(this, savedLoginsRecords);
        lvLogins.setAdapter(adapter);
        db.close();
    }
}
