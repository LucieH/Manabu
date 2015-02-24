package be.manabu;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);       
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Permet d'utiliser la police choisie */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    
    /** Called when the user clicks the Lecture flash button */
    public void startFlash(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, FlashActivity.class);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the Imagerie button */
    public void startImagerie(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, ImgActivity.class);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the Anagrammes button */
    public void startAnagrammes(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, AnagrammeActivity.class);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the Sons button */
    public void startSon(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, SonActivity.class);
    	startActivity(intent);
    }
}
