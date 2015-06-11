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
 * Classe reprenant des fonctions utilisées par les différentes activités de l'application.
 * @author Lucie Herrier - 3TL1
 */

public class Utilities{
    public static Toast toast;

    /**
     * Cette fonction permet d'obtenir l'ID d'une resource depuis son nom et son type.
     * @param resName le nom de la resource
     * @param resType le type de la resource
     * @param ctx le contexte de l'application
     * @return l'ID de la resource
     */
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
     * permettant de récupérer une string de string.xml depuis son nom (ici lié avec l'image).
     *
     * @param aString le nom de la resource string à récupérer dans strings.xml
     * @param ctx le contexte de l'application
     * @return la chaîne de caractères voulue
     * */
    protected final static String getStringResourceByName(String aString, Context ctx) {
        String packageName = ctx.getPackageName();
        int resId = ctx.getResources().getIdentifier(aString, "string", packageName);
        return ctx.getString(resId);
    }

    /**
     * Cette fonction permet d'afficher un toast android définissant la bonne ou mauvaise réponse
     * sur l'activité en cours.
     * @param act l'activité en cours
     * @param res défini à true si la réponse est correcte, false si elle est incorrecte
     * @param ctx le contexte de l'application
     */
    protected static void afficherToastRep(final Activity act, Boolean res, Context ctx){
        LayoutInflater inflater = act.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) act.findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        ImageView img = (ImageView) layout.findViewById(R.id.resultImg);
        if (res){
            text.setText(ctx.getResources().getString(R.string.bienJoue));
            text.setTextColor(Color.parseColor("#00A000"));
            img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ok));
            jouerSon("ok",ctx);
        }
        else{
            text.setText(ctx.getResources().getString(R.string.reessaye));
            text.setTextColor(Color.parseColor("#FF0000"));
            img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ko));
            jouerSon("ko",ctx);
        }
        if (toast == null) toast = new Toast(ctx.getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    /**
     * Cette fonction permet d'afficher les règles de l'un au l'autre exercice.
     * @param act l'activité en cours
     * @param view la vue en cours
     * @param id l'identifiant de la resource de la string contenant les règles
     * @return l'identifiant du layout affiché
     */
    protected static int chargerRegles(final Activity act, View view, int id){
        view.invalidate();
        act.setContentView(R.layout.regles);
        TextView tv = (TextView) act.findViewById(R.id.TVregles);
        tv.setText(id);
        return  R.layout.regles;
    }

    /**
     * Cette fonction permet de revenir au layout de début d'exercice.
     * @param act l'activité en cours
     * @param view la vue en cours
     */
    protected static void revenirDebut(final Activity act, View view){
        view.invalidate();
        act.setContentView(R.layout.activity_start);
    }

    /**
     * Cette fonction permet de jouer un son donné à partir de son nom.
     * @param str le nom du son à jouer
     * @param ctx le contexte de l'application
     */
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
