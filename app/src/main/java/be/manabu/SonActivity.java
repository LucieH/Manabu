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
import android.widget.TextView;

import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static be.manabu.Utilities.*;

public class SonActivity extends ActionBarActivity {
    final Random rnd = new Random();
    final static private int START_ASCII_LETTRES = 97;
    final static private int NB_SONS = 28;
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
        // NOTE Trap the back key: when the CustomKeyboard is still visible hide it, only when it is invisible, finish activity
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
        TextView tvSon = (TextView) findViewById(R.id.TVSonIS);
        int nbAscii = 0;
        switch (rand){
            case 26 :
                //le son est e accent aigu
                nbAscii = 233;
                break;
            case 27 :
                //le son est e accent grave
                nbAscii = 232;
                break;
            default:
                nbAscii = rand+START_ASCII_LETTRES;
                //les lettres de l'alphabet
        }
        tvSon.setText("Le son est : "+nbAscii+ String.valueOf(Character.toChars(nbAscii)));
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
