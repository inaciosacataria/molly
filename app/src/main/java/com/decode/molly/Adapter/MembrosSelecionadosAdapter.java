package com.decode.molly.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.decode.molly.Models.Usuario;
import com.decode.molly.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembrosSelecionadosAdapter extends RecyclerView.Adapter<MembrosSelecionadosAdapter.MyViewHolder> {

    List<Usuario>u; Context c;
    public MembrosSelecionadosAdapter(List<Usuario>usuarios, Context context) {
        u=usuarios;
        c=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_selecionado,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         String[] nome= u.get(position).getNome().split(" ");
         holder.nome.setText(nome[0]);

         String foto= u.get(position).getFoto();
         if(foto.equals("")){
             holder.perfil.setImageResource(R.drawable.ic_usuario);
         }else {
             Uri uri= Uri.parse(foto);
             Glide.with(c).load(uri).into(holder.perfil);
         }


    }

    @Override
    public int getItemCount() {
        return u.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView perfil;
        TextView nome;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            perfil= itemView.findViewById(R.id.imgPerfilGrupo);
            nome= itemView.findViewById(R.id.txtNomeGrupo);
        }
    }
}
