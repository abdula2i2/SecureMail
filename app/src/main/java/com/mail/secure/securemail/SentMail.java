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



public class SentMail extends Fragment{



    private Realm realm;
    private ArrayAdapter<String> adapter;

    //this code to link this page with mailbox
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sent_mail, container, false);

        return rootView;
    }

    // getting Emails from Realm database and but it in the sent page ListView
    @Override
    public void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();

        ListView listEmails = (ListView)getView().findViewById(R.id.sentemails);
        ArrayList<String> emailsubjects=new ArrayList<>();

        RealmResults<User> result= realm.where(User.class).findAll();// search for user in realm
        realm.close();

        RealmList<Emails> email = result.first().getEmails();// get the emails from realm
        for(Emails s: email )
        {
            emailsubjects.add(s.getSubject());//take subject from realm and add it to arrayList
        }
        if(!result.isEmpty()){// if the array not empty set it in the adapter
            adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,emailsubjects);
            listEmails.setAdapter(adapter);// set the adapter to ListView
        }


//Display email in message activty when user click on the email
        listEmails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),message.class);
                intent.putExtra("id",position);
                intent.putExtra("from","sentMail");//to let class message this email from inbox email
                startActivity(intent);
            }
        });
    }


}
