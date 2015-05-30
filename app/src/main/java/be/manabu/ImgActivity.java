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
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

public class ImgActivity extends ActionBarActivity {

    private final Random rnd = new Random();
    private final static int NB_IMAGES = 21;
    private final static int NB_TOURS = 10;
    private String strTmp = "start";
    private int tabNbImages[] = new int[NB_IMAGES];
    private int cmptImages = 0;
    public int lvl = 1;
    private Niveaux niv = new Niveaux();
    private int idLayout;

    //Getters et setters
    public void setStrTmp(String s){
        this.strTmp = s;
    }

    //Fonctions override
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
        if (idLayout == R.layout.activity_img || idLayout == R.layout.activity_img_choix || idLayout == R.layout.regles){
            setContentView(R.layout.activity_start);
            idLayout = R.layout.activity_start;
            lvl = 1;
            cmptImages = 0;
        }
        else  this.finish();
    }


    //Fonctions personnelles

    /** Démarrer le jeu imagerie ave les fiches Freinet*/
    public void start(View view) {
        view.invalidate();
        if (cmptImages < NB_TOURS) {
            setContentView(R.layout.activity_img);
            idLayout = R.layout.activity_img;
            randomImg();
        }
        else {
            setContentView(R.layout.activity_img_fin);
            idLayout = R.layout.activity_img_fin;
        }

    }

    /** Afficher les choix pour l'image*/
    public void afficherChoix(View view) {
        view.invalidate();
        setContentView(R.layout.activity_img_choix);
        idLayout =  R.layout.activity_img_choix;
        final ImageView img = (ImageView) findViewById(R.id.imgRandom);
        img.setImageDrawable
                (
                        getResources().getDrawable(getResourceID(strTmp, "drawable",
                                getApplicationContext()))
                );
        final Button b1 = (Button) findViewById(R.id.choix1);
        final Button b2 = (Button) findViewById(R.id.choix2);
        final Button b3 = (Button) findViewById(R.id.choix3);

        //texte ramdom dans les boutons
        creerBoutonRandom(b1, b2, b3);
    }

     /**  Permet d'avoir les images prises au hasard avec le mot correspondant  */
    protected void randomImg(){
        final ImageView img = (ImageView) findViewById(R.id.imgRandom);
        String str;
        int rand;
        do {
            rand = rnd.nextInt(NB_IMAGES);
            str = "img_" + rand;
        }while (existeImageAffichee(rand));
        img.setImageDrawable
                (
                        getResources().getDrawable(getResourceID(str, "drawable",
                                getApplicationContext()))
                );
        final TextView tv = (TextView) findViewById(R.id.tv1);
        tv.setText(getStringResourceByName(str, getApplicationContext()));
        setStrTmp(str);
        this.tabNbImages[cmptImages]=rand;
        this.cmptImages++;
    }

    protected void creerBoutonRandom(Button b1, Button b2, Button b3){
        int i = rnd.nextInt(3);
        int j = rnd.nextInt(2);
        Button a = null;
        Button b = null;
        switch(i){
            case 0:
                setBonneReponse(b1);
                switch(j){
                    case 0:
                        a=b3;
                        b=b2;

                        break;
                    case 1:
                        a=b2;
                        b=b3;
                        break;
                }
                setMauvaiseReponse(a, b);
                break;
            case 1:
                setBonneReponse(b2);
                switch(j){
                    case 0:
                        a=b3;
                        b=b1;
                        break;
                    case 1:
                        a=b1;
                        b=b3;
                        break;
                }
                setMauvaiseReponse(a, b);
                break;
            case 2:
                setBonneReponse(b3);
                switch(j){
                    case 0:
                        a=b1;
                        b=b2;
                        break;
                    case 1:
                        a=b2;
                        b=b1;
                        break;
                }
                setMauvaiseReponse(a, b);
                break;
        }
    }

    protected void setBonneReponse(Button b){
        final Activity act = this;
        b.setText(getStringResourceByName(strTmp,getApplicationContext()));
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                afficherToast(act, true, getResources().getString(R.string.bienJoue), "#00A000", getApplicationContext());
                disableButtons();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start(v);
                    }
                }, 2500);
            }
        });
    }

    protected void setMauvaiseReponse(final Button a, final Button b){
        final Activity act = this;
        a.setText(getStringResourceByName(strTmp+"_1",getApplicationContext()));
        b.setText(getStringResourceByName(strTmp+"_2",getApplicationContext()));
        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                afficherToast(act, false, getResources().getString(R.string.reessaye), "#FF0000", getApplicationContext());
                disableButtons();
                reEnableButtons();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                afficherToast(act, false, getResources().getString(R.string.reessaye), "#FF0000", getApplicationContext());
                disableButtons();
                reEnableButtons();
            }
        });
    }

    private void disableButtons(){
        Button a = (Button) findViewById(R.id.choix1);
        Button b = (Button) findViewById(R.id.choix2);
        Button c = (Button) findViewById(R.id.choix3);
        a.setEnabled(false);
        b.setEnabled(false);
        c.setEnabled(false);
    }

    private void reEnableButtons(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Button a = (Button) findViewById(R.id.choix1);
                Button b = (Button) findViewById(R.id.choix2);
                Button c = (Button) findViewById(R.id.choix3);
                a.setEnabled(true);
                b.setEnabled(true);
                c.setEnabled(true);
            }
        }, 2500);
    }

    private boolean existeImageAffichee(int rand){
        for(int i=0; i<cmptImages; i++){
            if (tabNbImages[i] == rand) return true;
        }
        return false;
    }

    public void rejouer(View view) {
        view.invalidate();
        this.cmptImages=0;
        setContentView(R.layout.activity_start);
        idLayout = R.layout.activity_start;
    }

    public void retournerMenu(View view){
        view.invalidate();
        this.finish();
    }

    public void afficheRegles(View view){
        idLayout = chargerRegles(this, view, R.string.regleImg);
    }
    public void jouerSonRegles(View v){
        Utilities.jouerSon("ok",getApplicationContext());
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


