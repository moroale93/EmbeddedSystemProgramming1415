package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.tbw.FemurShield.Model.ActiveSession;
import org.tbw.FemurShield.Model.OldSession;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

import java.util.ArrayList;
import java.util.HashMap;


public class UI1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui1);
        //aggiorno la listView
        AggiornaLista();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onRecClick(View view){
        //TODO: business logic
        Session sessione=Controller.CreateSession();

        onPlayClick(view);

        //aggiorno la listView
        AggiornaLista();
    }

    public void onPauseClick(View view){
        //TODO: business logic
        Controller.PauseSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.VISIBLE);

        Intent i=new Intent(getApplicationContext(), FallDetector.class);
        stopService(i);
    }

    public void onPlayClick(View view){
        //TODO: business logic
        Controller.StartSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.INVISIBLE);

        Intent i=new Intent(getApplicationContext(), FallDetector.class);
        startService(i);
    }

    public void onStopClick(View view){
        //TODO: business logic
        Controller.StopSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.INVISIBLE);

        Intent i=new Intent(getBaseContext(), FallDetector.class);
        stopService(i);

        //aggiorno la listView
        AggiornaLista();
    }

    public void AggiornaLista(){
        //costruisco la listview

        //lista delle sessioni che la listview visualizzerà
        ArrayList<Session> sessionsList=new ArrayList<>();

        //aggiungo la sessone attiva
        ActiveSession a =SessionManager.getInstance().getActiveSession();
        if(a!=null)
            sessionsList.add(a);

        //aggiungo le sessioni vecchie
        ArrayList<OldSession> old=SessionManager.getInstance().getOldSessions();
        for(int i=0;i<old.size();i++){
            sessionsList.add(old.get(i));
        }

        //Questa è la lista che rappresenta la sorgente dei dati della listview
        //ogni elemento è una mappa(chiave->valore)

        ArrayList<HashMap<String, Object>> data=new ArrayList<>();


        for(int i=0;i<sessionsList.size();i++){
            Session s=sessionsList.get(i);// per ogni sessione all'inteno della lista

            HashMap<String,Object> sessionMap=new HashMap<>();//creiamo una mappa di valori


            sessionMap.put("signature", s.getSignature());
            sessionMap.put("name", s.getName());
            sessionMap.put("data", s.getDataTime());
            sessionMap.put("falls", "" + s.getFalls().size());

            if(s instanceof ActiveSession){
                sessionMap.put("duration", "");
                sessionMap.put("state", R.mipmap.state);
            }
            else if(s instanceof OldSession){
                sessionMap.put("duration", ((OldSession) s).getDuration());
                sessionMap.put("state", "");
            }

            data.add(sessionMap);  //aggiungiamo la mappa di valori alla sorgente dati
        }


        String[] from={"signature","name","data","falls","duration","state"}; //dai valori contenuti in queste chiavi
        int[] to={R.id.sessionsignatureui1,R.id.sessionnameui1,R.id.sessiondateui1,R.id.sessionfallsui1,R.id.sessiondurationui1,R.id.sessionstateimgui1};//agli id delle view

        //costruzione dell adapter
        SimpleAdapter adapter=new SimpleAdapter(
                getApplicationContext(),
                data,//sorgente dati
                R.layout.sessionlistitemui1, //layout contenente gli id di "to"
                from,
                to);

        //utilizzo dell'adapter
        ((ListView)findViewById(R.id.listsessionui1)).setAdapter(adapter);
    }

    public void openSettings()
    {
        //lancio senza opzioni perchè la ui impostazioni non ne richiede
        Intent intent=new Intent(this,UI5.class);
        startActivity(intent);
    }
}