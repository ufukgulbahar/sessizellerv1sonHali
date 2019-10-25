package com.cengizhanbalci.sessizellerv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import java.util.Arrays;
import java.util.List;

public class FavorilerFragment extends Fragment {
    ListView listView;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ArrayList<String> arrayList = new ArrayList<String>();
    DatabaseReference usersRef;
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoriler,container,false);
        listView = view.findViewById(R.id.favList);
        database = FirebaseDatabase.getInstance();

        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = arrayList.get(position);
                sonFragment fragment = sonFragment.newInstance(key);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.sayfa, fragment).commit();

            }
        });

        return view;
    }

    public static icerikFragment newInstance(String param1) {
        icerikFragment fragment = new icerikFragment();
        Bundle args = new Bundle();
        args.putString("kelime", param1);
        fragment.setArguments(args);
        return fragment;
    }
    public void getData()
    {
        arrayList.clear();
        usersRef = database.getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                DataSnapshot ds = dataSnapshot.child(user.getUid()).child("favori");
                String dizi = ds.getValue().toString();
                String str[] = dizi.split(",");
                List<String> al = new ArrayList<String>();
                al.clear();
                al = Arrays.asList(str);
                arrayList.clear();
                for (String deneme : al)
                {
                    if (deneme.equals(null))
                    {

                    }else
                    {
                        arrayList.add(deneme);
                    }
                }
                if (getContext()!=null)
                    adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}