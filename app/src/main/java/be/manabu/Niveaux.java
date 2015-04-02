package be.manabu;

import android.app.Activity;
import android.widget.ImageView;

/**
 * Created by Lucie on 02-04-15.
 */
public class Niveaux extends Activity{

    public static int changeLvl1(final Activity act, int lvl){
        ImageView lvl2 = (ImageView) act.findViewById(R.id.EtoileLvl2);
        ImageView lvl3 = (ImageView) act.findViewById(R.id.EtoileLvl3);
        lvl2.setImageResource(R.drawable.etoile_non);
        lvl3.setImageResource(R.drawable.etoile_non);
        return 1;
    }

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
