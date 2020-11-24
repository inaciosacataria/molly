package com.decode.molly.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.decode.molly.Activities.ChatActivity;
import com.decode.molly.Activities.FormarGrupoActivity;
import com.decode.molly.Adapter.ContactosAdapter;
import com.decode.molly.FireBaseConfig.FirebaseConfig;
import com.decode.molly.Helper.RecyclerItemClickListener;
import com.decode.molly.Models.Usuario;
import com.decode.molly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactosFragment extends Fragment {

    List<Usuario>u=new ArrayList<Usuario>();
    RecyclerView recyclerView;
    DatabaseReference usarioRef;
    ContactosAdapter contactosAdapter;
    ValueEventListener contactos;

    public ContactosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            usarioRef= FirebaseConfig.getReference().child("Usuarios");
        // Inflate the layout for this fragment
            View view=  inflater.inflate(R.layout.fragment_contactos, container, false);
            recyclerView= view.findViewById(R.id.recycleViewContactos);

          contactosAdapter= new ContactosAdapter(u,getContext());

           RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
           recyclerView.setLayoutManager(layoutManager);
           recyclerView.setHasFixedSize(true);
           recyclerView.setAdapter(contactosAdapter);

           recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                   getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
               @Override
               public void onItemClick(View view, int position) {
                   Intent intent= new Intent(getActivity(), ChatActivity.class);
                   Usuario usuario=u.get(position);

                   if(!usuario.getEmail().equals("")){
                   if(usuario!=null) {
                       Log.i("chat", usuario.getNome());
                       intent.putExtra("nome", usuario.getNome());
                       intent.putExtra("foto", usuario.getFoto());
                       intent.putExtra("email", usuario.getEmail());


                   }
                   startActivity(intent);
               }else{
                       Intent grupo= new Intent(getActivity(), FormarGrupoActivity.class);
                       startActivity(grupo);
                   }



               }

               @Override
               public void onLongItemClick(View view, int position) {

               }

               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               }
           }
           ) {
           });


            return view;
    }
    
    public void recuperar(){


           contactos=usarioRef.addValueEventListener(new ValueEventListener() {

               FirebaseUser testarUser= FirebaseAuth.getInstance().getCurrentUser();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u.clear();

                Usuario itemGrupo= new Usuario();
                itemGrupo.setEmail("");
                itemGrupo.setNome("Novo Grupo");
                u.add(itemGrupo);
                for (DataSnapshot  usuario: snapshot.getChildren()) {
                    Usuario usuario1=usuario.getValue(Usuario.class);

                    if(!testarUser.getEmail().equals(usuario1.getEmail())){
                        u.add(usuario1);
                    }


                }
                contactosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperar();
    }

    @Override
    public void onStop() {
        super.onStop();
        usarioRef.removeEventListener(contactos);
    }


    public void pesquisa(String s){
        List<Usuario>pesquisa= new ArrayList<Usuario>();
        for(Usuario c:u){

            String nome= c.getNome().toLowerCase();
            String mensagem= c.getEmail().toLowerCase();

            if(nome.contains(s)||mensagem.contains(s)){
               Usuario conversas1= c;
                pesquisa.add(conversas1);


            }}
        contactosAdapter= new ContactosAdapter(pesquisa,getContext());
        recyclerView.setAdapter(  contactosAdapter);
        contactosAdapter.notifyDataSetChanged();
    }
}
