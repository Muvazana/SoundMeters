package com.project.soundmeter_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.project.soundmeter_1.utis.MyMediaRecorder;
import com.project.soundmeter_1.utis.Utils;

public class PengaturanActivity extends AppCompatActivity {

    private ImageButton btnBackPress;
    private TextView txtDbMax;
    private SeekBar seekBar;
    private SharedPreferences sharedPreferences;

    private int dbMaxValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        initVariable();

        btnBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dbMaxValue = progress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtDbMax.setText(progress+" db");
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Utils.MAX_DB_KEY, dbMaxValue);
                editor.commit();
            }
        });
    }
    private void initVariable(){
        sharedPreferences = getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
        btnBackPress = findViewById(R.id.btnBackPress);
        txtDbMax = findViewById(R.id.txtDbMax);
        txtDbMax.setText(sharedPreferences.getInt(Utils.MAX_DB_KEY, 60) + " db");
        seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(sharedPreferences.getInt(Utils.MAX_DB_KEY, 60));
    }
}