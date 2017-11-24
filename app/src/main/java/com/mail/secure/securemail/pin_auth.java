package com.mail.secure.securemail;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;

public class pin_auth extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etxtPhoneCode;
    private String mVerificationId;
    private Realm realm;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_auth);

        etxtPhoneCode = (EditText) findViewById(R.id.pin);
//الدلة الي تحت الي فهمته منها انه اذا كان مسجل المستخدم وحاول يستجل مره ثانيه يقوله انت مسجل ويخرج
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Toast.makeText(pin_auth.this, "now_logged_in"+ firebaseAuth.getCurrentUser().getProviderId(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };

        requestCode();// هذا استدعاء لدالة الي تجيب رقم التحقق
    }

    public void requestCode() {//هذي الدالة تجيب رقم الجوال من قاعدة البيانات وتسوي طلب للرقم التحقق فيه
       //هنا تاخذ من قاعدة البيانات الرقم حق الجوال وتطحه في المخزن فون نمبر
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("status", "active").findFirst();
        String phoneNumber = user.getPhone();

        //تحت كلها دالة التحقق تحط فيها الرقم والوقت حق الانتطار والاكتفتي الي انت فيها الاخيره اعمل نفسك ميت
        //الدوال الي تحت اتوقع لكل حالة دالة
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, pin_auth.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override//اتوقع هذي تاخذ الرقم من الرسالة تلقائيا
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override// هذي اذا صارت مشكلة في التحقق مثلا الرقم او اذا مشغل البرنامج من محاكي وغيرها
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        Toast.makeText(pin_auth.this, "onVerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override//تحفظ الايدي حق التحقق عشان تستخدمه في دالة التحقق
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //now the code has been sent, save the verificationId we may need it
                        super.onCodeSent(verificationId, forceResendingToken);

                        mVerificationId = verificationId;
                    }

                    @Override//اذا صارت مشكلة تاخر  يمكن
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        //called after timeout if onVerificationCompleted has not been called
                        super.onCodeAutoRetrievalTimeOut(verificationId);
                        Toast.makeText(pin_auth.this, "onCodeAutoRetrievalTimeOut :" + verificationId, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
//الدالة الي تحت هي دالة التحقق وهي الزبدة ياخذ الكود الي دخله اليوزر ويتحقق منه اذا صح او لا
    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//هنا اذا صح الكود تطلع هالرسالة
                            Toast.makeText(pin_auth.this, "signed success", Toast.LENGTH_SHORT).show();
                        } else {//اذا غلط ويطلع له رسالة غلط يبو
                            Toast.makeText(pin_auth.this, "sign fail" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override// هذي الدالة لو المستخدم ما سجل دخولة وضغط تراجع يحذف البيانات
    public void onBackPressed() {
        super.onBackPressed();

        realm = Realm.getDefaultInstance();
        RealmResults<User> result = realm.where(User.class).findAll();
        User user = result.first();
        realm.beginTransaction();
        user.deleteFromRealm();
        realm.commitTransaction();
        realm.close();

    }

    public void submit(View view) {//  هذي اذا ضغط على زر السب مت يتحقق اذا مب فاضي الفراغ ويستعدي دالة التحقق
        String code = etxtPhoneCode.getText().toString();
        if (TextUtils.isEmpty(code))
            return;

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }
}