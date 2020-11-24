package com.decode.molly.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.decode.molly.Adapter.ConversasAdapter;
import com.decode.molly.Helper.Base64Helper;
import com.decode.molly.Helper.RecyclerItemClickListener;
import com.decode.molly.Models.Conversas;
import com.decode.molly.Models.Usuario;
import com.decode.molly.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    RecyclerView recyclerView;
    List<Conversas>conversas= new ArrayList<Conversas>();
    ConversasAdapter conversasAdapter;
    AdView adView  ;



// TODO: Add adView to your view hierarchy.

    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_conversas, container, false);

        FirebaseUser remetenteEmail= FirebaseAuth.getInstance().getCurrentUser();
        String idRemetende= Base64Helper.encriptar(remetenteEmail.getEmail());
        conversaRef= databaseReference.child("Conversas").child(idRemetende);




        adView=view.findViewById(R.id.adView);
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
               Toast.makeText(getActivity(),"ads",Toast.LENGTH_SHORT).show();
            }
        });


      //  adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder().build();
       adView.loadAd(adRequest);






        recyclerView=view.findViewById(R.id.recycleView);

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
       recyclerView.setLayoutManager(layoutManager);
       recyclerView.setHasFixedSize(true);

        conversasAdapter= new ConversasAdapter(conversas,getContext());
        recyclerView.setAdapter(conversasAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent= new Intent(getActivity(), ChatActivity.class);
                        Usuario usuario=conversas.get(position).getUsuario();
                        if(usuario!=null) {
                            Log.i("chat", usuario.getNome());
                            intent.putExtra("nome", usuario.getNome());
                            intent.putExtra("foto", usuario.getFoto());
                            intent.putExtra("email", usuario.getEmail());

                        }
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));




        return view;

    }

    ChildEventListener childEventListener;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    DatabaseReference conversaRef;

    String idRemetende;

    @Override
    public void onStart() {
        super.onStart();
        recuperar();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversaRef.removeEventListener(childEventListener);
    }

    public void atualisar(){
        conversasAdapter= new ConversasAdapter(conversas,getContext());
        recyclerView.setAdapter(conversasAdapter);
        conversasAdapter.notifyDataSetChanged();
    }
    public void pesquisa(String s){
        List<Conversas>pesquisa= new ArrayList<Conversas>();
        for(Conversas c:conversas){

            String nome= c.getUsuario().getNome().toLowerCase();
            String mensagem= c.getUltimaMensagem().toLowerCase();

        if(nome.contains(s)||mensagem.contains(s)){
            Conversas conversas1= c;
            pesquisa.add(conversas1);


    }}
        conversasAdapter= new ConversasAdapter(pesquisa,getContext());
        recyclerView.setAdapter(conversasAdapter);
        conversasAdapter.notifyDataSetChanged();
    }

    public void recuperar(){
        conversas.clear();
           childEventListener= conversaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Conversas c= snapshot.getValue(Conversas.class);
                conversas.add(c);
                conversasAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}



}
