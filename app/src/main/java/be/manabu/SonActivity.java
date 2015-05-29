package be.manabu;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

public class SonActivity extends ActionBarActivity {
    final Random rnd = new Random();
    final static private int START_ASCII_LETTRES = 97;
    final static private int NB_SONS = 28;
    // Constantes permettant de savoir la fin de chaque lettre du VOB dans strings.xml
    final static private int END = 481;
    final static private int START_A =0;
    final static private int START_B =29;
    final static private int START_C =57;
    final static private int START_D =108;
    final static private int START_E = 143;
    final static private int START_F = 162 ;
    final static private int START_G = 193;
    final static private int START_H = 205;
    final static private int START_I = 212;
    final static private int START_J = 216;
    final static private int START_K = 232;
    final static private int START_L = 233;
    final static private int START_M = 257;
    final static private int START_N = 300;
    final static private int START_O = 317;
    final static private int START_P = 327;
    final static private int START_Q = 378;
    final static private int START_R = 383;
    final static private int START_S = 404;
    final static private int START_T = 431;
    final static private int START_U = 458;
    final static private int START_V = 461;
    final static private int START_Z = 480;
    private int lvl = 1;
    private Niveaux niv = new Niveaux();
    private int idLayout = 0;

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
        idLayout = R.layout.activity_start;
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

    @Override
    public void onBackPressed() {
        if (idLayout == R.layout.activity_son || idLayout == R.layout.regles){
            setContentView(R.layout.activity_start);
            idLayout = R.layout.activity_start;
            lvl = 1;
        }
        else  this.finish();
    }

    public void start(View view) {
        view.invalidate();
        setContentView(R.layout.activity_son);
        idLayout = R.layout.activity_son;
        int rand = rnd.nextInt(NB_SONS);
        setChoixLvl1(rand);
    }

    private void setChoixLvl1(int rand){
        TextView tvSon = (TextView) findViewById(R.id.TVSonIS);
        int nbAscii = rand+START_ASCII_LETTRES;
        switch (rand){
            case 0 :
                // son a
                break;
            case 1 :
                // son b
                findLettreSimple(START_B, START_C);
                break;
            case 2 :
                // son ch
                nbAscii = 2;
                break;
            case 3 :
                // son d
                findLettreSimple(START_D, START_E);
                break;
            case 4 :
                // son e
                break;
            case 5 :
                // son f
                findLettreSimple(START_F,START_G);
                break;
            case 6 :
                // son g
                break;
            case 7 :
                // y'a pas de son h... A REMPLACER
                break;
            case 8 :
                // son i
                break;
            case 9 :
                // son j
                break;
            case 10 :
                // son k
                break;
            case 11 :
                // son l
                findLettreSimple(START_L, START_M);
                break;
            case 12 :
                // son m
                findLettreSimple(START_M, START_N);
                break;
            case 13 :
                // son n
                findLettreSimple(START_N, START_O);
                break;
            case 14 :
                // son o
                break;
            case 15 :
                // son p
                findLettreSimple(START_P, START_Q);
                break;
            case 16 :
                // son q = k, A REMPLACER
                break;
            case 17 :
                // son r
                findLettreSimple(START_R, START_S);
                break;
            case 18 :
                // son s
                break;
            case 19 :
                // son t
                findLettreSimple(START_T, START_U);
                break;
            case 20 :
                // son u
                findLettreSimple(START_U, START_V);
                break;
            case 21 :
                // son v
                findLettreSimple(START_V, START_Z);
                break;
            case 22 :
                // son w A PAS, A REMPLACER
                break;
            case 23 :
                // son x A PAS, A REMPLACER
                break;
            case 24 :
                // son y A PAS, A REMPLACER
                break;
            case 25 :
                // son z
                Button b = (Button) findViewById(R.id.bSons1);
                String texte = getStringResourceByName("str_480",getApplicationContext());
                b.setText(texte);
                break;
            case 26 :
                //le son est e accent aigu
                nbAscii = 233;
                break;
            case 27 :
                //le son est e accent grave
                nbAscii = 232;
                break;
            default:
                break;
                //les lettres de l'alphabet
                //je considere que le son ch est represente par la lettre c.
        }
        if (nbAscii == 2)  tvSon.setText("Le son est : ch");
        else tvSon.setText("Le son est : " + String.valueOf(Character.toChars(nbAscii)));
    }

    private void findSonA(){

    }

    private void findLettreSimple(int min, int max){
        int r = rnd.nextInt(max-min)+min;
        Button b = (Button) findViewById(R.id.bSons1);
        String texte = "str_"+r+"";
        b.setText(getStringResourceByName(texte, getApplicationContext()));
    }

    public void afficheRegles(View view){
        idLayout = chargerRegles(this, view, R.string.regleSon);
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
    public void back(View view){ revenirDebut(this, view);}
}
