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

public class signIn extends Fragment {
    private FirebaseAuth mAuth;
    Button girisYap,kayitOl;
    EditText emailText,passwordText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin,container,false);
        emailText = view.findViewById(R.id.emailText);
        passwordText = view.findViewById(R.id.passwordText);
        girisYap = view.findViewById(R.id.girisYap);
        kayitOl = view.findViewById(R.id.kayitOl);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            SozlukFragment fragment = new SozlukFragment();

            fragmentTransaction.replace(R.id.sayfa,fragment);
            fragmentTransaction.commit();
        }
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailText.getText().toString() == null)
                    Toast.makeText(getActivity(),"Boş alan bırakmayınız !!", Toast.LENGTH_SHORT).show();
                if(passwordText.getText().toString() == null)
                    Toast.makeText(getActivity(),"Boş alan bırakmayınız !!", Toast.LENGTH_SHORT).show();
                if(emailText.getText().toString() != null && passwordText.getText().toString() != null) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                        SozlukFragment fragment = new SozlukFragment();

                                        fragmentTransaction.replace(R.id.sayfa, fragment);
                                        fragmentTransaction.commit();
                                    }
                                }
                            }).addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getActivity(),"hata "+e.getLocalizedMessage() ,Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });


        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                signUp fragment = new signUp();

                fragmentTransaction.replace(R.id.sayfa,fragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
