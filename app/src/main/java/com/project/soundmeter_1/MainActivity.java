package com.project.soundmeter_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.soundmeter_1.utis.FileUtil;
import com.project.soundmeter_1.utis.MyMediaRecorder;
import com.project.soundmeter_1.utis.Utils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.pawelkleczkowski.customgauge.CustomGauge;

import static com.project.soundmeter_1.utis.MyMediaRecorder.REQUEST_PERMISSION_MEDIA_RECORDER_CODE;
import static com.project.soundmeter_1.utis.Utils.msgWhat;
import static com.project.soundmeter_1.utis.Utils.refreshTime;
import static com.project.soundmeter_1.utis.Utils.volume;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnPengaturan;
    private CircleImageView ivTelinga;
    private CustomGauge customGauge;
    private ImageView imgMessage;
    private TextView txtMessage;

    private SharedPreferences sharedPreferences;
    private MyMediaRecorder myMediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariable();

        btnPengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PengaturanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initVariable(){
        sharedPreferences = getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
        btnPengaturan = findViewById(R.id.btnPengaturan);
        ivTelinga = findViewById(R.id.ivTelinga);
        customGauge = findViewById(R.id.customGauge);
        customGauge.setEndValue(100);
        imgMessage = findViewById(R.id.imgMessage);
        txtMessage = findViewById(R.id.txtMessage);
        myMediaRecorder = new MyMediaRecorder(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(myMediaRecorder.checkPermissionFromDevice()){
            Utils.dbMax = sharedPreferences.getInt(Utils.MAX_DB_KEY, 60);
            File file = FileUtil.createFile("temp.amr");
            myMediaRecorder.setupMediaRecorder(file);
            handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
//            Toast.makeText(MainActivity.this, "Listening...", Toast.LENGTH_SHORT).show();
        }else
            myMediaRecorder.requestPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            myMediaRecorder.setMediaRecorderToStop();
            handler.removeMessages(msgWhat);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_MEDIA_RECORDER_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.hasMessages(msgWhat)) {
                return;
            }
            volume = myMediaRecorder.getMaxAmplitude();
            if(volume > 0 && volume < 1000000){
                Utils.setDbCount(20 * (float)(Math.log10(volume)));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        customGauge.setValue(Math.round(Utils.dbCount));
                        if(Utils.dbCount > Utils.dbMax){
                            ivTelinga.setImageResource(R.drawable.ic_telinga_merah);
                            imgMessage.setImageResource(R.drawable.ic_sepedamotor);
                            txtMessage.setText(R.string.message_2);
                        }else{
                            ivTelinga.setImageResource(R.drawable.ic_telinga);
                            imgMessage.setImageResource(R.drawable.ic_bacabuku);
                            txtMessage.setText(R.string.message_1);
                        }
                    }
                });
            }
            handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
        }
    };
}




//        Button button = findViewById(R.id.button);
//        customGauge.setEndValue(300);

//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                try {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            current += 50;
//                            customGauge.setValue(current);
//                            if(current > 100){
//                                circleImageView.setImageResource(R.drawable.ic_telinga_merah);
//                            }
//                        }
//                    });
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
////                new Thread() {
////                    public void run() {
////                        for (i = 0;i<100;i++) {
////                            if (i == 50) {
////                                customGauge.setEndValue(1200);
////                            }
////                            try {
////                                runOnUiThread(new Runnable() {
////                                    @Override
////                                    public void run() {
////                                        customGauge.setValue(200 + i*5);
////                                    }
////                                });
////                                Thread.sleep(50);
////                            } catch (InterruptedException e) {
////                                e.printStackTrace();
////                            }
////                        }
////                    }
////                }.start();
//            }
//        });