package com.example.xsudetector;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidx.activity.EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        requestNotificationPermission();

        if (isDeviceRooted(this)) {
            showRootWarningAndExit();
        } else {
            showNotRootedDialog();
        }
    }

    private void showRootWarningAndExit() {
        new AlertDialog.Builder(this)
                .setTitle("Security Warning")
                .setMessage("This device appears to be rooted. For security reasons, the app will now close.")
                .setCancelable(false)
                .setPositiveButton("Exit", (dialog, which) -> finishAffinity())
                .show();
    }

    private void showNotRootedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Device Status")
                .setMessage("This device is not rooted. You may continue using the app safely.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .show();
    }

    public boolean isDeviceRooted(Context context) {
        return checkForSuBinary() || canExecuteSuCommand() || checkForRootApps(context);
    }

    private boolean checkForSuBinary() {
        String[] paths = {
                "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"
        };

        for (String path : paths) {
            File file = new File(path + "su");
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    private boolean canExecuteSuCommand() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    private boolean checkForRootApps(Context context) {
        String[] knownRootApps = {
                "com.noshufou.android.su", "com.thirdparty.superuser", "eu.chainfire.supersu",
                "com.koushikdutta.superuser", "com.zachspong.temprootremovejb", "com.ramdroid.appquarantine",
                "com.topjohnwu.magisk"
        };

        PackageManager pm = context.getPackageManager();

        for (String packageName : knownRootApps) {
            try {
                pm.getPackageInfo(packageName, 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return false;
    }


    private static final int NOTIFICATION_PERMISSION_CODE = 1001;

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
    }


}
