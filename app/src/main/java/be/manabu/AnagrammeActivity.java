package be.manabu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    private final static int NBTOURS = 10;
    private final static int NBMOTS = 481;
    private int compteur = 0;
    private int lvl = 1;
    private Niveaux niv = new Niveaux();
    final Random rnd = new Random();
    protected String strTmp;
    private String strName;
    private structBouton[] tbStructBouton;
    private int nbChar;
    private boolean isSetPos = false;
    private int nbLettresOk = 0;
    private int[] tabMotPre = new int[NBTOURS];
    private int idLayout = 0;

    class structBouton {
        public float posX, posY;
        public String lettre;
        public boolean ok = false;
    }

    class boutonLettre{
        public Button b;
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
        if (idLayout == R.layout.activity_anagramme || idLayout == R.layout.regles){
            setContentView(R.layout.activity_start);
            idLayout = R.layout.activity_start;
            lvl = 1;
            compteur = 0;
        }
        else  this.finish();
    }

    public void start(View view) {
        if (compteur<NBTOURS) {
            nbLettresOk = 0;
            nbChar = 0;
            isSetPos = false;
            view.invalidate();
            setContentView(R.layout.activity_anagramme);
            idLayout = R.layout.activity_anagramme;
            //trouver un mot
            int rand = 0;
            do {
                rand = rnd.nextInt(NBMOTS);
                strName = "str_" + rand;
                strTmp = getStringResourceByName(strName, getApplicationContext());
            } while (strTmp.length() > getMaxLettres() || strTmp.length() < getMinLettres() || rand ==0 || existeMotPre(rand));
            tabMotPre[compteur] = rand;
            //jouer le son du mot
            Utilities.jouerSon(strName, getApplicationContext());
            //prendre la longueur et mettre  le mot en majuscule
            nbChar = strTmp.length();
            strTmp = strTmp.toUpperCase();
            // créer un tableau de boutons de la longueur du mot
            boutonLettre[] tbBoutonLettres = new boutonLettre[nbChar];
            //créer le tableau qui permettra la vérification
            tbStructBouton = new structBouton[nbChar];
            char[] arStr = strTmp.toCharArray();
            //creer une liste pour mélanger les caractères
            ArrayList<Character> tbStr = new ArrayList<Character>();
            for (int i = 0; i < nbChar; i++) {
                tbStr.add(arStr[i]);
            }
            Collections.shuffle(tbStr);

            placerBoutons(tbBoutonLettres, arStr, tbStr);
        }
        else{
            setContentView(R.layout.activity_img_fin);
            idLayout = R.layout.activity_img_fin;
        }
    }

    private void placerBoutons(boutonLettre[] tbBoutonLettres, char[] arStr, ArrayList<Character> tbStr ){
        RelativeLayout lay = (RelativeLayout) findViewById(R.id.LayoutAna);
        int idBouton = R.id.bReplay;
        //Placer les cases blanches
        for (int i = 0; i < nbChar; i++) {
            tbBoutonLettres[i] = new boutonLettre();
            tbBoutonLettres[i].b = new Button(this);
            String temp = String.valueOf(arStr[i]);
            idBouton = setBoutonVerif(tbBoutonLettres[i].b, temp, i,idBouton);
            lay.addView(tbBoutonLettres[i].b);
        }
        //Placer les lettres a remettre dans l'ordre
        for (int i = 0; i < nbChar; i++) {
            tbBoutonLettres[i] = new boutonLettre();
            tbBoutonLettres[i].b = new Button(this);
            String temp = "" + tbStr.get(i) + "";
            idBouton = setBoutonLettre(tbBoutonLettres[i].b, temp, i, idBouton);
            lay.addView(tbBoutonLettres[i].b);
            addMouvementBouton(tbBoutonLettres[i], this);
        }
    }

    /* Verifie si le mot est deja sorti dans la serie de 10*/
    private boolean existeMotPre(int rand){
        for(int i=0; i<compteur; i++){
            if (tabMotPre[i] == rand) return true;
        }
        return false;
    }

    /* Met en place l'affichage des boutons de type lettre */
    private int setBoutonLettre(Button b, String temp, int i, int idBouton){
        b.setText(temp);
        b.setTextAppearance(this, R.style.texteSurFond);
        b.setShadowLayer(10, 0, 0, Color.BLACK);
        b.setTextSize(25);
        b.setId(i + 10);
        b.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opendyslexic.ttf"));
        //permet de ne pas prendre en compte les accents pour le choix des couleurs entre voyelles et consonnes
        temp = Normalizer.normalize(temp, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
        if (temp.equals("A") || temp.equals("E") || temp.equals("I") || temp.equals("O") || temp.equals("U") || temp.equals("Y"))
            b.setBackgroundResource(R.drawable.voyelles);
        else b.setBackgroundResource(R.drawable.consonnes);
        b.setLayoutParams(setParams(i, 0, idBouton, false));
        return i+10;
    }

    /* Met en place l'affichage des boutons de type cases blanches */
    private int setBoutonVerif(Button b, String temp, int i, int idBouton){
        b.setText(temp);
        b.setTextColor(getResources().getColor(R.color.White));
        b.setTextSize(25);
        b.setId(i);
        b.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/opendyslexic.ttf"));
        b.setBackgroundResource(R.drawable.anagramme_validate);
        b.setLayoutParams(setParams(i, R.id.bReplay, idBouton, true));
        return i;

    }

    /* Definit les parametres a mettre en place pour les objets sur le relative layout*/
    private RelativeLayout.LayoutParams setParams(int i, int buttonTop, int idButton, boolean verif ){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if (i==0) {
            params.addRule(RelativeLayout.BELOW, buttonTop);
            params.addRule(RelativeLayout.ALIGN_LEFT, buttonTop);
            params.addRule(RelativeLayout.ALIGN_START, buttonTop);
        }
        else{
                if (i==1 && verif){
                    params.addRule(RelativeLayout.BELOW,buttonTop);
                    params.addRule(RelativeLayout.RIGHT_OF, buttonTop);
                    params.addRule(RelativeLayout.END_OF, buttonTop);
                }
            else {
                    if (verif) params.addRule(RelativeLayout.ALIGN_TOP, idButton);
                    else params.addRule(RelativeLayout.ALIGN_BOTTOM,idButton);
                    params.addRule(RelativeLayout.RIGHT_OF, idButton);
                    params.addRule(RelativeLayout.END_OF, idButton);
                }

        }
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = (size.y)/3;
        if (verif) {
            if(i != 0) {
                if (i == 1) params.setMargins(20, 0, 10, 0);
                else params.setMargins(10, 0, 10, 0);
            }
        }
        else params.setMargins(0,height,20,0);
        return params;
    }

    /* Implemente le mouvement des boutons avec les lettres et leur immobilisme si elles sont sur la bonne case blanche*/
    private void addMouvementBouton(final boutonLettre bL, final Activity act){
        bL.b.setOnTouchListener(new View.OnTouchListener() {
            private float x,
                    y;
            private int mx,
                    my;
            public boolean onTouch(final View v, MotionEvent event) {
                if (!isSetPos) {
                    setPositionBoutons();
                }
                int pos = -1;
                float minX, maxX, minY, maxY;
                for (int i = 0; i < nbChar; i++) {
                    if (!tbStructBouton[i].ok) {
                        if (bL.b.getText().toString().equals(tbStructBouton[i].lettre)) {
                            pos = i;
                            break;
                        }
                    }
                }
                if (pos == -1 || bL.ok) return true;
                minX = (tbStructBouton[pos].posX) - 10;
                maxX = (tbStructBouton[pos].posX) + 10;
                minY = (tbStructBouton[pos].posY) - 10;
                maxY = (tbStructBouton[pos].posY) + 10;
                if (bL.b.getX() < maxX && bL.b.getX() > minX && bL.b.getY() < maxY && bL.b.getY() > minY) {
                    validerLettre(bL, pos, act, v);
                    return true;

                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = event.getX();
                            y = event.getY();
                        case MotionEvent.ACTION_MOVE:
                            mx = (int) (event.getRawX() - x);
                            my = (int) (event.getRawY() - y);
                            v.getScaleX();
                            if (mx > 0 && my > 0) {
                                bL.b.setX(mx);
                                bL.b.setY(my);
                            }
                            break;
                    }
                    return true;
                }
            }
        });
    }

    private void setPositionBoutons(){
        for (int i = 0; i < nbChar; i++) {
            Button b = (Button) findViewById(i);
            tbStructBouton[i] = new structBouton();
            tbStructBouton[i].posX = b.getX();
            tbStructBouton[i].posY = b.getY();
            tbStructBouton[i].lettre = b.getText().toString();
        }
        isSetPos = true;
    }

    private void validerLettre(final boutonLettre bL, int pos, final Activity act, final View v){
        bL.b.setX(tbStructBouton[pos].posX);
        bL.b.setY(tbStructBouton[pos].posY);
        tbStructBouton[pos].ok = true;
        bL.ok = true;
        Utilities.jouerSon("ok_lettre", getApplicationContext());
        nbLettresOk++;
        if(nbLettresOk == nbChar) {
            compteur++;
            afficherToast(act, true, getResources().getString(R.string.bienJoue), "#00A000", getApplicationContext());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start(v);
                }
            }, 2500);
        }
    }

    private int getMinLettres(){
        switch (lvl){
            case 1 :
                return 3;
            case 2 :
                return 6;
            case 3 :
                return 8;
            default: break;
        }
        return 0;
    }

    private int getMaxLettres(){
        switch (lvl){
            case 1 :
                return 5;
            case 2 :
                return 8;
            case 3 :
                return 11;
            default: break;
        }
        return 0;
    }

    public void rejouer(View view) {
        view.invalidate();
        this.compteur=0;
        lvl=1;
        setContentView(R.layout.activity_start);
        idLayout = R.layout.activity_start;
    }

    public void retournerMenu(View view){
        view.invalidate();
        this.finish();
    }

    public void jouerSon(View v){
        Utilities.jouerSon(strName, getApplicationContext());
    }

    public void jouerSonRegles(View v){
        Utilities.jouerSon("ok",getApplicationContext());
    }

    public void afficheRegles(View view){
        idLayout = chargerRegles(this, view, R.string.regleAna);
    }

    public void afficheDialog(View v){
        ReglesDialog dia = new ReglesDialog();
        dia.setIdString(R.string.regleAna);
        dia.show(getFragmentManager(),"Regles");
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
