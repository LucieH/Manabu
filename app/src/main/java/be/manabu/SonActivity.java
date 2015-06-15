package be.manabu;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

/**
 * Cette classe contient toutes les fonctions nécessaires au déroulement de l'exercice d'écoute du son
 * @author Lucie Herrier - 3TL1
 */

public class SonActivity extends ActionBarActivity {

    // Constantes spécifiant le nombre de sons par niveau, le nombre de mots et le nombre d'exercices dans une série
    final static private int NB_SONS = 34;
    final static private int NB_MOTS = 480;
    private final static int NB_TOURS = 10;

    final Random rnd = new Random();        // Seed pour le random
    private String son;                     // Chaîne contenant le nom du son pour chaque tour
    private ArrayList<String> listeSons;    // Liste des références pour les différents sons
    private int compteur = 0;               // Compteur de tours de la série
    private int[] indexSons;                // Tableau indexant la liste des sons par son
    private int[] tabSonPre;                // Tableau de stockage des nombres déjà sortis
    private int lvl = 1;                    // Niveau de l'exercice
    private int idLayout;                   // Stockage de l'identifiant du layout en cours d'affichage
    private Niveaux niv = new Niveaux();    // Objet permettant de changer de niveau

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
        nom.setText(getString(R.string.son));
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
        if (idLayout == R.layout.activity_son || idLayout == R.layout.regles){
            setContentView(R.layout.activity_start);
            idLayout = R.layout.activity_start;
            TextView nom = (TextView) findViewById(R.id.nomExo);
            nom.setText(getString(R.string.son));
            lvl = 1;
            compteur = 0;
        }
        else  this.finish();
    }

    /**
     * Cette fonction permet de démarrer l'exercice d'écoute du son
     * @param view la vue en cours
     */
    public void start(View view) {
        indexSons = new int[NB_SONS];
        tabSonPre = new int[NB_TOURS];
        listeSons = new ArrayList<String>();
        lireFichier();
        startSon(view);
    }

    /**
     * Cette fonction charge le fichier contenant les références des mots en fonction des sons qu'ils
     * contiennent en mémoire. Ce fichier est différent selon le niveau de l'exercice.
     */
    private void lireFichier(){
        String fich = "";
        // Définir quel fichier il faut charger selon le niveau de l'exercice
        switch (lvl){
            case 1 :
                fich = "start_son.txt";
                break;
            case 2 :
                fich = "mid_son.txt";
                break;
            case 3 :
                fich = "all_son.txt";
                break;
            default:
                break;
        }
        // Définir un code de son inexistant
        String code = "11";
        int i = 0;
        // Lecture du fichier
        try {
            // Ouvrir le fichier
            InputStream ips = getAssets().open(fich);//new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            /* Pour chaque ligne, ajouter la référence de la chaîne dans la liste des références, et
            * éventuellement, si le code est différent de celui stocké auparavant, prendre ce code et
            * le rajouter à la liste des codes indexés.
            */
            while ((ligne = br.readLine()) != null) {
                listeSons.add(ligne.substring(2, ligne.length()).trim());
                if (!ligne.startsWith(code)) {
                    code = ligne.substring(0, 2);
                    indexSons[i] = listeSons.size() - 1;
                    i++;
                }
            }
            // Fermer le fichier
            br.close();
            ipsr.close();
            ips.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Cette fonction permet de jouer l'exercice. Si la série n'est pas terminée, la fonction définit
     * le son à jouer ainsi que les mots formant l'exercice en cours. Dans le cas contraire, la fonction
     * passe à la fin.
     * @param view la vue en cours
     */
    private void startSon(View view){
        view.invalidate();
        if (compteur < NB_TOURS) {
            setContentView(R.layout.activity_son);
            idLayout = R.layout.activity_son;
            // Générer le nombre aléatoire
            int rand;
            do {
                rand = rnd.nextInt(NB_SONS);
            } while (existeSonPre(rand) || findMotsSon(rand));
            // Jouer le son à retrouver dans le mot
            son = "son_"+rand+"";
            jouerSon(son, getApplicationContext());
            // Ajouter la référence du son à la liste de ce qui est déjà sorti
            tabSonPre[compteur] = rand;
            compteur++;
        }
        else {
            setContentView(R.layout.activity_fin);
            idLayout = R.layout.activity_fin;
        }
    }

    /**
     * Cette fonction permet de générer les trois boutons de l'exercice : un correct contenant le son
     * qui est joué, et deux incorrects ne contenant pas ce son.
     * @param pos la position du son dans le tableau d'index
     * @return vrai s'il n'existe pas de mots connus pour ce son, faux dans le cas contraire
     */
    private boolean findMotsSon(int pos){
        int nbMots;
        // Calcul du nombre de références de mots existants contenant le son
        if (pos == NB_SONS-1) nbMots = listeSons.size()-indexSons[pos];
        else{
            nbMots = indexSons[pos+1]-indexSons[pos];
        }
        // Si n'y a qu'un mot et que sa référence est 0, cela signifie qu'il n'y a pas de mot avec ce son
        if(nbMots == 1 && listeSons.get(indexSons[pos]).equals("0")) return true;
        // Dans le cas contraire, on définit les boutons
        else {
            // La chaîne pour le bouton correct
            int randString = rnd.nextInt(nbMots) + indexSons[pos];
            String ok = getStringResourceByName("str_" + listeSons.get(randString) + "", getApplicationContext());

            // La chaîne pour les deux boutons incorrects
            do {
                randString = rnd.nextInt(NB_MOTS)+1;
            } while (existeMot(indexSons[pos], indexSons[pos]+nbMots, randString));
            String ko1 = getStringResourceByName("str_" + randString + "", getApplicationContext());

            do {
                randString = rnd.nextInt(NB_MOTS)+1;
            } while (existeMot(indexSons[pos], indexSons[pos]+nbMots, randString));
            String ko2 = getStringResourceByName("str_" + randString + "", getApplicationContext());

            final Button b1 = (Button) findViewById(R.id.bSons1);
            final Button b2 = (Button) findViewById(R.id.bSons2);
            final Button b3 = (Button) findViewById(R.id.bSons3);
            creerBoutonRandom(b1, b2, b3, ok, ko1, ko2);
        }
        return false;
    }

    /**
     * Cette fonction vérifie si une référence donnée existe parmi la liste des références entre deux
     * bornes correspondant au début et à la fin d'un son particulier.
     * @param start la borne de début
     * @param end la borne de fin
     * @param nbr la référence recherchée
     * @return vrai si la référence a été trouvée, faux dans le cas contraire
     */
    private boolean existeMot(int start, int end, int nbr){
        String nbrS = ""+nbr+"";
        for (int i = start; i< end; i++){
            if (listeSons.get(i).equals(nbrS)) return true;
        }
        return false;
    }

    /**
     * Cette fonction vérifie si le son a déjà été joué pendant la série de 10 actuelle.
     * @param rand le nombre généré aléatoirement ce tour-ci
     * @return true si le nombre est déjà sorti, false dans le cas contraire
     */
    private boolean existeSonPre(int rand){
        for(int i=0; i<compteur; i++){
            if (tabSonPre[i] == rand) return true;
        }
        return false;
    }

    /**
     * Cette fonction permet de créer les boutons de choix entre les mots (1 vrai et 2 faux) dans un
     * ordre aléatoire.
     * @param b1 Le premier bouton
     * @param b2 Le deuxième bouton
     * @param b3 Le troisième bouton
     * @param ok La chaîne de caractères contenue dans le bouton correct
     * @param ko1 Une chaîne de caractères contenue dans un bouton incorrect
     * @param ko2 Une chaîne de caractères contenue dans un bouton incorrect
     */
    protected void creerBoutonRandom(Button b1, Button b2, Button b3, String ok, String ko1, String ko2){
        int i = rnd.nextInt(3);
        int j = rnd.nextInt(2);
        Button a = null;
        Button b = null;
        switch(i){
            case 0:
                setBonneReponse(b1, ok);
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
                setMauvaiseReponse(a, ko1, b, ko2);
                break;
            case 1:
                setBonneReponse(b2, ok);
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
                setMauvaiseReponse(a, ko1, b, ko2);
                break;
            case 2:
                setBonneReponse(b3, ok);
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
                setMauvaiseReponse(a, ko1, b, ko2);
                break;
        }
    }

    /**
     * Cette fonction permet de définir un bouton comme étant celui qui contient la bonne réponse.
     * @param b un bouton
     * @param ok une chaîne de caractères (contenant la bonne réponse)
     */
    protected void setBonneReponse(Button b, String ok){
        final Activity act = this;
        b.setText(ok);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                afficherToastRep(act, true, getApplicationContext());
                disableButtons();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startSon(v);
                    }
                }, 2500);
            }
        });
    }

    /**
     * Cette fonction permet de définir deux boutons comme étant ceux qui contiennent les mauvaises réponses.
     * @param a un premier bouton
     * @param ko1 une chaîne de caractères (incorrecte)
     * @param b un second bouton
     * @param ko2 une chaîne de caractères (incorrecte)
     */
    protected void setMauvaiseReponse(final Button a, String ko1, final Button b, String ko2){
        final Activity act = this;
        a.setText(ko1);
        b.setText(ko2);
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
     * Cette fonction permet de désactiver tous les boutons du layout sons
     */
    private void disableButtons(){
        Button a = (Button) findViewById(R.id.bSons1);
        Button b = (Button) findViewById(R.id.bSons2);
        Button c = (Button) findViewById(R.id.bSons3);
        a.setEnabled(false);
        b.setEnabled(false);
        c.setEnabled(false);
    }

    /**
     * Cette fonction permet de ré-activer tous les boutons du layout sons
     */
    private void reEnableButtons(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Button a = (Button) findViewById(R.id.bSons1);
                Button b = (Button) findViewById(R.id.bSons2);
                Button c = (Button) findViewById(R.id.bSons3);
                a.setEnabled(true);
                b.setEnabled(true);
                c.setEnabled(true);
            }
        }, 2500);
    }

    /**
     * Cette fonction permet d'afficher les règles du jeu d'écoute du son.
     * @param view la vue en cours
     */
    public void afficheRegles(View view){
        idLayout = chargerRegles(this, view, R.string.regleSon);
    }

    /**
     * Cette fonction permet d'afficher un dialog en cours d'exercice pour rappeler les règles du jeu.
     * @param v la vue en cours
     */
    public void afficheDialog(View v){
        ReglesDialog dia = new ReglesDialog();
        dia.setIdString(R.string.regleSon);
        dia.show(getFragmentManager(),"Regles");
    }

    /**
     * Cette fonction permet d'écouter les règles du jeu d'écoute du son
     * @param v la vue en cours
     */
    public void jouerSonRegles(View v){
        Utilities.jouerSon("r_son", getApplicationContext());
    }

    /**
     * Cette fonction permet de rejouer le son à retrouver dans le mot.
     * @param v la vue en cours
     */
    public void rejouerSon(View v){
        Utilities.jouerSon(son, getApplicationContext());
    }

    /**
     * Cette fonction permet de rejouer à l'exercice d'écoute du son une fois la série finie.
     * @param view la vue en cours
     */
    public void rejouer(View view) {
        view.invalidate();
        compteur=0;
        lvl = 1;
        setContentView(R.layout.activity_start);
        idLayout = R.layout.activity_start;
        TextView nom = (TextView) findViewById(R.id.nomExo);
        nom.setText(getString(R.string.son));
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
    public void back(View view){
        idLayout = revenirDebut(this, view);
        TextView nom = (TextView) findViewById(R.id.nomExo);
        nom.setText(getString(R.string.son));}
}
