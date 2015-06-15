package be.manabu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

/**
 * Cette classe contient toutes les fonctions nécessaires au déroulement de l'exercice d'anagrammes.
 * @author Lucie Herrier - 3TL1
 */

public class AnagrammeActivity extends ActionBarActivity {

    // Constantes spécifiant le nombre d'exercices dans une série et le nombre de mots
    private final static int NBTOURS = 10;
    private final static int NBMOTS = 480;

    final Random rnd = new Random();            // Seed pour le random
    protected String strTmp;                    // Stocke à chaque tour la chaîne de caractère
    private String strName;                     // Stocke à chaque tour le nom de la chaîne
    private structBouton[] tbStructBouton;      // Tableau utilisé pour les cases blanches
    private boolean isSetPos = false;           // Permet de savoir si la position des boutons est définie
    private int nbChar;                         // Le nombre de caractères du mot à reconstituer
    private int nbLettresOk = 0;                // Le nombre de lettres placées sur les bonnes cases
    private int[] tabMotPre = new int[NBTOURS]; // Tableau de stockage des nombres déjà sortis
    private int compteur = 0;                   // Compteur de tours de la série
    private int lvl = 1;                        // Niveau de l'exercice
    private int idLayout;                       // Stockage de l'identifiant du layout en cours d'affichage
    private Niveaux niv = new Niveaux();        // Objet permettant de changer de niveau

    /**
     * Cette classe définit les attributs propres à un bouton : sa position, la lettre qu'il contient
     * et s'il est validé dans l'anagramme. Elle est utilisée pour les cases blanches.
     */
    class structBouton {
        public float posX, posY;
        public String lettre;
        public boolean ok = false;
    }

    /**
     * Cette classe représente un bouton de type lettre à placer sur les cases blanches. Il contient
     * en plus un booléen indiquant si sa position est correcte.
     */
    class boutonLettre{
        public Button b;
        public boolean ok = false;
    }

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
        TextView nom = (TextView) findViewById(R.id.nomExo);
        nom.setText(getString(R.string.ana));
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
        if (idLayout == R.layout.activity_anagramme || idLayout == R.layout.regles){
            setContentView(R.layout.activity_start);
            idLayout = R.layout.activity_start;
            TextView nom = (TextView) findViewById(R.id.nomExo);
            nom.setText(getString(R.string.ana));
            lvl = 1;
            compteur = 0;
        }
        else  this.finish();
    }

    /**
     * Cette fonction démarre un nouvel anagramme tant que la série de 10 n'est pas complétée. Si elle
     * l'est, le layout de fin est affiché.
     * @param view la vue en cours
     */
    public void start(View view) {
        if (compteur<NBTOURS) {
            nbLettresOk = 0;
            nbChar = 0;
            isSetPos = false;
            view.invalidate();
            setContentView(R.layout.activity_anagramme);
            idLayout = R.layout.activity_anagramme;
            //trouver un mot à partir d'un nombre aléatoire
            int rand;
            do {
                rand = rnd.nextInt(NBMOTS)+1;
                strName = "str_" + rand;
                strTmp = getStringResourceByName(strName, getApplicationContext());
                // vérifier que le mot répond aux différents critères de sélection
            } while (strTmp.length() > getMaxLettres() || strTmp.length() < getMinLettres() || existeMotPre(rand));
            // rajouter le nombre correspondant au mot à la liste de ce qui est déjà sorti
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
            do {
                Collections.shuffle(tbStr);
            }while (!estShuffle(tbStr, arStr));

            placerBoutons(tbBoutonLettres, arStr, tbStr);
        }
        else{
            setContentView(R.layout.activity_fin);
            idLayout = R.layout.activity_fin;
        }
    }

    /**
     * Cette fonction permet de vérifier que les lettres ont bien été mélangées de manière à ne pas
     * former un mot identique à celui de base.
     * @param tbStr La liste des caractères mélangés
     * @param arStr Le tableau de caractères du mot de base
     * @return vrai si les lettres sont mélangées, faux dans le cas contraire
     */
    private boolean estShuffle(ArrayList<Character> tbStr, char[] arStr ){
        for (int j = 0; j< nbChar; j++){
            if (! (tbStr.get(j) == arStr[j])) return true;
        }
        return false;
    }

    /**
     * Cette fonction met en place les différents boutons de l'exercice sur le layout.
     * @param tbBoutonLettres le tableau qui va contenir les boutons avec les lettres du mot
     * @param arStr le tableau avec les caractères dans l'ordre
     * @param tbStr le tableau avec les caractères mélangés
     */
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
     * Cette fonction met en place le design des boutons de type lettre
     * @param b le bouton à mettre en forme
     * @param temp le texte contenu par le bouton
     * @param i la position du bouton, qui servira d'identifiant
     * @param idBouton l'identifiant du bouton par rapport auquel il se positionne
     * @return l'identifiant attribué au bouton b
     */
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

    /**
     * Cette fonction met en place le design des boutons de type cases blanches
     * @param b le bouton à  mettre en forme
     * @param temp le texte contenu par le bouton
     * @param i la position du bouton, qui servira d'identifiant
     * @param idBouton l'identifiant du bouton par rapport auquel il se positionne
     * @return l'identifiant attribué au bouton b
     */
    private int setBoutonVerif(Button b, String temp, int i, int idBouton) {
        b.setText(temp);
        b.setTextColor(getResources().getColor(R.color.White));
        b.setTextSize(25);
        b.setId(i);
        b.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/opendyslexic.ttf"));
        b.setBackgroundResource(R.drawable.anagramme_validate);
        b.setLayoutParams(setParams(i, R.id.bReplay, idBouton, true));
        return i;

    }

    /**
     * Cette fonction définit les paramètres à mettre en place pour les objets sur le relative layout
     * @param i la position de l'objet
     * @param buttonTop le bouton se trouvant au dessus de l'objet, s'il a lieu d'être
     * @param idButton le bouton de trouvant à côté de l'objet
     * @param verif vrai s'il s'agit d'un case blanche, faux s'il s'agit d'une lettre
     * @return les paramètres du layout
     */
    private RelativeLayout.LayoutParams setParams(int i, int buttonTop, int idButton, boolean verif ){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if (i==0) {
            params.addRule(RelativeLayout.BELOW, buttonTop);
            params.addRule(RelativeLayout.ALIGN_LEFT, buttonTop);
            //params.addRule(RelativeLayout.ALIGN_START, buttonTop);
        }
        else{
                if (i==1 && verif){
                    params.addRule(RelativeLayout.BELOW,buttonTop);
                    params.addRule(RelativeLayout.RIGHT_OF, buttonTop);
                   // params.addRule(RelativeLayout.END_OF, buttonTop);
                }
            else {
                    if (verif) params.addRule(RelativeLayout.ALIGN_TOP, idButton);
                    else params.addRule(RelativeLayout.ALIGN_BOTTOM,idButton);
                    params.addRule(RelativeLayout.RIGHT_OF, idButton);
                   // params.addRule(RelativeLayout.END_OF, idButton);
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

    /**
     * Implémente le mouvement des boutons avec les lettres et leur immobilisme si elles sont sur la bonne case blanche.
     * @param bL le bouton pour lequel le mouvement est mis en place
     * @param act l'activité en cours
     */
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
                // Définition des positions de l'emplacement de la lettre pour qu'elle soit validée
                minX = (tbStructBouton[pos].posX) - 10;
                maxX = (tbStructBouton[pos].posX) + 10;
                minY = (tbStructBouton[pos].posY) - 10;
                maxY = (tbStructBouton[pos].posY) + 10;
                if (bL.b.getX() < maxX && bL.b.getX() > minX && bL.b.getY() < maxY && bL.b.getY() > minY) {
                    //Si la lettre se trouve sur la bonne case, la valider
                    validerLettre(bL, pos, act, v);
                    return true;

                } else {
                    // Dans le cas contraire, continuer le mouvement
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

    /**
     * Cette fonction permet de définir la position des boutons lettres validés sur les cases blanches.
     */
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

    /**
     * Cette fonction permet de valider qu'une lettre bien se trouve sur le case lui correspondant
     * @param bL le bouton contenant la lettre
     * @param pos la position de la lettre dans le tableau de boutons
     * @param act l'activité en cours
     * @param v la vue en cours
     */
    private void validerLettre(final boutonLettre bL, int pos, final Activity act, final View v){
        bL.b.setX(tbStructBouton[pos].posX);
        bL.b.setY(tbStructBouton[pos].posY);
        tbStructBouton[pos].ok = true;
        bL.ok = true;
        Utilities.jouerSon("ok_lettre", getApplicationContext());
        nbLettresOk++;
        if(nbLettresOk == nbChar) {
            compteur++;
            afficherToastRep(act, true, getApplicationContext());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start(v);
                }
            }, 2500);
        }
    }

    /**
     * Cette fonction permet de d'obtenir le nombre minimum de lettres dont est composé le mot selon le niveau.
     * @return le nombre minimum de lettres dans le mot
     */
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

    /**
     * Cette fonction permet de d'obtenir le nombre maximum de lettres dont est composé le mot selon le niveau.
     * @return le nombre maximum de lettres dans le mot
     */
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

    /**
     * Cette fonction permet de rejouer à l'exercice des anagrammes une fois la série finie.
     * @param view la vue en cours
     */
    public void rejouer(View view) {
        view.invalidate();
        this.compteur=0;
        lvl=1;
        setContentView(R.layout.activity_start);
        idLayout = R.layout.activity_start;
        TextView nom = (TextView) findViewById(R.id.nomExo);
        nom.setText(getString(R.string.ana));
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
     * Cette fonction permet de jouer le son du mot à reconstituer pendant l'exercice.
     * @param v la vue en cours
     */
    public void jouerSon(View v){
        Utilities.jouerSon(strName, getApplicationContext());
    }

    /**
     * Cette fonction permet d'écouter les règles du jeu des anagrammes
     * @param v la vue en cours
     */
    public void jouerSonRegles(View v){
        Utilities.jouerSon("r_ana",getApplicationContext());
    }

    /**
     * Cette fonction permet d'afficher les règles du jeu des anagrammes.
     * @param view la vue en cours
     */
    public void afficheRegles(View view){
        idLayout = chargerRegles(this, view, R.string.regleAna);
    }

    /**
     * Cette fonction permet d'afficher un dialog en cours d'exercice pour rappeler les règles du jeu.
     * @param v la vue en cours
     */
    public void afficheDialog(View v){
        ReglesDialog dia = new ReglesDialog();
        dia.setIdString(R.string.regleAna);
        dia.show(getFragmentManager(),"Regles");
    }

    /**
     * Cette fonction permet de définir le niveau lors du touch sur la première étoile comme étant le 1.
     * @param view la vue en cours
     */
    public void changeLvl1(View view){ lvl=niv.changeLvl1(this);}

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
    public void back(View view){
        idLayout = revenirDebut(this, view);
        TextView nom = (TextView) findViewById(R.id.nomExo);
        nom.setText(getString(R.string.ana));}
}
