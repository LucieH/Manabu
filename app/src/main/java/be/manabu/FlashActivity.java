package be.manabu;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

/**
 * @author Lucie Herrier - 3TL1
 */

public class FlashActivity extends ActionBarActivity {

    final Random rnd = new Random();
    private final static int NBTOURS = 10;
    private final static int NBMOTS = 480;
    private final static int NBMOTS_2 = 50;
    private final static int NBMOTS_3 = 50;
    private int nbEssai;
    protected int compteur = 0;
    protected String strTmp;
    private int secondes=5;
    private int lvl = 1;
    private int idLayout = 0;
    private int[] tabMotPre;
    private Niveaux niv = new Niveaux();
    CustomKeyboard mCustomKeyboard;

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

    /**
     *  Cette fonction permet d'utiliser la police choisie (by chrisjenx : https://github.com/chrisjenx/Calligraphy)
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     *
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // ...
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Cette fonction définit le comportement de l'activité lorsque la touche "back" a été pressée,
     * dépendemment du layout en cours d'affichage.
     */
    @Override
    public void onBackPressed() {
        if (idLayout == R.layout.activity_flash_answer){
            if( mCustomKeyboard.isCustomKeyboardVisible() ) mCustomKeyboard.hideCustomKeyboard();
        }
        if (idLayout == R.layout.activity_flash_start || idLayout == R.layout.activity_flash_answer || idLayout == R.layout.regles) {
            setContentView(R.layout.activity_start);
            idLayout = R.layout.activity_start;
            lvl = 1;
            compteur = 0;
        }
        else this.finish();
    }

    /**
     * Cette fonction permet de démarrer l'exercice de lecture flash en affichant le layout permettant
     * de choisir le nombre de secondes.
     * @param view la vue en cours
     */
    public void start(View view) {
        tabMotPre = new int[NBTOURS];
        view.invalidate();
        setContentView(R.layout.activity_flash_start);
        idLayout = R.layout.activity_flash_start;
        TextView nbSec = (TextView) findViewById(R.id.TVSecondesFlash);
        nbSec.setText(""+secondes+"");
    }

    /**
     * Démarrer le jeu flash
     */
    public void startFlash(View view) {
        if(compteur < NBTOURS){
            String str = "str_";
            int rand;
            int nbmots = NBMOTS;
            switch (lvl){
                case 1 :
                    break;
                case 2 :
                    str = "str2_";
                    nbmots = NBMOTS_2;
                    break;
                case 3 :
                    str = "str3_";
                    nbmots = NBMOTS_3;
                    break;
                default:
                    break;
            }
            do {
                rand = rnd.nextInt(nbmots)+1;
            }while (existeMotPre(rand));
            strTmp=getStringResourceByName(str + rand + "",getApplicationContext());
            tabMotPre[compteur] = rand;
            showWord(view);
            final Handler handler = new Handler();
            int temps = secondes * 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViewAnswer();
                    compteur ++;
                }
            }, temps);
        }
        else {
            setContentView(R.layout.activity_img_fin);
            idLayout = R.layout.activity_img_fin;
        }

    }

    /**
     * Cette fonction vérifie si le mot a déjà été joué pendant la série de 10 actuelle.
     * @param rand le nombre généré aléatoirement ce tour-ci
     * @return true si le nombre est déjà sorti, false dans le cas contraire
     */
    private boolean existeMotPre(int rand){
        for(int i=0; i<compteur; i++){
            if (tabMotPre[i] == rand) return true;
        }
        return false;
    }

    /**
     *
     * @param b
     */
    protected void verifierReponse(final Button b){
        this.nbEssai = 0;
        final Activity act = this;
        final Button aide = (Button) findViewById(R.id.AideFlash);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                if( mCustomKeyboard.isCustomKeyboardVisible() ) mCustomKeyboard.hideCustomKeyboard();
                EditText text = (EditText) findViewById(R.id.Answflash);
                String tmp = text.getText().toString().trim();
                final Handler handler = new Handler();
                if (strTmp.equalsIgnoreCase(tmp)){
                    afficherToastRep(act, true, getApplicationContext());
                    b.setEnabled(false);
                    aide.setEnabled(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startFlash(v);
                        }
                    }, 2500);
                }
                else{
                    afficherToastRep(act, false, getApplicationContext());
                    b.setEnabled(false);
                    aide.setEnabled(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            b.setEnabled(true);
                            aide.setEnabled(true);
                        }
                    }, 2500);
                }
                nbEssai++;
                if (nbEssai >= 1) {
                    aide.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     *
     * @param v
     */
    public void revoirMot(View v){
        showWord(v);
        final Handler handler = new Handler();
        int temps = secondes * 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewAnswer();
            }
        }, temps);

    }

    /**
     *
     */
    private void setViewAnswer(){
        setContentView(R.layout.activity_flash_answer);
        idLayout = R.layout.activity_flash_answer;
        mCustomKeyboard= new CustomKeyboard(getApplicationContext(),this, R.id.keyboardview, R.xml.alphabet_keyboard);
        mCustomKeyboard.registerEditText(R.id.Answflash);
        Button b=(Button) findViewById(R.id.VerifFlash);
        verifierReponse(b);
    }

    /**
     *
     * @param view
     */
    private void showWord(View view){
        view.invalidate();
        setContentView(R.layout.activity_flash);
        idLayout = R.layout.activity_flash;
        TextView tv = (TextView)findViewById(R.id.TVflash);
        tv.setText(strTmp);
        view.invalidate();
    }

    /**
     *
     * @param view
     */
    public void rejouer(View view) {
        view.invalidate();
        compteur=0;
        lvl = 1;
        setContentView(R.layout.activity_start);
        idLayout = R.layout.activity_start;
    }

    /**
     *
     * @param view
     */
    public void retournerMenu(View view){
        view.invalidate();
        this.finish();
    }

    /**
     *
     * @param view
     */
    public void decrementer(View view){
        if (secondes >1) {
            this.secondes--;
            TextView nbSec = (TextView) findViewById(R.id.TVSecondesFlash);
            nbSec.setText(""+secondes+"");
        }

    }

    /**
     *
     * @param view
     */
    public void incrementer(View view){
        if (secondes <20) {
            this.secondes++;
            TextView nbSec = (TextView) findViewById(R.id.TVSecondesFlash);
            nbSec.setText(""+secondes+"");
        }
    }

    /**
     *
     * @param view
     */
    public void afficheRegles(View view){
        idLayout = chargerRegles(this, view, R.string.regleFlash);
    }

    /**
     *
     * @param v
     */
    public void afficheDialog(View v){
        ReglesDialog dia = new ReglesDialog();
        dia.setIdString(R.string.regleFlash);
        dia.show(getFragmentManager(),"Regles");
    }

    /**
     *
     * @param v
     */
    public void jouerSonRegles(View v){
        Utilities.jouerSon("ok",getApplicationContext());
    }

    /**
     *
     * @param view
     */
    public void changeLvl1(View view){
        lvl=niv.changeLvl1(this, lvl);

    }

    /**
     *
     * @param view
     */
    public void changeLvl2(View view){
        lvl=niv.changeLvl2(this, lvl);
    }

    /**
     *
     * @param view
     */
    public void changeLvl3(View view){
        lvl=niv.changeLvl3(this, lvl);
    }

    /**
     *
     * @param view
     */
    public void back(View view){ revenirDebut(this, view);}


}
