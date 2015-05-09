package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.text.InputType;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 05/05/2015.
 * Questa classe gestisce l'interfaccia in cui l'utente immette la durata massima della sessione
 */
public class DurationFragment extends DialogFragment implements DialogInterface.OnClickListener{

    //per salvare e caricare i dati
    private SharedPreferences prefs;
    //L'EditText su cui verra' inserita la durata
    private EditText mEditText;
    // oggetto per il meccanismo di calback
    private OnDurationChangedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //classe per creare ALert Dialog, ci applico il tema di sistema
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        alert.setTitle("Durata Sessione");
        final Context themeContext = getActivity();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.fragment_duration_picker, null);
        alert.setView(view);
        alert.setPositiveButton(getString(R.string.ok), this);
        alert.setNegativeButton(getString(R.string.cancel), this);
        return alert.create();
    }

    /**
     * @param which il tasto premuto (ok o cancella)
     * con onClick comunico tramite callback all'attivita' che l'utente ha premuto ok*/
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (mCallback != null) {
                    mEditText=(EditText)getDialog().findViewById(R.id.etDuration);
                    String temp=mEditText.getText().toString();
                    if(temp!=null) {
                        int duration=getActivity().getPreferences(Context.MODE_PRIVATE).getInt("session_duration",12);
                        try {
                            duration = Integer.parseInt(temp.trim());
                        }catch(NumberFormatException nfe){
                                Log.e("FemurShield","Errore di conversione string a int");
                            }

                            if(duration<1)
                                duration=1;
                            if(duration>24)
                                duration=24;
                            mCallback.onDurationChanged(duration);
                    }
                }
                break;
        }
    }

    /* Questo metodo controlla che l'attivita' abbia implementato l'interfaccia per il callback*/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnDurationChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDurationChangedListener");
        }
    }

    //metodo che converte da pixel a dp
    private int convertDpToPx(int dp, View view) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        return Math.round(pixels);
    }

    /**
     * Interfaccia di callback per quando l'utente ha finito premendo il bottone ok
     */
    public interface OnDurationChangedListener {

        /**
         * @param newDuration la durata massima delle sessioni scelta.
         */
        void onDurationChanged(int newDuration);
    }
}