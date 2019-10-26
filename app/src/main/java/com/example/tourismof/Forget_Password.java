package com.example.tourismof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Forget_Password extends AppCompatActivity {
FirebaseAuth mauth;
String current_user;
EditText e1,Email;
private DatabaseReference UserRef;
    private ProgressDialog loadingBar;

String Phone_Number;
Button button;
PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
      private FirebaseAuth mAuth;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__password);
                loadingBar = new ProgressDialog(this);

              mAuth = FirebaseAuth.getInstance();
                UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

      button=findViewById(R.id.code_btn);
      Email=findViewById(R.id.edit_email);
           mauth=FirebaseAuth.getInstance();

         mcallback= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

             @Override
             public void onVerificationCompleted(PhoneAuthCredential credential) {
                 // This callback will be invoked in two situations:
                 // 1 - Instant verification. In some cases the phone number can be instantly
                 //     verified without needing to send or enter a verification code.
                 // 2 - Auto-retrieval. On some devices Google Play services can automatically
                 //     detect the incoming verification SMS and perform verification without
                 //     user action.
             //    Log.d(TAG, "onVerificationCompleted:" + credential);
               // Toast.makeText(Forget_Password.this, "haaaan", Toast.LENGTH_SHORT).show();
                // signInWithPhoneAuthCredential(credential);
             }

             @Override
             public void onVerificationFailed(FirebaseException e) {
                 // This callback is invoked in an invalid request for verification is made,
                 // for instance if the the phone number format is not valid.
                 Toast.makeText(Forget_Password.this, "naaahi  "+e, Toast.LENGTH_SHORT).show();
                 if (e instanceof FirebaseAuthInvalidCredentialsException) {
                     // Invalid request
                     // ...
                 } else if (e instanceof FirebaseTooManyRequestsException) {
                     // The SMS quota for the project has been exceeded
                     // ...
                 }

                 // Show a message and update the UI
                 // ...
             }

             @Override
             public void onCodeSent(@NonNull String verificationId,
                                    @NonNull PhoneAuthProvider.ForceResendingToken token) {
                 // The SMS verification code has been sent to the provided phone number, we
                 // now need to ask the user to enter the code and then construct a credential
                 // by combining the code with a verification ID.
                // Log.d(TAG, "onCodeSent:" + verificationId);
              Toast.makeText(Forget_Password.this, "Verification Code has been sent ", Toast.LENGTH_SHORT).show();
                 // Save verification ID and resending token so we can use them later
                 String mVerificationId = verificationId;
                 PhoneAuthProvider.ForceResendingToken mResendToken = token;

                 // ...
             }
         };
                
    button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(Email.getText().toString())) {
            Email.setError("Fill this");
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(Email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Forget_Password.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Forget_Password.this, "Email Address is not correct", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    } });




//    private void Checknum() {
//
//        Query query=FirebaseDatabase.getInstance().getReference().child("Users")
//         .orderByChild("ph_no").equalTo(Phone_Number);
//
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//             if (dataSnapshot.exists())
//              {
//                 // Toast.makeText(Forget_Password.this, "user is = "+ dataSnapshot.getChildren(), Toast.LENGTH_SHORT).show();
//               loadingBar.setTitle("Saving Information");
//                loadingBar.setMessage("Please wait, while we are creating your new Account...");
//            loadingBar.show();
//            loadingBar.setCanceledOnTouchOutside(true);
//
//               Iterable<DataSnapshot> Children=dataSnapshot.getChildren();
//                    for (DataSnapshot child: Children)
//               {
//
//                          current_user=child.getKey();
//               }
//
//       PhoneAuthProvider.getInstance().verifyPhoneNumber("+923227044872",
//               60,
//               TimeUnit.SECONDS,
//               Forget_Password.this,
//               mcallback);
//                }
//                else {
//                 Toast.makeText(Forget_Password.this, "No such Phone number exists", Toast.LENGTH_SHORT).show();
//
//             }}
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        })   ;
//
//
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//      mauth.signInWithCredential(credential)
//               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                          //  Intent intent=new Intent(Forget_Password.this, FirstActivity.class);
//                            //startActivity(intent);
//                           // Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = task.getResult().getUser();
//                                    //  Toast.makeText(Forget_Password.this,""+ user, Toast.LENGTH_SHORT).show();
//                                     Intent intent=new Intent(Forget_Password.this, new_password.class);
//                                      intent.putExtra("user",user);
//                                    startActivity(intent);
//
//
//
//
//
//                            // ...
//                        } else {
//                            // Sign in failed, display a message and update the UI
//                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                            }
//                        }
//                    }
//                });
//    }
//
//

}       }
