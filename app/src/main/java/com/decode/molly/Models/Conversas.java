package com.decode.molly.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;

public class Conversas implements Serializable {
    private String idRemetente, idDestinatario, ultimaMensagem;
    private Usuario usuario;

    public Conversas() {
    }

    public void salvar(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference conversasRef= databaseReference.child("Conversas");
        conversasRef.child(getIdRemetente()).child(getIdDestinatario()).setValue(this);
    }
    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
