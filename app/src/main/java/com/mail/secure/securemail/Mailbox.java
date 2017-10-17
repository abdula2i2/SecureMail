package com.mail.secure.securemail;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Mailbox extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
/// كل الي فوق كود من نفس الاندرويد ستوديو عشان يزبط الصفحات
        Realm.init(getApplicationContext());
// ال بعده هذا عشان يحول الزر حق الرسايل لين الصفحه الي بعدها
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Mailbox.this, SendEmail.class);
                startActivity(i);
            }
        });
 // هذا الشرط عشان اذا جاء اذا ضغط المستخدم رجوع من صفحه التسجيل يخرج من البرنامج
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }
    private ArrayAdapter<String> adapter;
    private ArrayList<String> emailsubjects;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mailbox, menu);


        final ListView listEmails = (ListView)findViewById(R.id.sentemails);
        realm = Realm.getDefaultInstance();


        emailsubjects=new ArrayList<>();
        RealmResults<User> result= realm.where(User.class).findAll();
        RealmList<Emails> emails = null;
       if (!result.isEmpty()) {
           emails = result.first().getEmails();
           for (Emails s : emails) {
               emailsubjects.add(s.getSubject());
           }

               adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emailsubjects);
               listEmails.setAdapter(adapter);


       }
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        final Context  co=this;


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    List<String> lstFound = new ArrayList<String>();
                    for(String item:emailsubjects){
                        if(item.contains(newText))
                            lstFound.add(item);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,lstFound);
                    listEmails.setAdapter(adapter);
                }
                else{
                    //if search text is null
                    //return default
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,emailsubjects);
                    listEmails.setAdapter(adapter);
                }
                return true;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.search) {

            return true;
        }

       else if (id == R.id.sign_out) {
            realm = Realm.getDefaultInstance();
            RealmResults<User> result= realm.where(User.class).findAll();
            User user = result.first();

            realm.beginTransaction();
            user.deleteFromRealm();
            realm.commitTransaction();

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();

        RealmResults<User> result= realm.where(User.class).findAll();

        if(result.isEmpty()) {

            Intent i = new Intent(Mailbox.this, MainActivity.class);
            startActivity(i);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Inbox inbox= new Inbox();
            SentMail sentMail= new SentMail();

            switch (position) {
                case 0:
                    return inbox;
                case 1:
                    return sentMail;
               default:
                   return null;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "inbox";
                case 1:
                    return "sent mail";

            }
            return null;
        }
    }
}
