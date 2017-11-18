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

/**
 * Created by zeez on 10/8/2017.
 */

public class SentMail extends Fragment{



    private Realm realm;
    private ArrayAdapter<String> adapter;

// الي تحت هذا تضيفه للكود عشان ينربط مع الاكتفتي الاساسي
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sent_mail, container, false);

        return rootView;
    }

// الكود الي تحت عشان يطلع الرسايل من قاعده البيانات ويعرضها ويحطها في القائمه حق الاميلات
      @Override
    public void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();

        ListView listEmails = (ListView)getView().findViewById(R.id.sentemails);
        ArrayList<String> emailsubjects=new ArrayList<>();

        RealmResults<User> result= realm.where(User.class).findAll();// بحث في قاعدة البيانات عن اليوزر
        realm.close();

        RealmList<Emails> email = result.first().getEmails();// تاخذ الاميلات
        for(Emails s: email )
        {
            emailsubjects.add(s.getSubject());//تاخذ العناوين وتضيفها في المصفوفة
        }
        if(!result.isEmpty()){// تزرق المصفوفة في الادابتر
            adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,emailsubjects);
            listEmails.setAdapter(adapter);
        }


//الكود الي تحت عشان اذا ضغط المستخدم على اي ايتم من القائمه يفتح له الرساله
        listEmails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),message.class);
                intent.putExtra("id",position);
                intent.putExtra("from","sentMail");// عشان يعرف كلاس مسج انها جاي من السنت ميل
                startActivity(intent);
            }
        });
    }


}
