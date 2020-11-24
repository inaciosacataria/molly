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

public class ContactosAdapter extends RecyclerView.Adapter <ContactosAdapter.MyViewHolder>{

    private List<Usuario> u;
    private Context c;
    public ContactosAdapter(List<Usuario>usuarios, Context context) {
        u=usuarios;
        c=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.contacto_layout,parent,false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario user=u.get(position);
        holder.email.setText(user.getEmail());
        holder.nome.setText(user.getNome());
        if(!user.getEmail().equals("")){
            holder.email.setVisibility(View.VISIBLE);
        if(user.getFoto().equals("")||user.getFoto()==null){
       holder.foto.setImageResource(R.drawable.ic_usuario);
        }else {
            Uri uri=Uri.parse(user.getFoto());
            Glide.with(c).load(uri).into(holder.foto);
        }}else {
            holder.foto.setImageResource(R.drawable.icone_grupo);
            holder.email.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return u.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foto= itemView.findViewById(R.id.photoContacto);
            nome=itemView.findViewById(R.id.txtNomeContacto);
            email=itemView.findViewById(R.id.txtEmailContacto);
        }
    }}
