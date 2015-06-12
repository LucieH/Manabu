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
 * Cette classe contient toutes les fonctions nécessaires au déroulement de l'exercice de lecture flash.
 * @author Lucie Herrier - 3TL1
 */

public class FlashActivity extends ActionBarActivity {

    // Constantes spécifiant le nombre d'exercices dans une série et le nombre de mots par niveau
    private final static int NBTOURS = 10;
    private final static int NBMOTS = 480;      // LVL 1
    private final static int NBMOTS_2 = 50;     // LVL 2
    private final static int NBMOTS_3 = 50;     // LVL 3

    private final Random rnd = new Random();    // Seed pour le random
    protected String strTmp;                    // La chaîne de caratères à reconstituer
    private int nbEssai;                        // Nombre d'essais d'écriture du mot / de la phrase
    private int secondes=10;                    // Durée d'affichage par défaut en secondes
    private int[] tabMotPre;                    // Tableau de stockage des nombres déjà sortis
    private int compteur = 0;                   // Compteur de tours de la série
    private int lvl = 1;                        // Niveau de l'exercice
    private int idLayout;                       // Stockage de l'identifiant du layout en cours d'affichage
    private Niveaux niv = new Niveaux();        // Objet permettant de changer de niveau
    protected CustomKeyboard mCustomKeyboard;   // Clavier personnalisé

    /**
     * Cette fonction est exécutée par défaut lors du démarrage de l'activité.
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/opendyslexic.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start);
        idLayout = R.layout.activity_start;
    }

    /**
     * Fonction utilisée lors de la création de l'activité.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    /**
     * Fonction utilisée lors de la création de l'activité.
     */
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
     * Cette fonction permet d'éviter une erreur plantant l'application lors du touch d'un bouton de
     * menu sur un smartphone ou une tablette.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // Ne rien faire pour éviter le bug
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Cette fonction redéfinit le comportement de l'activité lorsque la touche "back" a été pressée,
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
     * Cette fonction permet de démarrer le jeu de lecture flash une fois le nombre de seconde défini.
     * @param view la vue en cours
     */
    public void startFlash(View view) {
        if(compteur < NBTOURS){
            String str = "str_";
            int rand;
            int nbmots = NBMOTS;
            // Regarde le niveau choisi pour déterminer les valeurs des variables à utiliser
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
            // Génération du nombre aléatoire
            do {
                rand = rnd.nextInt(nbmots)+1;
            }while (existeMotPre(rand));
            // Définition de la chaîne de caractères utilisée pour ce tour
            strTmp=getStringResourceByName(str + rand + "",getApplicationContext());
            // Mémorisation que ce nombre est déjà sorti
            tabMotPre[compteur] = rand;
            // Affiche le mot pendant le nombre de secondes définit auparavant, puis le layout d'écriture du mot
            voirMot(view);
            compteur ++;
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
     * Cette fonction permet de vérifier si le mot écrit par l'enfant est correct.
     * @param b le bouton sur lequel il faut cliquer pour vérifier le mot
     */
    protected void verifierReponse(final Button b){
        this.nbEssai = 0;
        final Activity act = this;
        final Button aide = (Button) findViewById(R.id.AideFlash);
        // Mise en place du listener du clic sur le bouton
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                if( mCustomKeyboard.isCustomKeyboardVisible() ) mCustomKeyboard.hideCustomKeyboard();
                EditText text = (EditText) findViewById(R.id.Answflash);
                String tmp = text.getText().toString().trim();
                final Handler handler = new Handler();
                // Si la chaîne est identique à ce qui a été montré, afficher le toast correct, bloquer les boutons, puis relancer l'exercice
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
                // Si la chaîne est différente, afficher le toast de mauvaise réponse et bloquer les boutons pendant ce temps.
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
                // Augmenter le nombre d'essais pour afficher le bouton de relecture si le premier a été infructueux.
                nbEssai++;
                if (nbEssai >= 1) {
                    aide.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Cette fonction permet de voir le mot à mémoriser pendant un temps défini puis de passer à la vérification.
     * @param v la vue en cours
     */
    public void voirMot(View v){
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
     * Cette fonction permet d'afficher le mot à lire.
     * @param view la vue en cours
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
     * Cette fonction permet de mettre en place le layout de réécriture du mot.
     */
    private void setViewAnswer(){
        // Affichage du layout
        setContentView(R.layout.activity_flash_answer);
        idLayout = R.layout.activity_flash_answer;
        // Ancrage du clavier
        mCustomKeyboard= new CustomKeyboard(getApplicationContext(),this, R.id.keyboardview, R.xml.alphabet_keyboard);
        mCustomKeyboard.registerEditText(R.id.Answflash);
        // Mise en place du listener sur le bouton de vérification
        Button b=(Button) findViewById(R.id.VerifFlash);
        verifierReponse(b);
    }


    /**
     * Cette fonction permet de décrémenter le Number Picker personnalisé. Dans le cas présent, il s'agit
     * de diminuer le nombre de secondes d'affichage.
     * @param view la vue en cours
     */
    public void decrementer(View view){
        if (secondes >1) {
            this.secondes--;
            TextView nbSec = (TextView) findViewById(R.id.TVSecondesFlash);
            nbSec.setText(""+secondes+"");
        }

    }

    /**
     * Cette fonction permet d'incrémenter le Number Picker personnalisé. Dans le cas présent, il s'agit
     * d'augmenter le nombre de secondes d'affichage.
     * @param view la vue en cours
     */
    public void incrementer(View view){
        if (secondes <20) {
            this.secondes++;
            TextView nbSec = (TextView) findViewById(R.id.TVSecondesFlash);
            nbSec.setText(""+secondes+"");
        }
    }

    /**
     * Cette fonction permet de rejouer à l'exercice de lecture flash une fois la série finie.
     * @param view la vue en cours
     */
    public void rejouer(View view) {
        view.invalidate();
        compteur=0;
        lvl = 1;
        setContentView(R.layout.activity_start);
        idLayout = R.layout.activity_start;
    }

    /**
     * Cette fonction permet de revenir au menu principal une fois la série finie.
     * @param view la vue en cours
     */
    public void retournerMenu(View view){
        view.invalidate();
        this.finish();
    }
    /**
     * Cette fonction permet d'afficher les règles du jeu de lecture flash.
     * @param view la vue en cours
     */
    public void afficheRegles(View view){
        idLayout = chargerRegles(this, view, R.string.regleFlash);
    }

    /**
     * Cette fonction permet d'afficher un dialog en cours d'exercice pour rappeler les règles du jeu.
     * @param v la vue en cours
     */
    public void afficheDialog(View v){
        ReglesDialog dia = new ReglesDialog();
        dia.setIdString(R.string.regleFlash);
        dia.show(getFragmentManager(),"Regles");
    }

    /**
     * Cette fonction permet d'écouter les règles du jeu de lecture flash
     * @param v la vue en cours
     */
    public void jouerSonRegles(View v){
        Utilities.jouerSon("r_flash",getApplicationContext());
    }

    /**
     * Cette fonction permet de définir le niveau lors du clic sur la première étoile comme étant le 1.
     * @param view la vue en cours
     */
    public void changeLvl1(View view){
        lvl=niv.changeLvl1(this);

    }

    /**
     * Cette fonction permet de définir le niveau lors du clic sur la seconde étoile. Celui-ci deviendra 2 ou 1.
     * @param view la vue en cours
     */
    public void changeLvl2(View view){
        lvl=niv.changeLvl2(this, lvl);
    }

    /**
     * Cette fonction permet de définir le niveau lors du clic sur la troisième étoile. Celui-ci deviendra 3 ou 2.
     * @param view la vue en cours
     */
    public void changeLvl3(View view){
        lvl=niv.changeLvl3(this, lvl);
    }

    /**
     * Cette fonction s'exécute lors du clic sur le bouton "revenir" de l'affichage des règles.
     * @param view la vue en cours
     */
    public void back(View view){ revenirDebut(this, view);}


}
