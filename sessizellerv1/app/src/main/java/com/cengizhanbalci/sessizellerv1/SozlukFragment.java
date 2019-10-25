package com.cengizhanbalci.sessizellerv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SozlukFragment extends Fragment {
    private FirebaseAuth mAuth;
    ListView liste;
    List<String> lst;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sozluk,container,false);
        liste = view.findViewById(R.id.favList);
        database = FirebaseDatabase.getInstance();



        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            signIn fragment = new signIn();

            fragmentTransaction.replace(R.id.sayfa,fragment);
            fragmentTransaction.commit();
        }


        getData();


        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String kelime = lst.get(position);
                icerikFragment icerikfragment = icerikFragment.newInstance(kelime);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.sayfa, icerikfragment).commit();
            }
        });

        return view;
    }

    public void getData()
    {
        DatabaseReference databaseReference = database.getReference("kategoriler");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lst = new ArrayList<String>();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    lst.add(String.valueOf(ds.getKey()));
                }

               ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()),android.R.layout.simple_list_item_1,lst);
                liste.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
