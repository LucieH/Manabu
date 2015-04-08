package be.manabu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lucie on 10-03-15.
 */
public class Utilities{
    public static Toast toast;

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

    protected static void afficherToast(final Activity act, Boolean res, String message, String col, Context ctx){
        LayoutInflater inflater = act.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) act.findViewById(R.id.toast_layout_root));
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
        if (toast == null) toast = new Toast(ctx.getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    protected static void chargerRegles(final Activity act, View view, int id){
        view.invalidate();
        act.setContentView(R.layout.regles);
        TextView tv = (TextView) act.findViewById(R.id.TVregles);
        tv.setText(id);
    }


}
