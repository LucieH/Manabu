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
import android.widget.ImageView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AnagrammeActivity extends ActionBarActivity {
    private int lvl = 1;

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

    public void start(View view) {
        view.invalidate();
        setContentView(R.layout.activity_anagramme);
    }

    public void changeLvl1(View view){
        ImageView lvl2 = (ImageView) findViewById(R.id.EtoileLvl2);
        ImageView lvl3 = (ImageView) findViewById(R.id.EtoileLvl3);
        lvl2.setImageResource(R.drawable.etoile_non);
        lvl3.setImageResource(R.drawable.etoile_non);
        lvl=1;

    }

    public void changeLvl2(View view){
        ImageView lvl2 = (ImageView) findViewById(R.id.EtoileLvl2);
        ImageView lvl3 = (ImageView) findViewById(R.id.EtoileLvl3);
        if (lvl==3){
            lvl3.setImageResource(R.drawable.etoile_non);
            lvl=2;
        }
        else {
            if(lvl==2){
                lvl2.setImageResource(R.drawable.etoile_non);
                lvl=1;
            }
            else{
                lvl2.setImageResource(R.drawable.etoile_oui);
                lvl=2;
            }
        }
    }
    public void changeLvl3(View view){
        ImageView lvl2 = (ImageView) findViewById(R.id.EtoileLvl2);
        ImageView lvl3 = (ImageView) findViewById(R.id.EtoileLvl3);
        if (lvl==2){
            lvl3.setImageResource(R.drawable.etoile_oui);
            lvl=3;
        }
        else{
            if (lvl==1){
                lvl2.setImageResource(R.drawable.etoile_oui);
                lvl3.setImageResource(R.drawable.etoile_oui);
                lvl=3;
            }
            else{
                lvl3.setImageResource(R.drawable.etoile_non);
                lvl=2;
            }
        }
    }
}
