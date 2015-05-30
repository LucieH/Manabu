package be.manabu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

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
            jouerSon("ok",ctx);
        }
        else{
            img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ko));
            jouerSon("ko",ctx);
        }
        if (toast == null) toast = new Toast(ctx.getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    protected static int chargerRegles(final Activity act, View view, int id){
        view.invalidate();
        act.setContentView(R.layout.regles);
        TextView tv = (TextView) act.findViewById(R.id.TVregles);
        tv.setText(id);
        return  R.layout.regles;
    }

    protected static void revenirDebut(final Activity act, View view){
        view.invalidate();
        act.setContentView(R.layout.activity_start);
    }

    protected static void jouerSon(String str, Context ctx){
        MediaPlayer mp = MediaPlayer.create(ctx, getResourceID(str, "raw", ctx));
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            };
        });
    }

}
