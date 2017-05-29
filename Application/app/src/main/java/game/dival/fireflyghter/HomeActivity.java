package game.dival.fireflyghter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import game.dival.fireflyghter.engine.sound.AudioLibrary;
import game.dival.fireflyghter.engine.sound.SoundHandler;

public class HomeActivity extends Activity {
    private SoundHandler handler = new SoundHandler("caio-guitar.mp3", true);
    private AudioLibrary library;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        library = new AudioLibrary(this);

        library.addStereoSource(handler).startAll();

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        Button startMainActivity = (Button) findViewById(R.id.startBtn);
        Button about = (Button) findViewById(R.id.aboutBtn);
        Button exit  = (Button) findViewById(R.id.exitBtn);

        startMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                library.pauseAll();
                startActivity(new Intent(HomeActivity.this, MainVRActivity.class));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                alertDialog.setTitle("Sobre");
                alertDialog.setMessage("FireFlyghter a VR game developed with Opengl ES and the dival engine.\n" +
                        "\nDevelopers:\n" +
                        "Caio Guedes\n" +
                        "Jordan Lira\n" +
                        "Luzenildo Junior\n\n" +
                        "In game music: A Himitsu - Lost Within: https://youtu.be/e6YS10l_NFY licensed under creative commons");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                library.pauseAll();
                finish();
            }
        });
    }
}
