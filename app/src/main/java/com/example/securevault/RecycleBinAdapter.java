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

public class RecycleBinAdapter extends ArrayAdapter<SavedLoginsRecord> {

    public RecycleBinAdapter(@NonNull Context context, @NonNull List<SavedLoginsRecord> objects) {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_saved_login_design, parent, false);
        }

        SavedLoginsRecord restoreLoginsRecord = getItem(position);

        TextView tvUsername = v.findViewById(R.id.tvUsername);
        TextView tvPassword = v.findViewById(R.id.tvPassword);
        TextView tvUrl = v.findViewById(R.id.tvUrl);

        tvUsername.setText(restoreLoginsRecord.getUsername());
        tvPassword.setText(restoreLoginsRecord.getPassword());
        tvUrl.setText(restoreLoginsRecord.getUrl());

        v.setOnLongClickListener(v1 -> restoreRecord(restoreLoginsRecord));

        return v;
    }

    private boolean restoreRecord(SavedLoginsRecord restoreLoginsRecord) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.restore_record_design, null, false);

        Button btnRestore = view.findViewById(R.id.btnRestore);
        Button btnDelete=view.findViewById(R.id.btnDelete);

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        // for restore delete button
        int id = restoreLoginsRecord.getId();
        Database db = new Database(getContext());

        String username=restoreLoginsRecord.getUsername();
        String password=restoreLoginsRecord.getPassword();
        String url=restoreLoginsRecord.getUrl();

        btnRestore.setOnClickListener(v->{
            db.open();
            db.addSavedLoginInfo(username,password,url,1);
            db.deleteRecyclceBinRecord(id,1);
            remove(restoreLoginsRecord);
            notifyDataSetChanged();
            dialog.dismiss();
            db.close();

        });

        btnDelete.setOnClickListener(v->{
            db.open();
            db.deleteRecyclceBinRecord(id,0);
            remove(restoreLoginsRecord);
            notifyDataSetChanged();
            dialog.dismiss();
            db.close();
        });

        return false;
    }
}
