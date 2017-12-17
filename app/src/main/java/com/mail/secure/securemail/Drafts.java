package com.mail.secure.securemail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class Drafts extends Fragment {


    private Realm realm;
    private ArrayAdapter<String> adapter;


    //this code to link this page with mailbox
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.drafts, container, false);

        return rootView;
    }


    // getting Emails from Realm database and but it in the drafts page ListView
    @Override
    public void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();

        ListView listDrafts = (ListView)getView().findViewById(R.id.draftemails);
        ArrayList<String> draftsubject=new ArrayList<>();

        RealmResults<User> result= realm.where(User.class).findAll();
        realm.close();

        RealmList<Emails> draft = result.first().getDrafts();

        for(Emails d: draft )
        {
            draftsubject.add(d.getSubject());
        }
        if(!result.isEmpty()){
            adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,draftsubject);
            listDrafts.setAdapter(adapter);
        }


        //Display email in SendEmail activty when user click on the email
        listDrafts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),SendEmail.class);
                intent.putExtra("idDraft",position);
                startActivity(intent);
            }
        });


    }

}
