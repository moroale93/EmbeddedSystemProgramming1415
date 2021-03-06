package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.tbw.FemurShield.Model.ActiveSession;
import org.tbw.FemurShield.Model.OldSession;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che gestisce la lista delle sessioni mostrata nella {@link UI1}
 */
public class SessionsListFragment extends ListFragment {

    private List<SessionsListItem> sItems;
    private SessionsListAdapter sAdapter;
    private OnSessionClickListener mListener;

    public SessionsListFragment() {
    }

    public static SessionsListFragment newInstance() {
        SessionsListFragment slf = new SessionsListFragment();
        return slf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aggiornaLista();
    }

    /**
     * inizializza tutta la lista
     */
    public void aggiornaLista() {

        sItems = new ArrayList<>();
        //aggiungo la sessone attiva
        ActiveSession a = SessionManager.getInstance().getActiveSession();
        if (a != null) {
            SessionsListItem sli = new SessionsListItem(a.getName(), a.getDataTime(), a.getFallsNumber(), "", true);
            sItems.add(sli);

        }
        //aggiungo le sessioni vecchie
        ArrayList<OldSession> old = SessionManager.getInstance().getOldSessions();
        if (old != null) {
            for (int i = 0; i < old.size(); i++) {
                Session s = old.get(i);
                long timeElapsed = s.getDuration();
                int hours = (int) (timeElapsed / 3600000);
                String h = (("" + hours).length() == 1) ? "0" + hours : "" + hours;
                int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
                String m = (("" + minutes).length() == 1) ? "0" + minutes : "" + minutes;
                int seconds = (int) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;
                String se = (("" + seconds).length() == 1) ? "0" + seconds : "" + seconds;
                String durata = "" + h + ":" + m + ":" + se;
                SessionsListItem sli = new SessionsListItem(s.getName(), s.getDataTime(), s.getFallsNumber(), durata, false);
                sItems.add(sli);
            }
        }

        sAdapter = new SessionsListAdapter(getActivity(), sItems);
        setListAdapter(sAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSessionClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnSessionClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //recupera l'elemento cliccato della lista
        SessionsListItem session = (SessionsListItem) l.getItemAtPosition(position);
        if (isAdded())
            mListener.onSessionClick(session.getDataTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sessions_list, container, false);
        return rootView;
    }

    public interface OnSessionClickListener {
        /**
         * Metodo per notificare la pressione corta su una sessione
         *
         * @param sessionID l'ID della sessione premuta
         */
        void onSessionClick(String sessionID);

        /**
         * Metodo per notificare la pressione prolungata su una sessione
         *
         * @param sessionID l'ID della sessione premuta
         */
        void onSessionLongClick(String sessionID);
    }

    //clicklistener per la pressione lunga sulla lista
    public class OnSessionLongClickListener implements ListView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            SessionsListItem sli = (SessionsListItem) parent.getItemAtPosition(position);
            if (isAdded())
                mListener.onSessionLongClick(sli.getDataTime());

            return true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list = getListView();
        list.setOnItemLongClickListener(new OnSessionLongClickListener());
    }

}
