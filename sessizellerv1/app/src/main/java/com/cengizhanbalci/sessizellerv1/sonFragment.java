package com.cengizhanbalci.sessizellerv1;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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

public class sonFragment extends Fragment {
    String key;
    TextView textView;
    VideoView video;
    Button reply,favoriButton;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    public String name,dizi;
    FirebaseUser user;
    DatabaseReference myRef,myRef2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key = getArguments().getString("key");

        }

    }

    public static sonFragment newInstance(String param1) {
        sonFragment fragment = new sonFragment();
        Bundle args = new Bundle();
        args.putString("key", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_son,container,false);

        textView = view.findViewById(R.id.textView);
        video = view.findViewById(R.id.videoView);

        MediaController mc = new MediaController(getActivity());
        mc.setAnchorView(video);
        mc.setMediaPlayer(video);
        video.setOnPreparedListener(PreparedListener);
        video.requestFocus();

        favoriButton = view.findViewById(R.id.favoriButton);
        reply = view.findViewById(R.id.reply);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("kategoriler");
        myRef2 = database.getReference("Users");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    for(DataSnapshot ds2 : ds.getChildren())
                    {
                        if(ds2.getKey().toString().equals(key))
                        {
                            System.out.println("ANAN" + ds2.child("name").getValue().toString());
                            System.out.println("ANAN" + ds2.child("url").getValue().toString());
                            name =  ds2.child("name").getValue().toString();
                            String url =  ds2.child("url").getValue().toString();
                            textView.setText(name);
                            Uri uri = Uri.parse(url);
                            video.setVideoURI(uri);
                            video.start();

                            reply.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    video.start();
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();

                DataSnapshot ds = dataSnapshot.child(user.getUid()).child("favori");
                dizi = ds.getValue().toString();
                String str[] = dizi.split(",");
                List<String> al = new ArrayList<String>();
                al = Arrays.asList(str);
                for (String s : al) {
                    if(name.equals(s))
                        favoriButton.setText("FAVORİDEN ÇIKART");
                }

                favoriButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ad = favoriButton.getText().toString();
                        if(ad.equals("FAVORİ")) {
                            System.out.println("XXX " + name);
                            myRef2.child(user.getUid()).child("favori").setValue(dizi + "," + name);
                            favoriButton.setText("FAVORİDEN ÇIKART");
                        }
                        if(ad.equals("FAVORİDEN ÇIKART"))
                        {
                            String str[] = dizi.split(",");
                            List<String> al = new ArrayList<String>();
                            String yeni = new String();
                            al = Arrays.asList(str);
                            int i=0;
                            for (String s : al) {
                                if(name.equals(s))
                                {

                                }else if (name.equals(null)){

                                }
                                else{
                                    yeni += s + "," ;
                                }
                            }
                            String yeni2 = yeni.substring(0,yeni.length()-1);
                            System.out.println("XXX "+ yeni2);
                            myRef2.child(user.getUid()).child("favori").setValue(yeni2);
                            favoriButton.setText("FAVORİ");
                        }
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener(){

        @Override
        public void onPrepared(MediaPlayer m) {
            try {
                if (m.isPlaying()) {
                    m.stop();
                    m.release();
                    m = new MediaPlayer();
                }
                m.setVolume(0f, 0f);
                m.setLooping(false);
                m.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
