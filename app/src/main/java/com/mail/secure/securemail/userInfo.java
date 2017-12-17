package com.mail.secure.securemail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

import io.realm.Realm;

public class userInfo implements AsyncResponse {

    private Context context;


    public void pushInfo(String email,String pass,String phone,Context context){
        this.context=context;

        //set the infromation that you want to send to database
        HashMap postData = new HashMap();
        postData.put("txtUsername", email);
        postData.put("txtPassword", pass );
        postData.put("mobile", phone);


        //To display a dialog wait to send the information
        PostResponseAsyncTask task = new PostResponseAsyncTask(context, postData,this);
        //Try to send the information
        task.execute("http://192.168.1.87/add_user.php");

    }


    @Override   //take the response from PHP
    public void processFinish(String output) {
     //Toast a message as the response
        if(output.equals("done")){
            Toast.makeText(context,R.string.registration_done, Toast.LENGTH_LONG).show();
                ((Activity)context).finish();
        }
        else if (output.equals("User Already Exist"))
            Toast.makeText(context, "This email Already Existed", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "There is an error", Toast.LENGTH_LONG).show();
    }

}
