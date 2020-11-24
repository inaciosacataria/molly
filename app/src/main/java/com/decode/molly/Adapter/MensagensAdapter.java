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
import com.decode.molly.Helper.Base64Helper;
import com.decode.molly.Models.Mensagens;
import com.decode.molly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.MyViewHolder> {

    List<Mensagens>msg;
    Context c;
    int TIPO_REMETENTE=0;
    int TIPO_DESTINATARIO=1;
    public MensagensAdapter(List<Mensagens> mensagens, Context context) {
        this.msg=mensagens;
        c=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==TIPO_REMETENTE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.balao_remitente_layout, parent, false);
        }else if(viewType==TIPO_DESTINATARIO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.balao_destinatario_layout, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mensagens mensagens= msg.get(position);

        String imagem=mensagens.getImagem();

            if(imagem==null||imagem.equals("")){
                holder.msg.setText(mensagens.getMensgagem());
                holder.msg.setVisibility(View.VISIBLE);
                holder.img.setVisibility(View.GONE);

            }else{
                Uri uri=Uri.parse(imagem);
                Glide.with(c).load(uri).into(holder.img);
                holder.img.setVisibility(View.VISIBLE);
                holder.msg.setVisibility(View.GONE);
            }
    }

    @Override
    public int getItemCount() {
        return msg.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagens mensagens= msg.get(position);

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String idRemitente= Base64Helper.encriptar(firebaseUser.getEmail());

        if(idRemitente.equals(mensagens.getIdUser())){
            return TIPO_REMETENTE;
        }else{
            return TIPO_DESTINATARIO;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView msg;
        private ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg=itemView.findViewById(R.id.txtMensagemTexto);
            img=itemView.findViewById(R.id.imgMensagemImagem);
        }
    }
}
