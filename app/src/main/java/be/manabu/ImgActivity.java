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
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

/**
 * Cette classe contient toutes les fonctions nécessaires au déroulement de l'exercice d'imagerie.
 * @author Lucie Herrier - 3TL1
 */

public class ImgActivity extends ActionBarActivity {

    // Constantes spécifiant le nombre d'images par niveau et le nombre d'exercices dans une série
    private final static int NB_IMAGES = 21;        // LVL 1
    private final static int NB_IMAGES2 = 10;       // LVL 2
    private final static int NB_TOURS = 10;

    private final Random rnd = new Random();        // Seed pour le random
    protected String strTmp;                        // Stocke à chaque tour le nom de l'image
    private int tabNbImages[] = new int[NB_TOURS];  // Tableau de stockage des nombres déjà sortis
    private int cmptImages = 0;                     // Compteur de tours de la série
    private int lvl = 1;                            // Niveau de l'exercice
    private int idLayout;                           // Stockage de l'identifiant du layout en cours d'affichage
    private Niveaux niv = new Niveaux();            // Objet permettant de changer de niveau


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
        // Les deux lignes ci-dessous permettent de bloquer l'acces au niveau 3
        ImageView lvl3 = (ImageView) findViewById(R.id.EtoileLvl3);
        lvl3.setVisibility(View.GONE);
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
            // ...
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
        if (idLayout == R.layout.activity_img || idLayout == R.layout.activity_img_choix || idLayout == R.layout.regles){
            setContentView(R.layout.activity_start);
            idLayout = R.layout.activity_start;
            // Les deux lignes ci-dessous permettent de bloquer l'acces au niveau 3
            ImageView lvl3 = (ImageView) findViewById(R.id.EtoileLvl3);
            lvl3.setVisibility(View.GONE);
            lvl = 1;
            cmptImages = 0;
        }
        else  this.finish();
    }

    /**
     * Cette fonction permet de démarrer le jeu imagerie. Si la série est toujours en cours, un exercice
     * est lancé, dans le cas contraire, l'écran de fin est affiché.
     * @param view la vue en cours
     */
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

    /**
     * Cette fonction génère un nombre au hasard parmi le nombre d'images du niveau et prend l'image
     * correspondante pour l'afficher pour l'exercice.
     */
    protected void randomImg(){
        final ImageView img = (ImageView) findViewById(R.id.imgRandom);
        String str = "";
        int rand;
        int nbimg = 0;
        // Définir les variables selon le niveau
        switch (lvl){
            case 1 :
                str = "img_";
                nbimg = NB_IMAGES;
                break;
            case 2 :
                str = "img2_";
                nbimg = NB_IMAGES2;
                break;
            default:
                break;
        }
        // Générer le nombre au hasard.
        do {
            rand = rnd.nextInt(nbimg);
        }while (existeImageAffichee(rand));
        strTmp = str + rand+ "";
        // Afficher l'image
        img.setImageDrawable
                (
                        getResources().getDrawable(getResourceID(strTmp, "drawable",
                                getApplicationContext()))
                );
        // Afficher la chaîne de caractères correspondant à l'image
        final TextView tv = (TextView) findViewById(R.id.tv1);
        tv.setText(getStringResourceByName(strTmp, getApplicationContext()));
        // Mémoriser que cette image est déjà sortie
        this.tabNbImages[cmptImages]=rand;
        this.cmptImages++;
    }

    /**
     *  Cette fonction afficher les différents choix de réponse pour un exercice (un correct et deux faux).
     * @param view la vue en cours
     */
    public void afficherChoix(View view) {
        view.invalidate();
        setContentView(R.layout.activity_img_choix);
        idLayout =  R.layout.activity_img_choix;
        // Afficher l'image
        final ImageView img = (ImageView) findViewById(R.id.imgRandom);
        img.setImageDrawable
                (
                        getResources().getDrawable(getResourceID(strTmp, "drawable",
                                getApplicationContext()))
                );
        // Récupérer les boutons depuis le layout
        final Button b1 = (Button) findViewById(R.id.choix1);
        final Button b2 = (Button) findViewById(R.id.choix2);
        final Button b3 = (Button) findViewById(R.id.choix3);
        // Changer l'orientation des boutons pour le niveau 2
        if (lvl == 2) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.imgChBtns);
            ll.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,0,10);
            b1.setLayoutParams(lp);
            b2.setLayoutParams(lp);
            b3.setLayoutParams(lp);
            b1.setPadding(0,0,0,0);
        }
        // Définir l'ordre des boutons au hasard
        creerBoutonRandom(b1, b2, b3);
    }

    /**
     * Cette fonction vérifie si l'image a déjà été jouée pendant la série de 10 actuelle.
     * @param rand le nombre généré aléatoirement ce tour-ci
     * @return true si le nombre est déjà sorti, false dans le cas contraire
     */
    private boolean existeImageAffichee(int rand){
        for(int i=0; i<cmptImages; i++){
            if (tabNbImages[i] == rand) return true;
        }
        return false;
    }

    /**
     * Cette fonction permet de placer le bouton correct et les deux boutons incorrects dans un ordre
     * aléatoire sur le layout.
     * @param b1 le premier bouton
     * @param b2 le deuxième bouton
     * @param b3 le troisième bouton
     */
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

    /**
     * Cette fonction permet de définir un bouton comme étant celui qui contient la bonne réponse.
     * @param b un bouton
     */
    protected void setBonneReponse(Button b){
        final Activity act = this;
        b.setText(getStringResourceByName(strTmp, getApplicationContext()));
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                afficherToastRep(act, true, getApplicationContext());
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

    /**
     * Cette fonction permet de définir deux boutons comme étant ceux qui contiennent les mauvaises réponses.
     * @param a un premier bouton
     * @param b un second bouton
     */
    protected void setMauvaiseReponse(final Button a, final Button b){
        final Activity act = this;
        a.setText(getStringResourceByName(strTmp+"_1",getApplicationContext()));
        b.setText(getStringResourceByName(strTmp + "_2", getApplicationContext()));
        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                afficherToastRep(act, false, getApplicationContext());
                disableButtons();
                reEnableButtons();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                afficherToastRep(act, false, getApplicationContext());
                disableButtons();
                reEnableButtons();
            }
        });
    }

    /**
     * Cette fonction permet de désactiver tous les boutons du layout de réponse d'imagerie
     */
    private void disableButtons(){
        Button a = (Button) findViewById(R.id.choix1);
        Button b = (Button) findViewById(R.id.choix2);
        Button c = (Button) findViewById(R.id.choix3);
        a.setEnabled(false);
        b.setEnabled(false);
        c.setEnabled(false);
    }

    /**
     * Cette fonction permet de ré-activer tous les boutons du layout de réponse d'imagerie
     */
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

    /**
     * Cette fonction permet de rejouer à l'exercice d'imagerie une fois la série finie.
     * @param view la vue en cours
     */
    public void rejouer(View view) {
        view.invalidate();
        cmptImages=0;
        lvl = 1;
        setContentView(R.layout.activity_start);
        idLayout = R.layout.activity_start;
        // Les deux lignes ci-dessous permettent de bloquer l'acces au niveau 3
        ImageView lvl3 = (ImageView) findViewById(R.id.EtoileLvl3);
        lvl3.setVisibility(View.GONE);
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
     * Cette fonction permet d'afficher les règles du jeu d'imagerie.
     * @param view la vue en cours
     */
    public void afficheRegles(View view){
        idLayout = chargerRegles(this, view, R.string.regleImg);
    }

    /**
     * Cette fonction permet d'afficher un dialog en cours d'exercice pour rappeler les règles du jeu.
     * @param v la vue en cours
     */
    public void afficheDialog(View v){
        ReglesDialog dia = new ReglesDialog();
        dia.setIdString(R.string.regleImg);
        dia.show(getFragmentManager(),"Regles");
    }

    /**
     * Cette fonction permet d'écouter les règles du jeu d'imagerie
     * @param v la vue en cours
     */
    public void jouerSonRegles(View v){
        Utilities.jouerSon("r_img",getApplicationContext());
    }

    /**
     * Cette fonction permet de définir le niveau lors du touch sur la première étoile comme étant le 1.
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


