package com.example.qyu;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.VideoView;


public class SplashActivity extends AppCompatActivity {
    VideoView videoHolder;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            videoHolder = findViewById(R.id.videoView);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
            videoHolder.setVideoURI(video);
            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    jump();
                }
            });
            videoHolder.start();
        } catch (
                Exception ex) {
            jump();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        if (sp.getBoolean("login",false)){
            Intent i = new Intent(SplashActivity.this, OrgMainActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(SplashActivity.this, UserTypeActivity.class);
            startActivity(i);
        }
    }
}
