package com.cengizhanbalci.sessizellerv1;

import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SinavFragment extends Fragment {

    int sinavkontrol = 0,dogru,yanlis;
    String[] cvplar = new String[10];
    ArrayList<String> videolar = new ArrayList<>();
    ArrayList<String> cevapanahtari = new ArrayList<>();
    VideoView video;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            sinavkontrol = getArguments().getInt("sinavkontrol");
            cvplar = getArguments().getStringArray("cevaplar");
            videolar = getArguments().getStringArrayList("videolar");
            dogru = getArguments().getInt("dogru");
            yanlis = getArguments().getInt("yanlis");
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sinav, null);
        Button sinav_basla = view.findViewById(R.id.sinav_basla);
        final ListView cevaplistesi = view.findViewById(R.id.cevaplistesi);
        TextView cevapsayisi = view.findViewById(R.id.cevapsayilari);
        video = view.findViewById(R.id.videoView);

        MediaController mc = new MediaController(getActivity());
        mc.setAnchorView(video);
        mc.setMediaPlayer(video);
        video.setOnPreparedListener(PreparedListener);
        video.requestFocus();

        video.setVisibility(View.GONE);
        if(sinavkontrol != 0){
            sinav_basla.setText("Yeniden Sınava Girin");
            cevapsayisi.setText("Sınav Sonucu \n Doğru Cevap : " + dogru + " Yanlış Cevap : " + yanlis);
            for(int i=0;i<10;i++){
                String cevap = "İşaret Adı :  " + videolar.get(i) + "   -   " + cvplar[i];
                cevapanahtari.add(cevap);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,cevapanahtari);
            cevaplistesi.setAdapter(adapter);
            cevaplistesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String key = videolar.get(position);
                    videoPlay(key);
                }
            });
        }
        else {
            cevapsayisi.setText("");
            sinav_basla.setText("Sınava Başlayın");
        }
        sinav_basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinavBasla fragment = new sinavBasla();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.sayfa, fragment).commit();
            }
        });


        return view;
    }

    public void videoPlay(final String key){
        if(video.getVisibility() == View.GONE){
            video.setVisibility(View.VISIBLE);
        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("kategoriler");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    for(DataSnapshot ds2 : ds.getChildren())
                    {
                        if(ds2.getKey().toString().equals(key))
                        {
                            String url =  ds2.child("url").getValue().toString();

                            Uri uri = Uri.parse(url);
                            video.setVideoURI(uri);
                            video.start();

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
