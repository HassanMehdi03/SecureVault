package com.example.securevault;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationBar;
    private FragmentManager manager;
    private Fragment myVaultFrag,settingsFrag;
    private boolean homeFragFlag;// for onBackPressed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initFragment();
        backPressed();
        bottomNavigationBar.setOnItemSelectedListener(this::onItemClicked);

    }

    private void backPressed()
    {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (homeFragFlag)
                {
                    showLogoutDialog();
                }
                else
                {
                    bottomNavigationBar.setSelectedItemId(R.id.item_vault);
                    showFragment(myVaultFrag);
                }
            }
        });
    }

    private void showLogoutDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.logout_custom_dialog, null, false);
        Button btnDLogout = dialogView.findViewById(R.id.btnDLogout);
        Button btnDCancel = dialogView.findViewById(R.id.btnDCancel);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        btnDCancel.setOnClickListener(v -> dialog.dismiss());
        btnDLogout.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        });
    }

    private boolean onItemClicked(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.item_vault)
        {
            showFragment(myVaultFrag);
        } else if (itemId == R.id.item_settings) {
            showFragment(settingsFrag);
        }
        return true;
    }

    private void initFragment() {
        myVaultFrag = new MyVaultFrag();
        settingsFrag = new SettingsFrag();

        manager.beginTransaction()
                .add(R.id.FragSwitcher, myVaultFrag).hide(myVaultFrag)
                .add(R.id.FragSwitcher, settingsFrag).hide(settingsFrag)
                .commit();

        showFragment(myVaultFrag);

    }

    private void showFragment(Fragment fragment)
    {
        FragmentTransaction transaction = manager.beginTransaction();

        if(fragment.equals(myVaultFrag))
        {
            homeFragFlag=true;
        }
        else
        {
            homeFragFlag=false;
        }

        if (myVaultFrag.isAdded()) transaction.hide(myVaultFrag);
        if (settingsFrag.isAdded()) transaction.hide(settingsFrag);

        transaction.show(fragment).commit();
    }

    private void init()
    {
        bottomNavigationBar = findViewById(R.id.bottomNavigation);
        manager = getSupportFragmentManager();
        homeFragFlag = false;
    }

}