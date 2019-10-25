package com.cengizhanbalci.sessizellerv1;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class sinavBasla extends Fragment {

    FirebaseDatabase database;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> liste = new ArrayList<>();
    String[] cevaplar = new String[10];

    int dogrusayisi = 0;
    int yanlissayisi = 0;

    int[] rand = new int[10];
    String videoadi = "";
    int boyut;
    int soru = 0;
    DatabaseReference myRef;
    public String name;

    VideoView videoView;
    Button reply, button1, button2, button3, button4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sinav_basla, container, false);
        database = FirebaseDatabase.getInstance();
        videoView = view.findViewById(R.id.videoView);
        MediaController mc = new MediaController(getActivity());
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setOnPreparedListener(PreparedListener);
        videoView.requestFocus();


        reply = view.findViewById(R.id.reply);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoadi.equals(button1.getText().toString())) {
                    dogrusayisi++;
                    cevaplar[soru-1] = "Doğru";
                } else {
                    yanlissayisi++;
                    cevaplar[soru-1] = "Yanlış";
                }
                sinav(1);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoadi.equals(button2.getText().toString())) {
                    dogrusayisi++;
                    cevaplar[soru-1] = "Doğru";
                } else {
                    yanlissayisi++;
                    cevaplar[soru-1] = "Yanlış";
                }
                sinav(2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoadi.equals(button3.getText().toString())) {
                    dogrusayisi++;
                    cevaplar[soru-1] = "Doğru";
                } else {
                    yanlissayisi++;
                    cevaplar[soru-1] = "Yanlış";
                }
                sinav(3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoadi.equals(button4.getText().toString())) {
                    dogrusayisi++;
                    cevaplar[soru-1] = "Doğru";
                } else {
                    yanlissayisi++;
                    cevaplar[soru-1] = "Yanlış";
                }
                sinav(4);
            }
        });


        getData();
        return view;
    }

    public void getData() {
        DatabaseReference databaseReference = database.getReference("kategoriler");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dsc : ds.getChildren()) {

                        arrayList.add(dsc.getKey());
                        System.out.println("kayedildi :" + dsc.getKey());

                    }
                }

                boyut = arrayList.size();
                System.out.println("yek" + boyut);

                final Random random = new Random();
                int sayi = random.nextInt(boyut);
                rand[0] = sayi;

                for (int i = 1; i < 10; i++) {
                    sayi = random.nextInt(boyut);
                    for (int j = 0; j <= i; j++) {
                        if (rand[j] == sayi) {
                            sayi = random.nextInt(boyut);
                            j = 0;
                        }
                    }
                    rand[i] = sayi;
                }

                liste.clear();
                for (int i = 0; i < rand.length; i++) {
                    System.out.println(i + " deneme " + rand[i]);
                    String cc = arrayList.get(rand[i]);
                    liste.add(cc);
                    System.out.println(cc);
                    if (i < rand.length - 1) {
                        System.out.println("-");
                    }
                }

                // Random sayı üret
                //Listeye at
                //Random sayılara karşılık gelen elemanları arrayliste at
                //--------------------------------------------------------------------------------------


                int[] sayı = new int[4];
                Random r = new Random();
                int butonsec = r.nextInt(3) + 1;

                for (int i = 0; i < 4; i++) {
                    sayı[i] = r.nextInt(boyut);
                    System.out.println(sayı[i]);
                }

                if (butonsec == 1) {
                    button1.setText(liste.get(soru));
                    button2.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));
                } else if (butonsec == 2) {
                    button2.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 3) {
                    button3.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button2.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 4) {
                    button4.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button2.setText(arrayList.get(sayı[3]));

                }
                videoGetir(liste.get(soru));

                soru++;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void videoGetir(final String videoid) {
        myRef = database.getReference("kategoriler");
        videoadi = videoid;
//        System.out.println("yek123 : " + videoid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds2 : ds.getChildren()) {
                        if (ds2.getKey().toString().equals(videoid)) {
                            System.out.println(ds2.child("name").getValue().toString());
                            System.out.println(ds2.child("url").getValue().toString());
                            name = ds2.child("name").getValue().toString();
                            String url = ds2.child("url").getValue().toString();
                            Uri uri = Uri.parse(url);
                            videoView.setVideoURI(uri);
                            videoView.start();

                            reply.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    videoView.start();
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


    }


    public int sinav(int a) {
        System.out.println("Soru sayisi :" + soru);
        if (soru >= 10) {
            SinavFragment fragment = new SinavFragment();
            Bundle b = new Bundle();
            b.putInt("sinavkontrol",1);
            b.putStringArray("cevaplar",cevaplar);
            b.putStringArrayList("videolar",liste);
            b.putInt("dogru",dogrusayisi);
            b.putInt("yanlis",yanlissayisi);
            fragment.setArguments(b);
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.sayfa, fragment).commit();
        }


        Random r = new Random();
        int[] sayı = new int[4];
        int butonsec = r.nextInt(3) + 1;
        for (int i = 0; i < 4; i++) {
            sayı[i] = r.nextInt(boyut);
            System.out.println(sayı[i]);
        }

        if (soru > 9) {

            System.out.println("----------SINAV BİTTİ ----------");
            soru = 0;
        }

        switch (a) {
            case 1:

                if (butonsec == 1) {
                    button1.setText(liste.get(soru));
                    button2.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));
                } else if (butonsec == 2) {
                    button2.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 3) {
                    button3.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button2.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 4) {
                    button4.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button2.setText(arrayList.get(sayı[3]));

                }

                videoGetir(liste.get(soru));
                soru++;

                break;
            case 2:


                if (butonsec == 1) {
                    button1.setText(liste.get(soru));
                    button2.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));
                } else if (butonsec == 2) {
                    button2.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 3) {
                    button3.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button2.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 4) {
                    button4.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button2.setText(arrayList.get(sayı[3]));

                }

                videoGetir(liste.get(soru));
                soru++;

                break;

            case 3:

                if (butonsec == 1) {
                    button1.setText(liste.get(soru));
                    button2.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));
                } else if (butonsec == 2) {
                    button2.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 3) {
                    button3.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button2.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 4) {
                    button4.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button2.setText(arrayList.get(sayı[3]));

                }

                videoGetir(liste.get(soru));
                soru++;

                break;
            case 4:

                if (butonsec == 1) {
                    button1.setText(liste.get(soru));
                    button2.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));
                } else if (butonsec == 2) {
                    button2.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 3) {
                    button3.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button2.setText(arrayList.get(sayı[2]));
                    button4.setText(arrayList.get(sayı[3]));

                } else if (butonsec == 4) {
                    button4.setText(liste.get(soru));
                    button1.setText(arrayList.get(sayı[1]));
                    button3.setText(arrayList.get(sayı[2]));
                    button2.setText(arrayList.get(sayı[3]));

                }

                videoGetir(liste.get(soru));
                break;

        }


        return soru;
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
