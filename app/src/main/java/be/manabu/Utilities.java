package be.manabu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lucie on 10-03-15.
 */
public class Utilities{

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

    /** Fonction prise depuis
     * http://stackoverflow.com/questions/7493287/android-how-do-i-get-string-from-resources-using-its-name
     * permettant de récupérer une string de string.xml depuis son nom (ici lié avec l'image. */
    public final static String getStringResourceByName(String aString, Context ctx) {
        String packageName = ctx.getPackageName();
        int resId = ctx.getResources().getIdentifier(aString, "string", packageName);
        return ctx.getString(resId);
    }

    protected static void afficherToastReponse(Boolean res, String message, String col, Context ctx, View layout){

        TextView text = (TextView) layout.findViewById(R.id.text);
        ImageView img = (ImageView) layout.findViewById(R.id.resultImg);
        text.setText(message);
        text.setTextColor(Color.parseColor(col));
        if (res){
            img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ok));
        }
        else{
            img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ko));
        }
        Toast toast = new Toast(ctx.getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void changeLvl1(View view, int lvl, ImageView lvl2, ImageView lvl3){
        lvl2.setImageResource(R.drawable.etoile_non);
        lvl3.setImageResource(R.drawable.etoile_non);
        lvl=1;

    }

    public void changeLvl2(Activity a, View view, int lvl){
        ImageView lvl2 = (ImageView) a.findViewById(R.id.EtoileLvl2);
        ImageView lvl3 = (ImageView) a.findViewById(R.id.EtoileLvl3);
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
    public void changeLvl3(Activity a, View view, int lvl){
        ImageView lvl2 = (ImageView) a.findViewById(R.id.EtoileLvl2);
        ImageView lvl3 = (ImageView) a.findViewById(R.id.EtoileLvl3);
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
