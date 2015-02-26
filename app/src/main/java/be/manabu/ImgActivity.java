package be.manabu;

import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ImgActivity extends ActionBarActivity {

    final Random rnd = new Random();
    private String strTmp = "start";

    //Getters et setters
    public void setStrTmp(String s){
        this.strTmp = s;
    }

    //Fonctions override
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
		setContentView(R.layout.activity_img_start);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.img, menu);
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


    //Fonctions personnelles

    /** Fonction prise depuis
    * http://stackoverflow.com/questions/7493287/android-how-do-i-get-string-from-resources-using-its-name
            * permettant de récupérer une string de string.xml depuis son nom (ici lié avec l'image. */
            private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }


    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }

    /** Called when the user clicks the Sons button */
    public void refresh(View view) {
        view.invalidate();
        setContentView(R.layout.activity_img);
        // Do something in response to button
        randomImg(strTmp);

    }

    /** Démarrer le jeu imagerie ave les fiches Freinet*/
    public void startImage(View view) {
        view.invalidate();
        setContentView(R.layout.activity_img);
        randomImg(strTmp);

    }

    /** Afficher les choix pour l'image*/
    public void afficherChoix(View view) {
        view.invalidate();
        setContentView(R.layout.activity_img_choix);
        final ImageView img = (ImageView) findViewById(R.id.imgRandom);
        img.setImageDrawable
                (
                        getResources().getDrawable(getResourceID(strTmp, "drawable",
                                getApplicationContext()))
                );
        rnd.nextInt(3);
        final Button b1 = (Button) findViewById(R.id.choix1);
        final Button b2 = (Button) findViewById(R.id.choix2);
        final Button b3 = (Button) findViewById(R.id.choix3);

        //texte ramdom dans les boutons

        int i = rnd.nextInt(3);
        int j = rnd.nextInt(2);
        switch(i){
            case 0:
                b1.setText(getStringResourceByName(strTmp));
                switch(j){
                    case 0:
                        b3.setText(getStringResourceByName(strTmp+"1"));
                        b2.setText(getStringResourceByName(strTmp+"2"));
                        break;
                    case 1:
                        b2.setText(getStringResourceByName(strTmp+"1"));
                        b3.setText(getStringResourceByName(strTmp+"2"));
                        break;
                }
                break;
            case 1:
                b2.setText(getStringResourceByName(strTmp));
                switch(j){
                    case 0:
                        b3.setText(getStringResourceByName(strTmp+"1"));
                        b1.setText(getStringResourceByName(strTmp+"2"));
                        break;
                    case 1:
                        b1.setText(getStringResourceByName(strTmp+"1"));
                        b3.setText(getStringResourceByName(strTmp+"2"));
                        break;
                }
                break;
            case 2:
                b3.setText(getStringResourceByName(strTmp));
                switch(j){
                    case 0:
                        b1.setText(getStringResourceByName(strTmp+"1"));
                        b2.setText(getStringResourceByName(strTmp+"2"));
                        break;
                    case 1:
                        b2.setText(getStringResourceByName(strTmp+"1"));
                        b1.setText(getStringResourceByName(strTmp+"2"));
                        break;
                }
                break;

        }


        /*b1.setText(getStringResourceByName(strTmp));
        b2.setText(getStringResourceByName(strTmp+"1"));
        b3.setText(getStringResourceByName(strTmp+"2"));*/
    }

     /**  Permet d'avoir les images prises au hasard avec le mot correspondant  */
    protected void randomImg(String strTmp){
        final ImageView img = (ImageView) findViewById(R.id.imgRandom);
        // I have 3 images named img_0 to img_2, so...
        String str;
        do {
            str = "img_" + rnd.nextInt(3);
        }while (strTmp.equals(str));
        img.setImageDrawable
                (
                        getResources().getDrawable(getResourceID(str, "drawable",
                                getApplicationContext()))
                );
        final TextView tv = (TextView) findViewById(R.id.tv1);
        tv.setText(getStringResourceByName(str));
        setStrTmp(str);
    }
}


