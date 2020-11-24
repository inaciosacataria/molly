package com.decode.molly.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.decode.molly.Models.Conversas;
import com.decode.molly.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    List<Conversas>conversas;
    Context c;
    public ConversasAdapter(List<Conversas>conversas, Context context) {
        this.conversas=conversas;
        this.c=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacto_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversas conversas= this.conversas.get(position);
        if (conversas.getUsuario().getFoto()!=null || !conversas.getUsuario().getFoto().equals("") ) {
            Uri uri = Uri.parse(conversas.getUsuario().getFoto());
            Glide.with(c).load(uri).into(holder.foto);
        }else{
            holder.foto.setImageResource(R.drawable.ic_usuario);
        }
        holder.utimaMensagem.setText( conversas.getUltimaMensagem());
        holder.nome.setText( conversas.getUsuario().getNome());

    }

    @Override
    public int getItemCount() {
        return this.conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome,utimaMensagem;
        CircleImageView foto;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome= itemView.findViewById(R.id.txtNomeContacto);
            utimaMensagem= itemView.findViewById(R.id.txtEmailContacto);
            foto= itemView.findViewById(R.id.photoContacto);
        }
    }
}
