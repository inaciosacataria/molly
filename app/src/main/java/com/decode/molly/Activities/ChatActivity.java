package com.decode.molly.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.decode.molly.Adapter.MensagensAdapter;
import com.decode.molly.FireBaseConfig.FirebaseConfig;
import com.decode.molly.Helper.Base64Helper;
import com.decode.molly.Models.Conversas;
import com.decode.molly.Models.Mensagens;
import com.decode.molly.Models.Usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decode.molly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

   private Bundle bundle;
   private TextView nome;
   private CircleImageView foto;
   private EditText txtMensagem;
   Usuario Destinatario;
    Mensagens mensagens;
    RecyclerView recyclerView;
    List<Mensagens>msg= new ArrayList<Mensagens>();
   public  String idRemetente;
    public  String idDestinatario;
   FirebaseAuth firebaseAuth;
    MensagensAdapter adapter;
    ImageView btnCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCamera= findViewById(R.id.btnCameraChat);
        bundle = getIntent().getExtras();
        txtMensagem=findViewById(R.id.editMensagem);
        foto = findViewById(R.id.imgPerfilChat);
        nome = findViewById(R.id.txtNomeChat);
        recyclerView=findViewById(R.id.recycleChat);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

         adapter= new MensagensAdapter(msg,getApplicationContext());
        recyclerView.setAdapter(adapter);




        if (bundle != null) {
         //   Usuario usuario = (Usuario) bundle.getSerializable("usuario");
            String nomechat= bundle.getString("nome");
            String fotouri=bundle.getString("foto");
            String email= bundle.getString("email");
            Destinatario= new Usuario();
            Destinatario.setFoto(fotouri);
            Destinatario.setNome(nomechat);
            Destinatario.setEmail(email);

           // if (!usuario.getFoto().equals("") ||usuario.getFoto() != null) {
            if (foto.equals("") ||foto != null) {
                Uri uri = Uri.parse(fotouri);
                Glide.with(ChatActivity.this).load(uri).into(foto);

            }else {
                foto.setImageResource(R.drawable.ic_usuario);
            }
           // nome.setText(usuario.getNome());
            nome.setText(nomechat);
        }
    }

    public void enviar(View view){

        String msg= txtMensagem.getText().toString();
        if(msg!=null || msg.equals("")){
            mensagens= new Mensagens();
            mensagens.setMensgagem(msg);


           mensagens.setIdUser(idRemetente);
           salvarMensagens(idRemetente,Destinatario,mensagens);
        }
    }
    public void salvarMensagens(String idRemetente,Usuario destinatario,Mensagens mensagens) {

        String idDestinatariio = Base64Helper.encriptar(destinatario.getEmail());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mensagensRef = databaseReference.child("Mensagens");
        mensagensRef.child(idRemetente).child(idDestinatariio).push().setValue(mensagens);
        mensagensRef.child(idDestinatariio).child(idRemetente).push().setValue(mensagens);
        criarConversa(mensagens);
        txtMensagem.setText("");
    }
    DatabaseReference mensagensRef;
   ChildEventListener childEventListener;
    public void recuperarMensagens(){
        String idDestinatariio = Base64Helper.encriptar(Destinatario.getEmail());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
         mensagensRef = databaseReference.child("Mensagens").child(idRemetente).child(idDestinatariio);

        mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagens mensagens= snapshot.getValue(Mensagens.class);
                msg.add(mensagens);
               // Log.i("aaa", mensagens.getMensgagem());
                adapter.notifyDataSetChanged();
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
        });


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        idRemetente= Base64Helper.encriptar(user.getEmail());
        recuperarMensagens();
    }

    private final int REQUEST_CAMERA=100;
    private final int REQUEST_GALERIA=200;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String idDestinatariio = Base64Helper.encriptar(Destinatario.getEmail());
        if(resultCode== RESULT_OK){
            Bitmap imagem= null;
            try {
                switch (requestCode){
                   /* case REQUEST_CAMERA:
                        imagem= (Bitmap) data.getExtras().get("data");

                        break; */

                    case REQUEST_GALERIA:
                        Uri localselecionado= data.getData();
                        imagem= MediaStore.Images.Media.getBitmap(getContentResolver(),localselecionado);

                        break;
                }
                if(imagem!=null){


                    String userKey= FirebaseConfig.getUserKey();
                    String nomeFoto= UUID.randomUUID()+"";
                    StorageReference imagemRefRemetente= FirebaseStorage.getInstance().getReference()
                            .child("imagens")
                            .child("Conversas")
                            .child(idRemetente)
                            .child(userKey+".jpeg");
                    upload(imagem,imagemRefRemetente);


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    public void upload(Bitmap imagem, final StorageReference imagemRef){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
        byte[]bytes= byteArrayOutputStream.toByteArray();

        UploadTask uploadTask= imagemRef.putBytes(bytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Ocurreu um erro ao atualizar o perfil, certifique-se que esta conectado a internet",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(ChatActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Mensagens mensagens= new Mensagens();
                        Uri uri=task.getResult();
                        mensagens.setImagem(uri+"");
                        mensagens.setMensgagem("imagem.jpeg");
                        mensagens.setIdUser(idRemetente);
                        salvarMensagens(idRemetente,Destinatario,mensagens);


                    }
                });
            }
        });


    }

    public void escolherFoto(View view){

        Intent i= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  if(i.resolveActivity(getPackageManager())!=null) {
        startActivityForResult(i, REQUEST_GALERIA);

    }


    public void criarConversa(Mensagens msg){
         String idRemetente, idDestinatario, ultimaMensagem;
         Usuario usuario;

         idRemetente= this.idRemetente;
         idDestinatario = Base64Helper.encriptar(Destinatario.getEmail());
         ultimaMensagem= msg.getMensgagem();
         Usuario  usuario1= Destinatario;

        Conversas conversas = new Conversas();
        conversas.setIdDestinatario(idDestinatario);
        conversas.setIdRemetente(idRemetente);
        conversas.setUltimaMensagem(ultimaMensagem);
        conversas.setUsuario(Destinatario);
        conversas.salvar();

    }
}
