package com.robin.tesing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.robin.tesing.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private EditText editText6, editText7, editText8, editText9, editText10;
    private Button button;
    private TextView textView;
    private String scheme, host, path, key, value;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        initData();
        button.setOnClickListener(v -> setData());
    }

    private void setData() {
        scheme = editText6.getText().toString().trim();
        host = editText7.getText().toString().trim();
        path = editText8.getText().toString().trim();
        key = editText9.getText().toString().trim();
        value = editText10.getText().toString().trim();
        if (!TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(host)) {
            url = scheme + "://" + host + path;
            String[] keyStrings = key.split(",");
            String[] valueStrings = value.split(",");
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value) && keyStrings.length == valueStrings.length) {
                url = url + "?";
                for (int i = 0; i < keyStrings.length; i++) {
                    url = url + keyStrings[i] + "=" + valueStrings[i] + "&";
                }
                url = url.substring(0, url.length() - 1);
                redirect();
            } else if (TextUtils.isEmpty(key) && TextUtils.isEmpty(value)) {
                redirect();
            } else {
                Toast.makeText(MainActivity.this, "The number of key-value pairs you entered is not equal", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "scheme or host should not be null", Toast.LENGTH_LONG).show();
        }
    }

    private void redirect() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            Log.i(TAG, "setData: url==" + url);
            intent.setData(Uri.parse(url));
            textView.setText(String.format(getResources().getString(R.string.text), url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "There is no match this deeplink", Toast.LENGTH_LONG).show();
        }
    }

    private void initData() {
        editText6 = binding.editText6;
        editText7 = binding.editText7;
        editText8 = binding.editText8;
        editText9 = binding.editText9;
        editText10 = binding.editText10;
        textView = binding.textView12;
        button = binding.button;
        textView.setText(String.format(getResources().getString(R.string.text), getSharedPreferences("SharedPreferences", MODE_PRIVATE).getString("deeplink", "")));
        editText6.setText(getSharedPreferences("SharedPreferences", MODE_PRIVATE).getString("scheme", ""));
        editText7.setText(getSharedPreferences("SharedPreferences", MODE_PRIVATE).getString("host", ""));
        editText8.setText(getSharedPreferences("SharedPreferences", MODE_PRIVATE).getString("path", ""));
        editText9.setText(getSharedPreferences("SharedPreferences", MODE_PRIVATE).getString("key", ""));
        editText10.setText(getSharedPreferences("SharedPreferences", MODE_PRIVATE).getString("value", ""));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor sp = getSharedPreferences("SharedPreferences", MODE_PRIVATE).edit();
        sp.putString("deeplink", url);
        sp.putString("scheme", scheme);
        sp.putString("host", host);
        sp.putString("path", path);
        sp.putString("key", key);
        sp.putString("value", value);
        sp.apply();
    }
}
