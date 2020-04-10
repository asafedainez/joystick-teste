package com.autenticar.teste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class TelaInicial extends AppCompatActivity {

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
        setContentView(R.layout.activity_tela_inicial);




        final EditText nome = findViewById(R.id.editText_nome);
        final EditText ip = findViewById(R.id.editText_IP);
        Button ok = findViewById(R.id.button_OK);
        nome.setText("Ten Cel. Sebastião");
        ip.setText("192.168.0.54");


//        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN.NNN");
//        MaskTextWatcher mtw = new MaskTextWatcher(ip, smf);
//        ip.addTextChangedListener(mtw);



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle params = new Bundle();
                params.putString("nome",nome.getText().toString());
                params.putString("ip",ip.getText().toString());


                Intent intent = new Intent(getApplicationContext(), Operador.class);
                intent.putExtras(params);
                startActivity(intent);


            }
        });





    }
}
