package be.manabu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReglesDialog extends DialogFragment {

    private int idString;

    protected void setIdString(int id){
        this.idString = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.dialog, null);
        TextView regles = (TextView) v.findViewById(R.id.reglesDialog);
        regles.setText(getResources().getString(idString));
        builder.setView(v)
               // .setTitle(getResources().getString(R.string.regles))
                .setPositiveButton(R.string.fermer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
