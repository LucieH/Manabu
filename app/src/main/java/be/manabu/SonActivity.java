package be.manabu;

/**
 * @author Lucie Herrier - 3TL1
 */

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

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

public class SonActivity extends ActionBarActivity {
    final Random rnd = new Random();
    final static private int NB_SONS = 34;
    final static private int NB_MOTS = 481;
    private final static int NB_TOURS = 10;
    private int compteur = 0;
    private int lvl = 1;
    private Niveaux niv = new Niveaux();
    private int idLayout = 0;
    private int[] indexSons;
    private int[] tabSonPre;
    private ArrayList<String> listeSons;
    private String son;

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
            lvl = 1;
            compteur = 0;
        }
        else  this.finish();
    }

    /**
     *
     * @param view
     */
    public void start(View view) {
        indexSons = new int[NB_SONS];
        tabSonPre = new int[NB_TOURS];
        listeSons = new ArrayList<String>();
        lireFichier();
        startSon(view);
    }

    /**
     *
     * @param view
     */
    private void startSon(View view){
        view.invalidate();
        if (compteur < NB_TOURS) {
            setContentView(R.layout.activity_son);
            idLayout = R.layout.activity_son;
            int rand;
            do {
                rand = rnd.nextInt(NB_SONS);
            } while (existeSonPre(rand) || findMotSon(rand));
            son = "son_"+rand+"";
            jouerSon(son, getApplicationContext());
            tabSonPre[compteur] = rand;
            compteur++;
        }
        else {
            setContentView(R.layout.activity_img_fin);
            idLayout = R.layout.activity_img_fin;
        }
    }

    /**
     *
     */
    private void lireFichier(){
        String fich = "";
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
        String code = "11";
        int i = 0;
        //Essai de lecture de fichier
        try {
            InputStream ips = getAssets().open(fich);//new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                listeSons.add(ligne.substring(2, ligne.length()).trim());
                if (!ligne.startsWith(code)) {
                    code = ligne.substring(0, 2);
                    indexSons[i] = listeSons.size() - 1;
                    i++;
                }
            }
            br.close();
            ipsr.close();
            ips.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     *
     * @param pos
     * @return
     */
    private boolean findMotSon(int pos){
        int nbMots = -1;
        if (pos == NB_SONS-1) nbMots = listeSons.size()-indexSons[pos];
        else{
            nbMots = indexSons[pos+1]-indexSons[pos];
        }
        if(nbMots == 1 && listeSons.get(indexSons[pos]).equals("0")) return true;
        else {
            int randString = rnd.nextInt(nbMots) + indexSons[pos];
            String ok = getStringResourceByName("str_" + listeSons.get(randString) + "", getApplicationContext());

            do {
                randString = rnd.nextInt(NB_MOTS);
            } while (existeMot(indexSons[pos], indexSons[pos]+nbMots, randString));
            String ko1 = getStringResourceByName("str_" + randString + "", getApplicationContext());

            do {
                randString = rnd.nextInt(NB_MOTS);
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
     *
     * @param start
     * @param end
     * @param nbr
     * @return
     */
    private boolean existeMot(int start, int end, int nbr){
        String nbrS = ""+nbr+"";
        for (int i = start; i< end; i++){
            if (listeSons.get(i).equals(nbrS)) return true;
        }
        return false;
    }

    /**
     * Cette fonction verifie si le son a deja ete joue pendant la serie de 10
     * @param rand
     * @return
     */
    private boolean existeSonPre(int rand){
        for(int i=0; i<compteur; i++){
            if (tabSonPre[i] == rand) return true;
        }
        return false;
    }

    /**
     *
     * @param b1
     * @param b2
     * @param b3
     * @param ok
     * @param ko1
     * @param ko2
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
     *
     * @param b
     * @param ok
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
     *
     * @param a
     * @param ko1
     * @param b
     * @param ko2
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
     *
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
     *
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
        Utilities.jouerSon("ok", getApplicationContext());
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
    public void back(View view){ revenirDebut(this, view);}
}
