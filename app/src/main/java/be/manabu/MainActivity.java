package be.manabu;

/**
 * Cette classe est l'activit� principal de l'application Manabu. Elle repr�sente le menu o� est
 * affich� le choix de jouer � l'un au l'autre exercice.
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main, menu);
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


    /**
     *  Cette fonction est appel�e quand l'utilisateur clique sur le bouton "Lecture flash"
     * @param view
     */
    public void startFlash(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, FlashActivity.class);
    	startActivity(intent);
    }

    /**
     * Cette fonction est appel�e quand l'utilisateur clique sur le bouton "Imagerie"
     * @param view
     */
    public void startImagerie(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, ImgActivity.class);
    	startActivity(intent);
    }

    /**
     * Cette fonction est appel�e quand l'utilisateur clique sur le bouton "Anagrammes"
     * @param view
     */
    public void startAnagrammes(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, AnagrammeActivity.class);
    	startActivity(intent);
    }

    /**
     * Cette fonction est appel�e quand l'utilisateur clique sur le bouton "Ecouter le son"
     * @param view
     */
    public void startSon(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, SonActivity.class);
    	startActivity(intent);
    }
}
