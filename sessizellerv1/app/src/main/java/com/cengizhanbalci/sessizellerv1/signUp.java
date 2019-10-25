package com.cengizhanbalci.sessizellerv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUp extends Fragment {
    Button kayitOl,geriDon;
    private FirebaseAuth mAuth;
    EditText emailText,passwordText;
    FirebaseDatabase database;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup,container,false);
        emailText = view.findViewById(R.id.emailText);
        kayitOl = view.findViewById(R.id.kayitOl);
        geriDon = view.findViewById(R.id.geriDon);
        passwordText = view.findViewById(R.id.passwordText);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    String email = emailText.getText().toString();
                                    FirebaseUser user;
                                    user = mAuth.getCurrentUser();
                                    DatabaseReference databaseReference = database.getReference();
                                    databaseReference.child("Users").child(user.getUid()).child("email").setValue(email);
                                    databaseReference.child("Users").child(user.getUid()).child("favori").setValue("");
                                    databaseReference.child("Users").child(user.getUid()).child("meslek").setValue("-");
                                    databaseReference.child("Users").child(user.getUid()).child("sehir").setValue("-");
                                    databaseReference.child("Users").child(user.getUid()).child("tarih").setValue("-");
                                    databaseReference.child("Users").child(user.getUid()).child("phone").setValue("-");
                                    databaseReference.child("Users").child(user.getUid()).child("ad").setValue("-");
                                    databaseReference.child("Users").child(user.getUid()).child("resim").setValue("-");


                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                signIn fragment= new signIn();

                                fragmentTransaction.replace(R.id.sayfa,fragment);
                                fragmentTransaction.commit();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Hata ! ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

        });


        geriDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                signIn fragment= new signIn();

                fragmentTransaction.replace(R.id.sayfa,fragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
