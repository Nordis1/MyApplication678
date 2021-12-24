package com.example.myapplication678;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication678.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binder;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private static final String TAG = "MainlogTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());
        binder.btnRead.setOnClickListener(this);
        binder.btnWrite.setOnClickListener(this);
        final String databaseUrl = "https://checklist-nordis-default-rtdb.europe-west1.firebasedatabase.app";
        database = FirebaseDatabase.getInstance(databaseUrl);


    }

    @Override
    public void onClick(View v) {
        myRef = database.getReference();

        switch (v.getId()){
            case R.id.btnRead:
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Log.d(TAG, "onDataChange: "+ dataSnapshot.getValue());

                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: error: " + error.getDetails());

                    }
                });

                break;
            case R.id.btnWrite:
                myRef = database.getReference("Hello");
                myRef.setValue("Hello from new project");
                break;
        }

    }
}