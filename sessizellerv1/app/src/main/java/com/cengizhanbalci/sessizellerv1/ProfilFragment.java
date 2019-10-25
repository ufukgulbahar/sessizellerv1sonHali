package com.cengizhanbalci.sessizellerv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilFragment extends Fragment {
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseUser user;
    TextView ad,email,sehir,tel,tarih,meslek;
    DatabaseReference myRef;
    ImageView settings;
    ImageView logout;
    CircleImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil,container,false);
        settings = view.findViewById(R.id.settings);
        logout = view.findViewById(R.id.logout);
        ad = view.findViewById(R.id.adText);
        email = view.findViewById(R.id.mailText);
        sehir = view.findViewById(R.id.sehirText);
        tel = view.findViewById(R.id.telText);
        tarih = view.findViewById(R.id.tarihText);
        meslek = view.findViewById(R.id.meslekText);

        imageView =(CircleImageView)view.findViewById(R.id.imageView);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();

                DataSnapshot ds = dataSnapshot.child(user.getUid());
                ad.setText(ds.child("ad").getValue().toString());
                email.setText(ds.child("email").getValue().toString());
                sehir.setText(ds.child("sehir").getValue().toString());
                tel.setText(ds.child("phone").getValue().toString());
                tarih.setText(ds.child("tarih").getValue().toString());
                meslek.setText(ds.child("meslek").getValue().toString());
                Picasso.get().load(ds.child("img").getValue().toString()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                profilduzenleFragment profilDuzenleFragment= new profilduzenleFragment();

                fragmentTransaction.replace(R.id.sayfa,profilDuzenleFragment);
                fragmentTransaction.commit();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

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
