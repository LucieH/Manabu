package be.manabu;

/**
 * Cette classe
 * @author Lucie Herrier - 3TL1
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ReglesDialog extends DialogFragment {

    private int idString;

    /**
     * Cette fonction permet de définir l'id de la resource string qui sera utilisée.
     * @param id l'identifiant de la resource.
     */
    protected void setIdString(int id){
        this.idString = id;
    }

    /**
     * Cette fonction crée le dialog par dessus l'activité en cours.
     * @return la création du dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog, null);
        TextView regles = (TextView) v.findViewById(R.id.reglesDialog);
        regles.setText(getResources().getString(idString));
        builder.setView(v)
                .setPositiveButton(R.string.fermer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Crée l'objet et le retourne
        return builder.create();
    }
}
