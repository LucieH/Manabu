package be.manabu;

import android.app.Activity;
import android.widget.ImageView;

/**
 * Cette classe permet de cr�er un objet de type niveau pour un exercice
 * @author Lucie Herrier - 3TL1
 */

public class Niveaux extends Activity{

    /**
     * Cette fonction permet de d�finir le niveau comme �tant le niveau 1.
     * @param act l'activit� en cours
     * @param lvl le niveau actuel d�fini pour l'exercice
     * @return le nouveau niveau pour l'exercice, dans le cas pr�sent 1.
     */
    public static int changeLvl1(final Activity act, int lvl){
        ImageView lvl2 = (ImageView) act.findViewById(R.id.EtoileLvl2);
        ImageView lvl3 = (ImageView) act.findViewById(R.id.EtoileLvl3);
        lvl2.setImageResource(R.drawable.etoile_non);
        lvl3.setImageResource(R.drawable.etoile_non);
        return 1;
    }

    /**
     * Cette fonction permet de d�finir le niveau comme �tant le niveau 2 ou de d�sactiver celui-ci.
     * @param act l'activit� en cours
     * @param lvl le niveau actuel d�fini pour l'exercice
     * @return le nouveau niveau pour l'exercice : soit 2, soit 1.
     */
    public static int changeLvl2(final Activity act, int lvl){
        ImageView lvl2 = (ImageView) act.findViewById(R.id.EtoileLvl2);
        ImageView lvl3 = (ImageView) act.findViewById(R.id.EtoileLvl3);
        if (lvl==3){
            lvl3.setImageResource(R.drawable.etoile_non);
            return 2;
        }
        else {
            if(lvl==2){
                lvl2.setImageResource(R.drawable.etoile_non);
                return 1;
            }
            else{
                lvl2.setImageResource(R.drawable.etoile_oui);
                return 2;
            }
        }
    }

    /**
     * Cette fonction permet de d�finir le niveau comme �tant le niveau 3 ou de d�sactiver celui-ci.
     * @param act l'activit� en cours
     * @param lvl le niveau actuel d�fini pour l'exercice
     * @return le nouveau niveau pour l'exercice : soit 3, soit 2.
     */
    public static int changeLvl3(final Activity act,int lvl){
        ImageView lvl2 = (ImageView) act.findViewById(R.id.EtoileLvl2);
        ImageView lvl3 = (ImageView) act.findViewById(R.id.EtoileLvl3);
        if (lvl==2){
            lvl3.setImageResource(R.drawable.etoile_oui);
            return 3;
        }
        else{
            if (lvl==1){
                lvl2.setImageResource(R.drawable.etoile_oui);
                lvl3.setImageResource(R.drawable.etoile_oui);
                return 3;
            }
            else{
                lvl3.setImageResource(R.drawable.etoile_non);
                return 2;
            }
        }
    }

}
