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
     * Cette fonction permet de d�finir l'id de la resource string qui sera utilis�e.
     * @param id l'identifiant de la resource.
     */
    protected void setIdString(int id){
        this.idString = id;
    }

    /**
     * Cette fonction cr�e le dialog par dessus l'activit� en cours.
     * @return la cr�ation du dialog
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
        // Cr�e l'objet et le retourne
        return builder.create();
    }
}
