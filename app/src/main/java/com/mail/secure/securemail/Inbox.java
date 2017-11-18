package com.mail.secure.securemail;

/**
 * Created by zeez on 10/8/2017.
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

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


public class Inbox extends Fragment{

    private Realm realm;//
    private ArrayAdapter<String> adapter;

    @Override//هذا الكود لازم تحطه في كل بدايه صفحه
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.inbox, container, false);

        return rootView;
    }
    // الكود الي تحت عشان يطلع الرسايل من قاعده البيانات ويعرضها ويحطها في القائمه حق الاميلات
    @Override
    public void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();
        ListView listEmails = (ListView)getView().findViewById(R.id.inboxemails);
        ArrayList<String> emailsubjects=new ArrayList<>();

        RealmResults<User> result= realm.where(User.class).findAll();// يطلع اليوزر من القاعدة
        realm.close();

        RealmList<Emails> email = result.first().getInbox();// تحفظ كل الي في الانبوكس بعدين تحطهم في المصوفوفة عشان تحطفها في الادابتر
        for(Emails s: email )
        {
            emailsubjects.add(s.getSubject());
        }
        if(!result.isEmpty()){
            adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,emailsubjects);
            listEmails.setAdapter(adapter);
        }


//الكود الي تحت عشان اذا ضغط المستخدم على اي ايتم من القائمه يفتح له الرساله
        listEmails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),message.class);
                intent.putExtra("id",position);
                intent.putExtra("from","inbox"); // عشان كلاس مسج يفرق بينه وبين الاميل المرسل
                startActivity(intent);
            }
        });
    }
}
