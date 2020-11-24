package com.decode.molly.Models;

import com.decode.molly.FireBaseConfig.FirebaseConfig;
import com.decode.molly.Helper.Base64Helper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String key;
    private String foto;
    private String nome;
    private String email;
    private String password;

    private FirebaseAuth firebaseAuth= FirebaseConfig.getAuth();
    private DatabaseReference databaseReference=FirebaseConfig.getReference();

    public Usuario(String key, String foto, String nome, String email, String password) {
        this.key = key;
        this.foto = foto;
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    public Usuario() {

    }

    public void inserirNoDatabase(){

        String key= Base64Helper.encriptar(this.getEmail());
        databaseReference.child("Usuarios").child(key).setValue(this);
        databaseReference.child("Passwords").child(key).child("email").setValue(getEmail());
        databaseReference.child("Passwords").child(key).child("password").setValue(getPassword());
    }

    public void atualisar(){
        firebaseAuth = FirebaseAuth.getInstance();
        String key = Base64Helper.encriptar(firebaseAuth.getCurrentUser().getEmail());

        DatabaseReference  databaseReference= FirebaseConfig.getReference();
        DatabaseReference userRef=databaseReference.child("Usuarios").child(key);;
        Map <String,Object> dadosUser=converterObjectoParaMap();
        userRef.updateChildren(dadosUser);
    }

    public Map<String, Object> converterObjectoParaMap(){
        HashMap<String,Object> usuarioMap= new HashMap<String , Object>();

        usuarioMap.put("nome",this.getNome());
        usuarioMap.put("email",this.getEmail());
        usuarioMap.put("foto",this.getFoto());

        return usuarioMap;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getFoto() {
        return foto;
    }


    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
