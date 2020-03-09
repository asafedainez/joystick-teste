package com.autenticar.teste;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class Operador extends AppCompatActivity {

    TextView direcao, potencia, angulo, led, agente, ip;
    boolean ledIR = false;
    RelativeLayout layout_joystick;
    JoyStickClass js;
    String ipServer, nomeAgente;


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

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operador);

        SeekBar ang = findViewById(R.id.seekBar_angCam);



        layout_joystick = findViewById(R.id.relativeLayout_JoystickBase);

        final Button botaoIR = findViewById(R.id.button_IR);
        direcao = findViewById(R.id.textView_direcao);
        potencia = findViewById(R.id.textView_potencia);
        led = findViewById(R.id.textView_botaoIR);
        angulo = findViewById(R.id.textView_seekbarAngulo);
        agente = findViewById(R.id.textView_Operador);
        ip = findViewById(R.id.textView_ip);


//        nomeAgente = "CB. Juvenal";
//        ipServer = "123.456.798.890";

        Intent intent = getIntent();
        if(intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                nomeAgente = params.getString("nome");
                ipServer = params.getString("ip");

                agente.setText("Operador: " + nomeAgente);
                ip.setText("IP Server: " + ipServer);
            }
        }






        botaoIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledIR = !ledIR;

                if (ledIR) {
                    botaoIR.setBackgroundColor(Color.YELLOW);
                    led.setText("LED IR: Ativo");
                    //mandar comando para ativar LED IR
                } else {
                    botaoIR.setBackgroundColor(Color.GRAY);
                    led.setText("LED IR: Desligado");
                    //mandar comando para desativar LED IR
                }


            }
        });



        ang.setMax(90);
        ang.setProgress(45);

        ang.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int prog = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prog = progress;
                angulo.setText("Ângulo Câmera: " + prog + "°");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                angulo.setText("Ângulo Câmera: " + prog + "°");

            }
        });



        js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50); // máxima distancia do centro para iniciar o momvimento

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                js.drawStick(event);
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_MOVE) {

                    float pot = js.getDistance();
                    pot = pot - js.getMinimumDistance();
                    if(pot > 200)
                        pot = 200;
                    else if(pot < 0)
                        pot = 0;
                    pot = pot/2;  //normalizando potencia para 0-100
                    potencia.setText("Potência: " + String.valueOf((int)pot) + "%");

                    int dir = js.get8Direction();
                    if (dir == JoyStickClass.STICK_UP) {
                        direcao.setText("Direção: Frente");
                    } else if (dir == JoyStickClass.STICK_UPRIGHT) {
                        direcao.setText("Direção: Frente Direita");
                    } else if (dir == JoyStickClass.STICK_RIGHT) {
                        direcao.setText("Direção: Direita");
                    } else if (dir == JoyStickClass.STICK_DOWNRIGHT) {
                        direcao.setText("Direção: Atrás Direita");
                    } else if (dir == JoyStickClass.STICK_DOWN) {
                        direcao.setText("Direção: Atrás");
                    } else if (dir == JoyStickClass.STICK_DOWNLEFT) {
                        direcao.setText("Direção: Atrás Esquerda");
                    } else if (dir == JoyStickClass.STICK_LEFT) {
                        direcao.setText("Direção: Esquerda");
                    } else if (dir == JoyStickClass.STICK_UPLEFT) {
                        direcao.setText("Direção: Frente Esquerda");
                    } else if (dir == JoyStickClass.STICK_NONE) {
                        direcao.setText("Direção: Parado");
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    direcao.setText("Direção:");
                    potencia.setText("Potência:");

                }
                return true;
            }
        });


    }



}
