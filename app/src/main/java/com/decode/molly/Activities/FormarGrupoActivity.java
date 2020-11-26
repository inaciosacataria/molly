package com.decode.molly.Activities;

import android.os.Bundle;

import com.decode.molly.Adapter.ContactosAdapter;
import com.decode.molly.Adapter.MembrosSelecionadosAdapter;
import com.decode.molly.FireBaseConfig.FirebaseConfig;
import com.decode.molly.Helper.RecyclerItemClickListener;
import com.decode.molly.Models.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.decode.molly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FormarGrupoActivity extends AppCompatActivity {


    List<Usuario> usuarios=new ArrayList<Usuario>();
    List<Usuario> usuariosSelecionados=new ArrayList<Usuario>();
    RecyclerView recyclerMembros, recyclerContactosEscolhidos;
    ContactosAdapter adapter;
    MembrosSelecionadosAdapter adapterMembros;
    ValueEventListener valueEventListenerUsuario;
    DatabaseReference usuarioRef= FirebaseConfig.getReference().child("Usuarios");
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_formar_grupo);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Social Online");

        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerContactosEscolhidos=findViewById(R.id.recyclerContactosSelecionados);
        recyclerMembros=findViewById(R.id.recyclerMembros);

         adapter = new ContactosAdapter(usuarios,getApplicationContext());
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerMembros.setLayoutManager(layoutManager);
        recyclerMembros.setHasFixedSize(true);
        recyclerMembros.setAdapter(adapter);
       // recuperar();


        recyclerMembros.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerMembros,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        Usuario usuario= usuarios.get(position);
                        usuariosSelecionados.add(usuario);

                        usuarios.remove(usuario);

                        adapterMembros = new MembrosSelecionadosAdapter(usuariosSelecionados,getApplicationContext());
                        RecyclerView.LayoutManager layoutManagerHorizontal= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                        recyclerContactosEscolhidos.setLayoutManager(layoutManagerHorizontal);
                        recyclerContactosEscolhidos.setAdapter(adapterMembros);
                        recyclerContactosEscolhidos.setHasFixedSize(true);
                        adapter.notifyDataSetChanged();
                        atualisarQuantidadeDeMembros();


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));

        recyclerContactosEscolhidos.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(), recyclerContactosEscolhidos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuario= usuariosSelecionados.get(position);
                usuarios.add(usuario);
                usuariosSelecionados.remove(usuario);

                adapter.notifyDataSetChanged();
                adapterMembros.notifyDataSetChanged();

                atualisarQuantidadeDeMembros();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }
        ));
    }

    public void atualisarQuantidadeDeMembros(){
        int nr =usuariosSelecionados.size(), totalDecontactos=usuarios.size()+nr;
        toolbar.setSubtitle(nr+" de " +totalDecontactos+" selecionado");
    }
    public void recuperar(){

    valueEventListenerUsuario=usuarioRef.addValueEventListener(new ValueEventListener() {

            FirebaseUser testarUser= FirebaseAuth.getInstance().getCurrentUser();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarios.clear();


                for (DataSnapshot  usuario: snapshot.getChildren()) {
                    Usuario usuario1=usuario.getValue(Usuario.class);

                    if(!testarUser.getEmail().equals(usuario1.getEmail())){
                        usuarios.add(usuario1);
                    }


                }

                adapter.notifyDataSetChanged();
                atualisarQuantidadeDeMembros();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
      recuperar();

    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
    }
}
