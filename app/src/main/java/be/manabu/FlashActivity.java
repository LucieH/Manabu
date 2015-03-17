package be.manabu;

import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

public class FlashActivity extends ActionBarActivity {

    final Random rnd = new Random();
    private NumberPicker np;
    private int nbEssai;
    protected int compteur = 0;
    protected String strTmp;

    protected void setNp(int min, int max){
        this.np = (NumberPicker) findViewById(R.id.nbSec);
        this.np.setMinValue(min);
        this.np.setMaxValue(max);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/opendyslexic.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_flash_start);
        setNp(5,10);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    /** Permet d'utiliser la police choisie */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // ...
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /** Démarrer le jeu flash*/
    public void startFlash(View view) {
        if(compteur < 10){
            String str;
            int rand = rnd.nextInt(480);
            str = "str_" + rand;
            strTmp=getStringResourceByName(str,getApplicationContext());
            showWord(view);
            final Handler handler = new Handler();
            int temps = (this.np.getValue()) * 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViewAnswer();
                    //setNp(5, 10);
                    compteur ++;
                }
            }, temps);
        }
        else {
            setContentView(R.layout.activity_img_fin);
        }

    }


    protected void verifierReponse(final Button b){
        this.nbEssai = 0;
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                EditText text = (EditText) findViewById(R.id.Answflash);
                String tmp = text.getText().toString().trim();
                if (strTmp.equalsIgnoreCase(tmp)){
                    afficherToast(true, "Bien joué !", "#00A000");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            startFlash(v);
                        }
                    }, 2500);
                }
                else{
                    afficherToast(false, "Essaye encore !", "#FF0000");
                }
                nbEssai++;
                if (nbEssai >= 3) {
                    Button aide = (Button) findViewById(R.id.AideFlash);
                    aide.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    protected void afficherToast(Boolean res, String message, String col){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        afficherToastReponse(res, message, col, getApplicationContext(), layout);
    }

    public void revoirMot(View v){
        showWord(v);
        final Handler handler = new Handler();
        int temps = (this.np.getValue()) * 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewAnswer();
            }
        }, temps);

    }

    private void setViewAnswer(){
        setContentView(R.layout.activity_flash_answer);
        Button b=(Button) findViewById(R.id.VerifFlash);
        verifierReponse(b);
    }

    private void showWord(View view){
        view.invalidate();
        setContentView(R.layout.activity_flash);
        TextView tv = (TextView)findViewById(R.id.TVflash);
        tv.setText(strTmp);
        view.invalidate();
    }

    public void rejouer(View view) {
        view.invalidate();
        this.compteur=0;
        setContentView(R.layout.activity_flash_start);
    }

    public void retournerMenu(View view){
        view.invalidate();
        this.finish();
    }
}
