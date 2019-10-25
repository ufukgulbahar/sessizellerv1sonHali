package com.cengizhanbalci.sessizellerv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class icerikFragment extends Fragment {
    public String kelime;
    ListView icerikListe;
    FirebaseDatabase database;
    List<String> icerikler;
    TextView textView;
    String text;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kelime = getArguments().getString("kelime");
        }
    }

    public static icerikFragment newInstance(String param1) {
        icerikFragment fragment = new icerikFragment();
        Bundle args = new Bundle();
        args.putString("kelime", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icerik,container,false);

        database = FirebaseDatabase.getInstance();
        icerikListe = view.findViewById(R.id.favList);

        textView = view.findViewById(R.id.textView);
        textView.setText(kelime);
        getData();


        icerikListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = icerikler.get(position);
                sonFragment fragment = sonFragment.newInstance(key);

                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.sayfa, fragment).commit();
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

                icerikler = new ArrayList<String>();

                for(DataSnapshot ds: dataSnapshot.child(kelime).getChildren())
                {
                    icerikler.add(String.valueOf(ds.getKey()));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()),android.R.layout.simple_list_item_1,icerikler);
                icerikListe.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
