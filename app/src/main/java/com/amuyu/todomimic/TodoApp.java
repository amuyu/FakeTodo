package com.amuyu.todomimic;

import android.app.Application;
import android.support.annotation.NonNull;

import com.amuyu.logger.DefaultLogPrinter;
import com.amuyu.logger.Logger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by amuyu on 2017. 9. 21..
 */

public class TodoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogPrinter(new DefaultLogPrinter(this));
        initFirebase();
    }

    private void initFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("temp@temp.com","p@ssw0rd")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Logger.d("Firebase Signin success");
                        } else {
                            Logger.d("Firebase Signin fail");
                        }
                    }
                });
    }
}
