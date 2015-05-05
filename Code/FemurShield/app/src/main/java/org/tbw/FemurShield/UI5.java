package org.tbw.FemurShield;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.tbw.FemurShield.View.ActivityObserver;


public class UI5 extends ActivityObserver implements SettingsFragment.OnFragmentInteractionListener,TimePickerFragment.OnAlarmChangedListener,DurationFragment.OnDurationChangedListener{

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui5);

        // controlla che il layout in uso abbia il posto per il fragment
        if (findViewById(R.id.fragment_container_settings) != null) {

            /*Se stiamo tornando indietro non abbiamo bisogno di ricaricarlo
            * rischiamo di ritrovarci con un secondo fragment sovrapposto*/
            if (savedInstanceState != null) {
                return;
            }

            // Creo il mio fragment
            SettingsFragment settFragment = new SettingsFragment();

            // passo gli eventuali argomenti al fragment
            settFragment .setArguments(getIntent().getExtras());

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container_settings, settFragment ,"mSettingsFragment");
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVoiceSelected(SettingListItem s) {
        if(s.title.equalsIgnoreCase(getString(R.string.title_alarm)))
        {
            TimePickerFragment timepick= new TimePickerFragment();
            timepick.show(getFragmentManager(), "TimePicker");
        }
        if(s.title.equalsIgnoreCase(getString(R.string.title_sample_rate)))
        {
            SliderFragment slider=new SliderFragment();
            slider.show(getFragmentManager(),"SampleRatePicker");
        }
        if(s.title.equalsIgnoreCase(getString(R.string.title_session_duration)))
        {
            DurationFragment duration=new DurationFragment();
            duration.show(getFragmentManager(),"DurationPicker");
        }

    }

    @Override
    public void OnAlarmChanged(int hourOfDay, int minute) {
        // salva l'orario selezionato
        prefs=getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("alarm_hour", hourOfDay);
        editor.putInt("alarm_minute", minute);
        editor.commit();
        //aggiorna l'orario mostrato nella descrizione
        SettingsFragment articleFrag = (SettingsFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        if(articleFrag!=null)
            articleFrag.updateAlarmTime(hourOfDay,minute);
        // TODO: impostare la sveglia con l'orario salvato
    }

    @Override
    public void onDurationChanged(int newDuration) {

        prefs=getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("session_duration", newDuration);
    }
}
