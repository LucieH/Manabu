package be.manabu;

/**
 * Cette classe est l'activité principal de l'application Manabu. Elle représente le menu où est
 * affiché le choix de jouer à l'un au l'autre exercice.
 * @author Lucie Herrier - 3TL1
 */

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_main);
    }

    /**
     * Fonction utilisée lors de la création de l'activité.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main, menu);
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
     *  Cette fonction est appelée quand l'utilisateur clique sur le bouton "Lecture flash"
     * @param view
     */
    public void startFlash(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, FlashActivity.class);
    	startActivity(intent);
    }

    /**
     * Cette fonction est appelée quand l'utilisateur clique sur le bouton "Imagerie"
     * @param view
     */
    public void startImagerie(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, ImgActivity.class);
    	startActivity(intent);
    }

    /**
     * Cette fonction est appelée quand l'utilisateur clique sur le bouton "Anagrammes"
     * @param view
     */
    public void startAnagrammes(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, AnagrammeActivity.class);
    	startActivity(intent);
    }

    /**
     * Cette fonction est appelée quand l'utilisateur clique sur le bouton "Ecouter le son"
     * @param view
     */
    public void startSon(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, SonActivity.class);
    	startActivity(intent);
    }
}
