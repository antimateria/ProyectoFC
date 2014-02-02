package com.android.aesafe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Act_StartActivity extends Activity {
	
	protected boolean _active = true;
	protected int _splashTime = 4000; // time to display the splash screen in ms
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.scr_screenshot);
             
        /* MODELAMOS TIPO FUENTE, TAMAnio DEL TEXTO DEL SPLASH SCREEN */
        TextView txt = (TextView) findViewById(R.id.scrTituloAPP);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/YUKA.ttf");
        txt.setText("AESafe DataVault");
        txt.setTextSize(36);
        txt.setTextColor(Color.parseColor(getString(R.color.Yellow)));
        txt.setTypeface(tf);
        
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(100);
                        if(_active) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                    finish();  
                    startActivity(new Intent(Act_StartActivity.this, Act_LoginActivity.class));
                }
            }
        };
        splashTread.start();
     }

}
