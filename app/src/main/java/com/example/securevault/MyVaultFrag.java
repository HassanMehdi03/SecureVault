package com.example.securevault;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyVaultFrag extends Fragment {

    private TextView tvLogin,tvRecycleBin;


    public MyVaultFrag() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        tvLogin.setOnClickListener(v->showLogin());
        tvRecycleBin.setOnClickListener(v->showRecycleBin());

    }

    private void showRecycleBin()
    {
        startActivity(new Intent(getActivity(),RecylceBin.class));
    }

    private void showLogin() {
        startActivity(new Intent(getActivity(), SavedLogins.class));
    }

    private void init(View view)
    {
        tvLogin=view.findViewById(R.id.tvLogin);
        tvRecycleBin=view.findViewById(R.id.tvBin);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_vault, container, false);
    }
}