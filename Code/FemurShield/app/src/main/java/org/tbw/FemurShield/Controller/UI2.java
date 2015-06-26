package org.tbw.FemurShield.Controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;


public class UI2 extends BaseActivity implements FallFragment.OnFallClickListener{

    private Session thisSession;
    public final static String SESSION_DATA_STAMP = "sessiondatastamp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui2);


        LinearLayout fragContainer = (LinearLayout) findViewById(R.id.ui2rootLayout);

        LinearLayout ll = new LinearLayout(this);
        int orientation=this.getResources().getConfiguration().orientation;
        ll.setOrientation(orientation== Configuration.ORIENTATION_PORTRAIT?LinearLayout.VERTICAL:LinearLayout.HORIZONTAL);

        ll.setId(View.generateViewId());


        String thisData = getIntent().getExtras().getString(SESSION_DATA_STAMP);
        SessionDetailsFragment sdf = SessionDetailsFragment.newIstance(thisData,SessionDetailsFragment.SHOW_SIGNATURE);
        FallFragment ff = FallFragment.newInstance(thisData);

        getFragmentManager().beginTransaction().add(ll.getId(),sdf, "sessionDetails").commit();
        getFragmentManager().beginTransaction().add(ll.getId(), ff, "FallDetails").commit();

        fragContainer.addView(ll);

        /*
        FallFragment ff = FallFragment.newInstance();
        SessionDetailsFragment sdf = SessionDetailsFragment.newIstance();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(ff, "cadutalista");
        ft.add(sdf, "dettaglicaduta");
        ft.commit();
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, UI5.class);
                startActivity(intent);
                return true;
            case R.id.action_all_sessions:
                Intent ui1 = new Intent(this, UI1.class);
                startActivity(ui1);
                return true;
            case R.id.action_active_session:
                if (SessionManager.getInstance().getActiveSession() != null) {
                    Intent ui3 = new Intent(this, UI3.class);
                    startActivity(ui3);
                } else
                    Toast.makeText(this, getString(R.string.no_active_session), Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        finish(); // chiude la UI2 quando premi il tasto back e ritorna alla chiamante (UI1)
    }

    @Override
    public void onFallClick(String sessionID, String fallID) {
        Intent fallDetails=new Intent(this,UI4.class);
        fallDetails.putExtra(UI4.ID_SESSION, sessionID);
        fallDetails.putExtra(UI4.ID_FALL, fallID);
        startActivity(fallDetails);
    }
}
