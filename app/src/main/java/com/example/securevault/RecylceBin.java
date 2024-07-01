package com.example.securevault;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class RecylceBin extends AppCompatActivity {

    private ListView lvBin;
    private RecycleBinAdapter adapter;
    private Database db;
    private ArrayList<SavedLoginsRecord> restoreRecords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylce_bin);
        init();
    }


    private void init()
    {
        lvBin = findViewById(R.id.lvBin);
        db = new Database(this);
        db.open();
        restoreRecords = db.readRestoreRecords();
        adapter = new RecycleBinAdapter(this, restoreRecords);
        lvBin.setAdapter(adapter);
        db.close();
    }
}