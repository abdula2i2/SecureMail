package com.mail.secure.securemail;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

import io.realm.Realm;

/**
 * Created by zeez on 11/22/2017.
 */

public class getPhoneNum  implements AsyncResponse {

   private Context context;
    private String email;
    private String pass;
public void getNumber(String email,String pass,Context context){
                // تخزن البيانات الي ارسلها المسخدم في مخازن عشان اذا نجح الموضوع تخزنها بعدين في قاعدة البيانات
                this.context=context;
                this.email  = email;
                this.pass = pass;
                //هنا تسوي الاتصال مع قاعدة البيانات تاخذ اليوزر والباس
                HashMap postData = new HashMap();
                postData.put("mobile", "android");
                postData.put("txtUsername", email);
                postData.put("txtPassword", pass );

                //عشان يطلع حق الانتظار وقت ما يحاول جيب البيانات
                PostResponseAsyncTask task = new PostResponseAsyncTask(context, postData,this);
                task.execute("http://192.168.1.87/login.php");// هنا يحاول يتصل وياخذ رقم الجوال حق المستخدم


}


    @Override// هذي الدالة اذا خلصت عملية الاتصال يكون فيها الاوت بوت
    public void processFinish(String output) {
        //اذا الاوتبوت كان غير الفشل والرقم الي رجعه اطول من 7 ارقام يدخل
        // يخزن ملعومات المسخدم في قاعدة بيانات الجوال و ينتقل لصفحة التحقق
       if(!output.equals("Failed")&&output.length()>7){

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
       else// هنا بس يطلع رسالة الفشل
           Toast.makeText(context,"Failed" , Toast.LENGTH_SHORT).show();


    }
}