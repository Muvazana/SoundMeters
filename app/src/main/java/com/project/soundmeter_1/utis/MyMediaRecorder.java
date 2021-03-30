package com.project.soundmeter_1.utis;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.project.soundmeter_1.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MyMediaRecorder {
    private String pathSave = "";
    private android.media.MediaRecorder mediaRecorder;
    private Activity activity;

    public static final int REQUEST_PERMISSION_MEDIA_RECORDER_CODE = 100;

    public MyMediaRecorder(Activity activity){
        this.activity = activity;
    }

    public void setPathSave(){
        pathSave = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+"/"
                + UUID.randomUUID().toString()+"_audio_record.3gp";
    }

    public void setupMediaRecorder(File file){
        mediaRecorder = new android.media.MediaRecorder();
        mediaRecorder.setAudioSource(android.media.MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(android.media.MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(android.media.MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(file.getAbsoluteFile().toString());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "This Apps not Available for your device!", Toast.LENGTH_SHORT).show();
        }
    }
    public void setMediaRecorderToStop(){
        mediaRecorder.stop();
    }

    public float getMaxAmplitude() {
        if (mediaRecorder != null) {
            try {
                return mediaRecorder.getMaxAmplitude();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 5;
        }
    }

    public boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result  == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(){
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_MEDIA_RECORDER_CODE);
    }
}
