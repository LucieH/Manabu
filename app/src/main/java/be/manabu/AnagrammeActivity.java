package be.manabu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

public class AnagrammeActivity extends ActionBarActivity {
    private int lvl = 1;
    private Niveaux niv = new Niveaux();
    final Random rnd = new Random();
    protected String strTmp;

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
		setContentView(R.layout.activity_start);
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

    public void start(View view) {
        view.invalidate();
        setContentView(R.layout.activity_anagramme);

        String str;
        do{
            int rand = rnd.nextInt(480);
            str = "str_" + rand;
            strTmp=getStringResourceByName(str,getApplicationContext());
        } while (strTmp.length()>5 || strTmp.length()< 3);
        int lgt = strTmp.length();
        strTmp = strTmp.toUpperCase();
        Button[] tbLettres = new Button[lgt];
        char[] arStr = strTmp.toCharArray();
        ArrayList<Character> tbStr = new ArrayList<Character>();
        for(int i=0; i<lgt; i++){
            tbStr.add(arStr[i]);
        }
        Collections.shuffle(tbStr);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 10);

        LinearLayout ll = (LinearLayout) findViewById(R.id.LayoutAnag);
        for(int i=0; i<lgt; i++){
            tbLettres[i] = new Button(this);
            String temp = "" +tbStr.get(i)+"";
            setBoutonLettre(tbLettres[i], temp, params);
            //A continuer
            ll.addView(tbLettres[i]);
            addMouvementBouton(tbLettres[i]);
        }
        TextView tvAna = (TextView) findViewById(R.id.TVAnag);
        tvAna.setText(strTmp);
    }

    private void setBoutonLettre(Button b, String temp, LinearLayout.LayoutParams params){
        b.setText(temp);
        b.setTextAppearance(this, R.style.texteSurFond);
        b.setShadowLayer(10, 0, 0, Color.BLACK);
        b.setTextSize(25);
        b.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opendyslexic.ttf"));
        //permet de ne pas prendre en compte les accents pour le choix des couleurs entre voyelles et consonnes
        temp = Normalizer.normalize(temp, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
        if (temp.equals("A") || temp.equals("E") || temp.equals("I") || temp.equals("O") || temp.equals("U") || temp.equals("Y"))
            b.setBackgroundResource(R.drawable.voyelles);
        else b.setBackgroundResource(R.drawable.consonnes);
        b.setLayoutParams(params);
    }
    private void addMouvementBouton(final Button b){
        b.setOnTouchListener(new View.OnTouchListener() {
            private float x
                    ,
                    y;
            private int mx
                    ,
                    my;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                    case MotionEvent.ACTION_MOVE:
                        mx = (int) (event.getRawX() - x);
                        my = (int) (event.getRawY() - y);
                        v.getScaleX();
                        if (mx > 30 && my > 30) {
                            b.setX(mx);
                            b.setY(my);
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void afficheRegles(View view){
        chargerRegles(this, view, R.string.regleAna);
    }

    public void changeLvl1(View view){
        lvl=niv.changeLvl1(this, lvl);

    }

    public void changeLvl2(View view){
        lvl=niv.changeLvl2(this, lvl);
    }
    public void changeLvl3(View view){
        lvl=niv.changeLvl3(this, lvl);
    }
}
