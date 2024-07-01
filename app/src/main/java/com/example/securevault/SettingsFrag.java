package com.example.securevault;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsFrag extends Fragment {

    private ImageView ivFacebook, ivTwitter, ivYoutube, ivInstagram;
    private TextView tvName,tvEmail,tvMobile;
    private Switch touchId;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button btnLogout;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setUserInfo();
        setSocialMediaClickListeners();
        buttonsStateCheckers();
        touchId.setOnClickListener(v->setTouchID());
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog()
    {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.logout_custom_dialog, null,false);

        Button btnDLogout = dialogView.findViewById(R.id.btnDLogout);
        Button btnDCancel = dialogView.findViewById(R.id.btnDCancel);


        // Show dialog with custom logout view
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        btnDCancel.setOnClickListener(v -> dialog.dismiss());

        btnDLogout.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(getContext(), Login.class));
            getActivity().finish();
        });

    }

    private void setTouchID()
    {
        if(touchId.isChecked())
        {
            editor.putBoolean("key_touch_id",true);
        }
        else
        {
            editor.putBoolean("key_touch_id",false);
        }
        editor.apply();
    }

    private void buttonsStateCheckers() {
        boolean isTouchIdOn = sharedPreferences.getBoolean("key_touch_id",false);
        touchId.setChecked(isTouchIdOn);
    }

    private void setUserInfo()
    {
        tvName.setText(sharedPreferences.getString("key_name",""));
        tvEmail.setText(sharedPreferences.getString("key_email",""));
        tvMobile.setText(sharedPreferences.getString("key_mobile",""));
    }

    private void setSocialMediaClickListeners() {
        try {
            ivFacebook.setOnClickListener(v -> openUrl("https://www.facebook.com/Hassan.Mehdi.03"));
            ivTwitter.setOnClickListener(v -> openUrl("https://x.com/HassanMehdi03"));
            ivYoutube.setOnClickListener(v -> openUrl("https://www.youtube.com/@Has_san03"));
            ivInstagram.setOnClickListener(v -> openUrl("https://www.instagram.com/hassan._.mehdi/"));
        } catch (Exception e) {
            Log.e("SettingsFrag", "Error setting social media click listeners: " + e.getMessage());
        }
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void init(View view)
    {
        tvName=view.findViewById(R.id.tvName);
        tvEmail=view.findViewById(R.id.tvEmail);
        tvMobile=view.findViewById(R.id.tvMobile);
        sharedPreferences=getContext().getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        touchId=view.findViewById(R.id.touchId);
        btnLogout=view.findViewById(R.id.btnLogout);
        ivFacebook = view.findViewById(R.id.ivFacebook);
        ivTwitter = view.findViewById(R.id.ivTwitter);
        ivYoutube = view.findViewById(R.id.ivYoutube);
        ivInstagram = view.findViewById(R.id.ivInstagram);
    }

    public SettingsFrag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}