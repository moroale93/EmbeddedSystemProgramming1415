package org.tbw.FemurShield.Controller;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import org.tbw.FemurShield.Model.ActiveSession;
import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;

/**
 * Created by Moro on 02/05/15.
 */
public class Controller implements Observer{
    SessionManager SM;
    FallDetector detector;

    @Override
    public void update(Observable observable, Object data) {

    }

    public Controller(){
        SM=SessionManager.getInstance();
    }

    public Session CreateSession(){
        ActiveSession active=SM.getActiveSession();
        if(active==null)
            return SM.createNewActiveSession();
        else
            return null; //TODO: sollevare errore magari;

    }

    public void StartSession(){
        SM.StartSession();
    }

    public void PauseSession(){
        SM.PauseSession();
    }

    public void StopSession(){
        SM.StopSession();
    }

    public FallDetector getDetector(){
        return detector;
    }

    protected void handleFallEvent(Fall fall){
        SM.getActiveSession().AddFall(fall);
    }
}
