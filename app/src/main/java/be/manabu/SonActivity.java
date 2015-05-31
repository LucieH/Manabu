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

public class SonActivity extends ActionBarActivity {
    final Random rnd = new Random();
    final static private int NB_SONS = 33;
    final static private int NB_MOTS = 481;
    private final static int NB_TOURS = 10;
    private int compteur = 0;
    private int lvl = 1;
    private Niveaux niv = new Niveaux();
    private int idLayout = 0;
    private int[] indexSons = new int[NB_SONS];
    private ArrayList<String> listeSons = new ArrayList<String>();
    private boolean fichierLu = false;

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
        if (compteur < NB_TOURS) {
            if (!fichierLu) {
                String code = "11";
                int i = 0;
                //Essai de lecture de fichier
                try {
                    InputStream ips = getAssets().open("start_son.txt");//new FileInputStream(fichier);
                    InputStreamReader ipsr = new InputStreamReader(ips);
                    BufferedReader br = new BufferedReader(ipsr);
                    // br.
                    String ligne;
                    while ((ligne = br.readLine()) != null) {
                        listeSons.add(ligne.substring(2, ligne.length()).trim());
                        if (!ligne.startsWith(code)) {
                            code = ligne.substring(0, 2);
                            indexSons[i] = listeSons.size() - 1;
                            i++;
                        }
                        //chaine+=ligne+"\n";
                    }
                    br.close();
                    ipsr.close();
                    ips.close();
                    fichierLu = true;
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
            view.invalidate();
            setContentView(R.layout.activity_son);
            idLayout = R.layout.activity_son;
            int rand = rnd.nextInt(NB_SONS);
            setChoixLvl1(rand);
        }
        else {
            setContentView(R.layout.activity_img_fin);
            idLayout = R.layout.activity_img_fin;
        }
    }

    private void setChoixLvl1(int rand){
        /*TextView tvSon = (TextView) findViewById(R.id.TVSonIS);
        String code = "";
        switch (rand){
            case 0 :
                // son AA
                code = "AA";
                break;
            case 1 :
                // son AU
                code = "AU";
                break;
            case 2 :
                // son BB
                code = "BB";
                break;
            case 3 :
                // son CH
                code = "CH";
                break;
            case 4 :
                // son DD
                code = "DD";
                break;
            case 5 :
                // son EA
                code = "EA";
                break;
            case 6 :
                // son EE
                code = "EE";
                break;
            case 7 :
                // son EG
                code = "EG";
                break;
            case 8 :
                // son EN
                code = "EN";
                break;
            case 9 :
                // son EU
                code = "EU";
                break;
            case 10 :
                code = "FF";
                // son FF
                break;
            case 11 :
                // son GG
                code = "GG";
                break;
            case 12 :
                // son GR
                code = "GR";
                break;
            case 13 :
                // son II
                code = "II";
                break;
            case 14 :
                code = "JJ";
                // son JJ
                break;
            case 15 :
                code = "KK";
                // son KK
                break;
            case 16 :
                // son KR
                code = "KR";
                break;
            case 17 :
                // son LL
                code = "LL";
                break;
            case 18 :
                // son MM
                code = "MM";
                break;
            case 19 :
                // son NN
                code = "NN";
                break;
            case 20 :
                // son OI
                code = "OI";
                break;
            case 21 :
                // son ON
                code = "ON";
                break;
            case 22 :
                // son OO
                code = "OO";
                break;
            case 23 :
                // son OU
                code = "OU";
                break;
            case 24 :
                // son PP
                code = "PP";
                break;
            case 25 :
                // son PR
                code = "PR";
                break;
            case 26 :
                //le son RR
                code = "RR";
                break;
            case 27 :
                //le son SS
                code = "SS";
                break;
            case 28:
                // le son TT
                code = "TT";
                break;
            case 29:
                // le son UN
                code = "UN";
                break;
            case 30:
                // le son UU
                code = "UU";
                break;
            case 31:
                // le son VV
                code = "VV";
                break;
            case 32:
                // le son ZZ
                code = "ZZ";
                break;
            default:
                break;
        }*/
        findMotSon(rand);
        //tvSon.setText("Le son est : " + code);
    }

    private void findMotSon(int pos){
        int nbMots = -1;
        if (pos == NB_SONS-1) nbMots = listeSons.size()-indexSons[pos];
        else{
            nbMots = indexSons[pos+1]-indexSons[pos];
        }
        int randString = rnd.nextInt(nbMots) + indexSons[pos];
        String ok = getStringResourceByName("str_"+ listeSons.get(randString)+"",getApplicationContext());

        do{
            randString = rnd.nextInt(NB_MOTS);
        } while (existeMot(indexSons[pos], indexSons[pos+1], randString));
        String ko1 = getStringResourceByName("str_"+randString+"",getApplicationContext());

        do{
            randString = rnd.nextInt(NB_MOTS);
        } while (existeMot(indexSons[pos], indexSons[pos+1], randString));
        String ko2 = getStringResourceByName("str_"+randString+"",getApplicationContext());

        final Button b1 = (Button) findViewById(R.id.bSons1);
        final Button b2 = (Button) findViewById(R.id.bSons2);
        final Button b3 = (Button) findViewById(R.id.bSons3);
        creerBoutonRandom(b1, b2, b3, ok, ko1, ko2);
        compteur++;
    }

    private boolean existeMot(int start, int end, int nbr){
        String nbrS = ""+nbr+"";
        for (int i = start; i< end; i++){
            if (listeSons.get(start).equals(nbrS)) return true;
        }
        return false;
    }

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

    protected void setBonneReponse(Button b, String ok){
        final Activity act = this;
        b.setText(ok);
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

    protected void setMauvaiseReponse(final Button a, String ko1, final Button b, String ko2){
        final Activity act = this;
        a.setText(ko1);
        b.setText(ko2);
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
        Button a = (Button) findViewById(R.id.bSons1);
        Button b = (Button) findViewById(R.id.bSons2);
        Button c = (Button) findViewById(R.id.bSons3);
        a.setEnabled(false);
        b.setEnabled(false);
        c.setEnabled(false);
    }

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

    public void afficheRegles(View view){
        idLayout = chargerRegles(this, view, R.string.regleSon);
    }

    public void afficheDialog(View v){
        ReglesDialog dia = new ReglesDialog();
        dia.setIdString(R.string.regleSon);
        dia.show(getFragmentManager(),"Regles");
    }

    public void changeLvl1(View view){
        lvl=niv.changeLvl1(this, lvl);

    }

    public void jouerSonRegles(View v){
        Utilities.jouerSon("ok",getApplicationContext());
    }

    public void rejouer(View view) {
        view.invalidate();
        compteur=0;
        setContentView(R.layout.activity_start);
        idLayout = R.layout.activity_start;
    }

    public void retournerMenu(View view){
        view.invalidate();
        this.finish();
    }

    public void changeLvl2(View view){
        lvl=niv.changeLvl2(this, lvl);
    }
    public void changeLvl3(View view){
        lvl=niv.changeLvl3(this, lvl);
    }
    public void back(View view){ revenirDebut(this, view);}
}
