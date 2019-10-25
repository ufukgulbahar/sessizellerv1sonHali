package com.cengizhanbalci.sessizellerv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AramaFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("kategoriler");
    View view;
    ArrayList<String> arrayList = new ArrayList<>();
    EditText searchbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_arama, container, false);

        recyclerView = view.findViewById(R.id.liste);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        searchbar = view.findViewById(R.id.searchbar);

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String userInput = searchbar.getText().toString().toLowerCase();
                List<String> newList = new ArrayList<>();

                for(String name : arrayList){
                    if(name.toLowerCase().contains(userInput)){
                        newList.add(name);
                    }
                }
                adapter.updateList(newList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        for (DataSnapshot child : snapshot.getChildren()){
                            String eleman = child.getKey();
                            arrayList.add(eleman.toString());

                        }

                    }

                }

                adapter = new RecyclerAdapter(arrayList,getContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        return view;
    }
    public void updateList(List<String> newlist){
        arrayList = new ArrayList<>();
        arrayList.addAll(newlist);

    }
}
