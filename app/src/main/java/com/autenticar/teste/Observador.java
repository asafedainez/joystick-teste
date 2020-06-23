package com.autenticar.teste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

public class Observador extends AppCompatActivity {


    private Handler handler = new Handler();
    VideoView videoRpi;
    String ipServer, nomeAgente;
    TextView ip, agente;
    Conexao connect;



    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observador);

        videoRpi = findViewById(R.id.videoView_Rpi);
        agente = findViewById(R.id.textView_observador);
        ip = findViewById(R.id.textView_ip_observador);

        Intent intent = getIntent();
        if(intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                nomeAgente = params.getString("nome");
                ipServer = params.getString("ip");

                agente.setText("Agente: " + nomeAgente);
                ip.setText("IP Server: " + ipServer);


            }
        }

        new Thread(){
            public void run() {
                connect = new Conexao(ipServer, nomeAgente);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        do {
                            if (connect.getMyUUID() != null) {
                                Log.i("MENSAGEM ","CONECTADO");
                            } else {
                                Log.i("ERRO","Conexão mal sucedida");
                            }
                        }while(connect.getMyUUID() == null);

                    }
                });
            }
        }.run();

        String url = "http://" + "192.168.0.43"//connect.getIP_VTNT()
                + "/rpi-cam/cam_pic_new.php";

        videoRpi.setVideoURI(Uri.parse(url));
        videoRpi.start();
        videoRpi.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("ERRO","ERRO AO REPRODUZIR VÍDEO");
                return true;
            }
        });





    }
}
