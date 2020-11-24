package com.decode.molly.Models;

public class Mensagens {
    private String idUser;
    private String mensgagem;
    private String imagem;

    public Mensagens(String idUser, String mensgagem, String imagem) {
        this.idUser = idUser;
        this.mensgagem = mensgagem;
        this.imagem = imagem;
    }

    public Mensagens() {
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getMensgagem() {
        return mensgagem;
    }

    public void setMensgagem(String mensgagem) {
        this.mensgagem = mensgagem;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
