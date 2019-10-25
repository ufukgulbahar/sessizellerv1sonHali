package com.cengizhanbalci.sessizellerv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    RelativeLayout sayfa;
    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    public int fragmentkey = 0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_favori:
                    switchToFragmentFavori();
                    return true;

                    case R.id.navigation_ara:
                    switchToFragmentAra();
                    return true;

                case R.id.navigation_sozluk:
                    switchToFragmentSozluk();
                    return true;

                    case R.id.navigation_sinav:
                    switchToFragmentSinav();
                    return true;


                case R.id.navigation_profil:
                    switchToFragmentProfil();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sayfa = findViewById(R.id.sayfa);
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(user == null)
        {
            navigation.setVisibility(View.INVISIBLE);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.sayfa, new signIn()).commit();
        }
        if(user!=null)
        {
            navigation.setVisibility(View.VISIBLE);
            if (getIntent().getExtras() != null) {
                fragmentkey = getIntent().getExtras().getInt("fragmentkey");
                if (fragmentkey == 3) {
                    switchToVideoFragment();
                }
            } else {
                if (fragmentkey == 0) {
                    switchToFragmentSozluk();
                }
            }
        }




    }

    public void switchToFragmentSozluk() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.sayfa, new SozlukFragment()).commit();
    }

    public void switchToFragmentFavori() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.sayfa, new FavorilerFragment()).commit();
    }

    public void switchToFragmentProfil() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.sayfa, new ProfilFragment()).commit();
    }

    public void switchToFragmentSinav() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.sayfa, new SinavFragment()).commit();
    }

    public void switchToFragmentAra() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.sayfa, new AramaFragment()).commit();
    }

    public void switchToVideoFragment() {
        String key = getIntent().getExtras().getString("key");
        sonFragment fragment = sonFragment.newInstance(key);
        getSupportFragmentManager().beginTransaction().replace(R.id.sayfa, fragment).commit();
    }

    public void onBackPressed() {
        switchToFragmentSozluk();
    }

}
