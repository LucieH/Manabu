package be.manabu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableRow;
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
    private String strName;
    private structBouton[] tbStructBouton;
    private int nbChar;
    private boolean isSetPos = false;

    class structBouton {
        public float posX, posY;
        public String lettre;
        public boolean ok = false;
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
        nbChar = 0;
        isSetPos = false;
        view.invalidate();
        setContentView(R.layout.activity_anagramme);
        //trouver un mot
        do{
            int rand = rnd.nextInt(480);
            strName = "str_" + rand;
            strTmp=getStringResourceByName(strName,getApplicationContext());
        } while (strTmp.length()>5 || strTmp.length()< 3);
        //jouer le son du mot
        Utilities.jouerSon(strName, getApplicationContext());
        //prendre la longueur et mettre en majuscule
        nbChar = strTmp.length();
        strTmp = strTmp.toUpperCase();
        // créer un tableau de boutons de la longueur du mot
        Button[] tbLettres = new Button[nbChar];
        //créer le tableau qui permettra la vérification
        tbStructBouton = new structBouton[nbChar];
        char[] arStr = strTmp.toCharArray();
        //creer une liste pour mélanger les caractères
        ArrayList<Character> tbStr = new ArrayList<Character>();
        for(int i=0; i<nbChar; i++){
            tbStr.add(arStr[i]);
        }
        Collections.shuffle(tbStr);

        /*TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );*/


        GridLayout tr = (GridLayout) findViewById(R.id.LayoutAna);
        tr.setRowCount(3);
        tr.setColumnCount(nbChar);

        for(int i=0; i<nbChar; i++){
            tbLettres[i] = new Button(this);
            String temp = String.valueOf(arStr[i]);
            setBoutonVerif(tbLettres[i], temp, i);
            //A continuer
            tr.addView(tbLettres[i]);
        }



        for(int i=0; i<nbChar; i++){
            tbLettres[i] = new Button(this);
            String temp = "" +tbStr.get(i)+"";
            setBoutonLettre(tbLettres[i], temp, i);
            //A continuer
            tr.addView(tbLettres[i]);
            addMouvementBouton(tbLettres[i]);
        }
    }

    private void setBoutonLettre(Button b, String temp, int i){
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
        b.setLayoutParams(setParams(2, i));
    }

    private void setBoutonVerif(Button b, String temp, int i){
        b.setText(temp);
        b.setTextAppearance(this, R.style.texteSurFond);
        b.setTextSize(25);
        b.setId(i);
        b.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/opendyslexic.ttf"));
        temp = Normalizer.normalize(temp, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
        b.setBackgroundResource(R.drawable.anagramme_validate);
        b.setLayoutParams(setParams(1, i));

    }

    private GridLayout.LayoutParams setParams(int row, int i){
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(i,1);
        params.rowSpec = GridLayout.spec(row);
        params.setGravity(Gravity.TOP);
        params.setMargins(10, 10, 10, 10);
        return params;
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
                if (! isSetPos){
                    for (int i = 0; i < nbChar; i++) {
                        Button b = (Button) findViewById(i);
                        tbStructBouton[i] = new structBouton();
                        tbStructBouton[i].posX = b.getX();
                        System.out.println(b.getX());
                        tbStructBouton[i].posY = b.getY();
                        System.out.println(b.getY());
                        tbStructBouton[i].lettre = b.getText().toString();
                        System.out.println(b.getText().toString());
                    }
                    isSetPos = true;

                }
                //Utilities.jouerSon("ok_lettre",getApplicationContext());
                int pos = -1;
                float minX, maxX, minY, maxY;
                for (int i = 0; i < nbChar; i++) {
                    if (!tbStructBouton[i].ok) {

                        if (b.getText().toString().equals(tbStructBouton[i].lettre)) {
                            pos = i;
                            break;
                        }
                    }
                }
                minX = (tbStructBouton[pos].posX) - 10;
                maxX = (tbStructBouton[pos].posX) + 10;
                minY = (tbStructBouton[pos].posY) - 10;
                maxY = (tbStructBouton[pos].posY) + 10;
                //System.out.println(b.getX());
                if (b.getX() < maxX && b.getX() > minX && b.getY() < maxY && b.getY() > minY){
                    b.setX(tbStructBouton[pos].posX);
                    b.setY(tbStructBouton[pos].posY);
                   // tbStructBouton[pos].ok = true;
                    Utilities.jouerSon("ok_lettre",getApplicationContext());
                    return true;

                }
                else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = event.getX();
                            y = event.getY();
                        case MotionEvent.ACTION_MOVE:
                            mx = (int) (event.getRawX() - x);
                            my = (int) (event.getRawY() - y);
                            v.getScaleX();
                            if (mx > 0 && my > 0) {
                                b.setX(mx);
                                b.setY(my);
                            }
                            break;
                    }
                    return true;
                }
            }
        });
    }

    public void jouerSon(View v){
        Utilities.jouerSon(strName, getApplicationContext());
    }

    public void afficheRegles(View view){
        chargerRegles(this, view, R.string.regleAna);
    }
    public void changeLvl1(View view){ lvl=niv.changeLvl1(this, lvl);}
    public void changeLvl2(View view){
        lvl=niv.changeLvl2(this, lvl);
    }
    public void changeLvl3(View view){
        lvl=niv.changeLvl3(this, lvl);
    }
    public void back(View view){ revenirDebut(this, view);}
}
