package be.manabu;

import android.content.Context;

/**
 * Created by Lucie on 10-03-15.
 */
public class Utilities {

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



}
