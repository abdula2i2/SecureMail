package com.mail.secure.securemail;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import java.util.HashMap;
import io.realm.Realm;



public class getPhoneNum  implements AsyncResponse {

    private Context context;
    private String email;
    private String pass;

public void getNumber(String email,String pass,Context context){

                //Take the info from user and store it in the variables
                this.context=context;
                this.email  = email;
                this.pass = pass;

                 //Set the infromation that you want to send to database
                HashMap postData = new HashMap();
                postData.put("txtUsername", email);
                postData.put("txtPassword", pass );

                //To display a dialog wait to send the information
                PostResponseAsyncTask task = new PostResponseAsyncTask(context, postData,this);
                //Try to send the information
                task.execute("http://192.168.1.87/login.php");
}

    //take the response from PHP
    @Override
    public void processFinish(String output) {

        //if the output has more than 7 numbers and not failed
       if(!output.equals("Failed")&&output.length()>7){
           // store the user information into realm with the phone number
           Realm realm = Realm.getDefaultInstance();
           realm.beginTransaction();//

           User user = new User();

           user.setEmail(this.email);
           user.setPassword(this.pass);
           user.setStatus("active");
           user.setPhone(output);

           realm.copyToRealmOrUpdate(user);
           realm.commitTransaction();
           realm.close();


        Intent intent = new Intent(context, pin_auth.class);
        context.startActivity(intent);
       }
       else// if the output is failed will toast a message with failed
           Toast.makeText(context,"Failed" , Toast.LENGTH_SHORT).show();


    }
}