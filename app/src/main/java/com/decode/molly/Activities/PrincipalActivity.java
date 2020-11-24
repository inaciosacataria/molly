package com.decode.molly.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.decode.molly.Adapter.ConversasAdapter;
import com.decode.molly.FireBaseConfig.FirebaseConfig;
import com.decode.molly.Fragments.ContactosFragment;
import com.decode.molly.Fragments.ConversasFragment;
import com.decode.molly.MainActivity;
import com.decode.molly.Models.Conversas;
import com.decode.molly.R;
import com.google.firebase.auth.FirebaseAuth;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class PrincipalActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth= FirebaseConfig.getAuth();
     private Toolbar toolbar;

    private MaterialSearchView materialSearchView;
    SmartTabLayout smartTabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        toolbar=findViewById(R.id.toolbar2);
        toolbar.setTitle("Molly");
        setSupportActionBar(toolbar);


        //frgamentos
        final FragmentPagerItemAdapter pagerItems= new FragmentPagerItemAdapter(
               getSupportFragmentManager(),FragmentPagerItems.with(this).add("Conversas", ConversasFragment.class)
                .add("Contactos", ContactosFragment.class).create());
        viewPager=findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerItems);
        smartTabLayout=findViewById(R.id.viewpagertab);
        smartTabLayout.setViewPager(viewPager);
        materialSearchView= findViewById(R.id.search_view);

        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ConversasFragment conversasFragment= (ConversasFragment) pagerItems.getPage(0);
                conversasFragment.pesquisa(newText);
                return false;
            }
        });

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ConversasFragment conversasFragment= (ConversasFragment) pagerItems.getPage(0);
                conversasFragment.atualisar();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem pesquisa= menu.findItem(R.id.btnPesquisa);
        materialSearchView.setMenuItem(pesquisa);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnPesquisa: break;
            case R.id.btnPerfil: Intent intentPerfil= new Intent(getApplicationContext(),PerfilActivity.class);
                                startActivity(intentPerfil);
                                break;
            case R.id.btnSair:
                firebaseAuth.signOut();
                firebaseAuth=null;
                Intent intentPrincipal = new Intent(this, MainActivity.class);
                startActivity(intentPrincipal);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
