package com.cengizhanbalci.sessizellerv1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class profilduzenleFragment extends Fragment {
    private StorageReference mStorageRef;
    EditText adsoyText, sehirText, telText, tarihText, meslekText;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    Button kaydet;
    ImageView resimekle;
    Uri image;
    String downloadUrl;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            image = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);

                resimekle.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilduzen, container, false);
        adsoyText = view.findViewById(R.id.adsoyText);
        sehirText = view.findViewById(R.id.sehirText);
        telText = view.findViewById(R.id.telText);
        tarihText = view.findViewById(R.id.tarihText);
        meslekText = view.findViewById(R.id.meslekText);
        kaydet = view.findViewById(R.id.button);
        resimekle = view.findViewById(R.id.resimekle);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAuth = FirebaseAuth.getInstance();
//                FirebaseUser user = mAuth.getCurrentUser();
//                System.out.println("SELAM = " + user.getUid());
//                String ad = adsoyText.getText().toString();
//                String email = user.getEmail();
//                String tarih = tarihText.getText().toString();
//                String meslek = meslekText.getText().toString();
//                String tel = telText.getText().toString();
//                String sehir = sehirText.getText().toString();
//                myRef.child(user.getUid()).child("ad").setValue(ad);
//                myRef.child(user.getUid()).child("email").setValue(email);
//                myRef.child(user.getUid()).child("meslek").setValue(meslek);
//                myRef.child(user.getUid()).child("phone").setValue(tel);
//                myRef.child(user.getUid()).child("sehir").setValue(sehir);
//                myRef.child(user.getUid()).child("tarih").setValue(tarih);

                UUID uuid = UUID.randomUUID();
                final String name = "images/" + uuid + ".jpg";
                StorageReference storageReference = mStorageRef.child(name);
                storageReference.putFile(image).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        StorageReference newReferance = FirebaseStorage.getInstance().getReference(name);
                        newReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser();
                                System.out.println("SELAM = " + user.getUid());
                                String ad = adsoyText.getText().toString();
                                String email = user.getEmail();
                                String tarih = tarihText.getText().toString();
                                String meslek = meslekText.getText().toString();
                                String tel = telText.getText().toString();
                                String sehir = sehirText.getText().toString();
                                myRef.child(user.getUid()).child("ad").setValue(ad);
                                myRef.child(user.getUid()).child("email").setValue(email);
                                myRef.child(user.getUid()).child("meslek").setValue(meslek);
                                myRef.child(user.getUid()).child("phone").setValue(tel);
                                myRef.child(user.getUid()).child("sehir").setValue(sehir);
                                myRef.child(user.getUid()).child("tarih").setValue(tarih);
                                downloadUrl = uri.toString();
                                myRef.child(user.getUid()).child("img").setValue(downloadUrl);

                            }
                        });


                    }
                }).addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG);
                    }
                });



                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                ProfilFragment profilFragment = new ProfilFragment();

                fragmentTransaction.replace(R.id.sayfa, profilFragment);
                fragmentTransaction.commit();


            }
        });

        resimekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }

            }
        });


//        emailText = view.findViewById(R.id.emailText);
//
//        adsoyText = view.findViewById(R.id.adsoyText);
//        yasText = view.findViewById(R.id.yasText);
//
//        imageView = view.findViewById(R.id.resimView);
//        buttongeri = view.findViewById(R.id.buttongeri);
//        buttonkaydet = view.findViewById(R.id.buttonkaydet);
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = firebaseDatabase.getReference();
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = firebaseDatabase.getReference("Users");
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren())
//                {
//                    HashMap<String,String> haspMap = (HashMap<String,String>) ds.getValue();
//                    String ad = haspMap.get("adsoyad");
//                    String sehir = haspMap.get("sehir");
//                    String yas = haspMap.get("yas");
//                    adsoyText.setText(ad);
//                    sehirText.setText(sehir);
//                    yasText.setText(yas);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        buttongeri.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                ProfilFragment profilFragment= new ProfilFragment();
//
//                fragmentTransaction.replace(R.id.sayfa,profilFragment);
//                fragmentTransaction.commit();
//            }
//        });
//
//        buttonkaydet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseUser user = mAuth.getCurrentUser();
//                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = firebaseDatabase.getReference();
//                String id = user.getUid();
//                String ad = adsoyText.getText().toString();
//                String yas = yasText.getText().toString();
//                String sehir = sehirText.getText().toString();
//
//                myRef.child("Users").child(id).child("adsoyad").setValue(ad);
//                myRef.child("Users").child(id).child("sehir").setValue(sehir);
//                myRef.child("Users").child(id).child("yas").setValue(yas);
//
//                Toast.makeText(getActivity(),"Profil Güncellendi !!",Toast.LENGTH_SHORT).show();
//
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                ProfilFragment profilFragment= new ProfilFragment();
//
//                fragmentTransaction.replace(R.id.sayfa,profilFragment);
//                fragmentTransaction.commit();
//
//            }
//        });


        return view;
    }
}
