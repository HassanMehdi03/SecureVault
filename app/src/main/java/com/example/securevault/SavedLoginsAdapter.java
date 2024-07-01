package com.example.securevault;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class SavedLoginsAdapter extends ArrayAdapter<SavedLoginsRecord> {

    public SavedLoginsAdapter(@NonNull Context context, @NonNull List<SavedLoginsRecord> objects) {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_saved_login_design, parent, false);
        }

        SavedLoginsRecord savedLoginsRecord = getItem(position);

        TextView tvUsername = v.findViewById(R.id.tvUsername);
        TextView tvPassword = v.findViewById(R.id.tvPassword);
        TextView tvUrl = v.findViewById(R.id.tvUrl);

        tvUsername.setText(savedLoginsRecord.getUsername());
        tvPassword.setText(savedLoginsRecord.getPassword());
        tvUrl.setText(savedLoginsRecord.getUrl());

        v.setOnLongClickListener(v1 -> updateDeleteRecord(savedLoginsRecord));

        return v;
    }

    private boolean updateDeleteRecord(SavedLoginsRecord savedLoginsRecord) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.update_delete_save_logins_design, null, false);

        Button btnDelete = view.findViewById(R.id.btnDelete);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etUrl = view.findViewById(R.id.etUrl);

        etUsername.setText(savedLoginsRecord.getUsername());
        etPassword.setText(savedLoginsRecord.getPassword());
        etUrl.setText(savedLoginsRecord.getUrl());

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        // for update delete button
        int id = savedLoginsRecord.getId();
        Database db = new Database(getContext());

        btnUpdate.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString();
            String url = etUrl.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                etUsername.setError(getContext().getString(R.string.username_required));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError(getContext().getString(R.string.password_required));
                return;
            }

            if (TextUtils.isEmpty(url)) {
                etUrl.setError(getContext().getString(R.string.url_required));
                return;
            }

            db.open();
            boolean updated = db.updateSavedLoginInfo(id, username, password, url);

            if (updated) {
                savedLoginsRecord.setUsername(username);
                savedLoginsRecord.setPassword(password);
                savedLoginsRecord.setUrl(url);
                notifyDataSetChanged();
            }

            db.close();
            dialog.dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            db.open();
            boolean deleted = db.deleteSavedLoginInfo(id);
            if (deleted) {
                db.addRestoreRecords(savedLoginsRecord.getUsername(), savedLoginsRecord.getPassword(), savedLoginsRecord.getUrl());
                remove(savedLoginsRecord);
                notifyDataSetChanged();
            }
            db.close();
            dialog.dismiss();
        });

        return false;
    }
}
