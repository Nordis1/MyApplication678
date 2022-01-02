package com.example.myapplication678;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.myapplication678.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binder;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private static final String TAG = "MainlogTag";
    boolean x = false;
    Handler handler;
    Thread currentThread;
    AsyncTask task;
    Executor executor;
    String s = "Stanislav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());
        currentThread = Thread.currentThread();
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        synchronized (currentThread){
                            notify();
                        }
                        break;
                }
            }
        };
        binder.btnRead.setOnClickListener(this);
        binder.btnWrite.setOnClickListener(this);
        binder.btnPurchase.setOnClickListener(this);
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
                        Log.d(TAG, "onDataChange: зашли в метод чтения");
                        if (snapshot.exists()){
                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            x = true;
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


                executor.execute(()->{
                    while (!x){
                        try {
                            Log.d(TAG, "onClick: waiting 1 sec x");
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "onClick from executor: x= " + x);
                });


                Log.d(TAG, "onClick: x= " + x);

                break;
            case R.id.btnWrite:
                myRef = database.getReference("Hello");
                myRef.setValue("Hello from new project");
                break;

            case R.id.btnPurchase:
       /*         SubscriptionPurchase subscriptionPurchase = new SubscriptionPurchase();
                if (!subscriptionPurchase.isEmpty()){
                    Log.d(TAG, "onClick: Subscribtion isn't empty");
                }*/
          /*      for (Object o :subscriptionPurchase.entrySet()){

                }*/
        }

    }
}