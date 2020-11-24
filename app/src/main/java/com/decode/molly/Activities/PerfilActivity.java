package com.decode.molly.Activities;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.decode.molly.FireBaseConfig.FirebaseConfig;
import com.decode.molly.Helper.Permissoes;
import com.decode.molly.Models.Usuario;
import com.decode.molly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {
    private StorageReference storageReference;
    private final int REQUEST_CAMERA=100;
    private final int REQUEST_GALERIA=200;
    private String [] permisoes;
    ImageButton btnCamera,btnGalleria;
    CircleImageView profile;
    FirebaseAuth firebaseAuth=FirebaseConfig.getAuth();
    FirebaseUser user;
  Toolbar toolbar;
    ImageView btnEditar;
    EditText editNomeUser;

    Usuario usuarioLogado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        toolbar=findViewById(R.id.toolbar2);
        storageReference= FirebaseConfig.getStorage();
        toolbar.setTitle("Perfil");
       // toolbar.setLogo(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        profile=findViewById(R.id.profile_image);
        final EditText editText;

       //recuperar foto


        //recuperar usuario logado
        usuarioLogado=FirebaseConfig.getDadosDoUsuario();




        btnCamera=findViewById(R.id.btnCamera);
        btnGalleria=findViewById(R.id.btnGallery);
        permisoes= new String []{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,

        };
        Permissoes.testarPermicoes(permisoes,this,1);


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               // if(i.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(i, REQUEST_CAMERA);
          //  }else {
           //     Toast.makeText(getApplicationContext(),"ee",Toast.LENGTH_SHORT).show();
          //  }
           }
        });

        btnGalleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               //  if(i.resolveActivity(getPackageManager())!=null) {
                startActivityForResult(i, REQUEST_GALERIA);
              //   }else {
              //     Toast.makeText(getApplicationContext(),"ee",Toast.LENGTH_SHORT).show();
              //    }
            }
        });

        btnEditar=findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome=editNomeUser.getText().toString();
                if( FirebaseConfig.atualisarNome(nome)){
                    usuarioLogado.setNome(nome);
                    usuarioLogado.atualisar();
                Toast.makeText(getApplicationContext(),"O seu @molly foi atualisado",Toast.LENGTH_SHORT).show();
            }}
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== RESULT_OK){
            Bitmap imagem= null;
            try {
                switch (requestCode){
                    case REQUEST_CAMERA:
                        imagem= (Bitmap) data.getExtras().get("data");

                        break;

                    case REQUEST_GALERIA:
                        Uri localselecionado= data.getData();
                        imagem= MediaStore.Images.Media.getBitmap(getContentResolver(),localselecionado);

                        break;
                }
                if(imagem!=null){
                    profile.setImageBitmap(imagem);

                    String userKey= FirebaseConfig.getUserKey();
                    StorageReference imagemRef= storageReference
                                                .child("imagens")
                                                .child("perfil")
                                                .child(userKey+".jpeg");
                    upload(imagem,imagemRef);


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void alertarPermissoe(){
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Permissoes");
        alert.setMessage(R.string.permicoes_info);
        alert.setPositiveButton("confirma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.create();
        alert.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permicoesNegadas:grantResults){
            if(permicoesNegadas== PackageManager.PERMISSION_DENIED){
                alertarPermissoe();
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
        }).addOnSuccessListener(PerfilActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                       Uri uri=task.getResult();
                       atualisarFotoUsuario(uri);


                    }
                });
            }
        });


    }
    public  void atualisarFotoUsuario(Uri uri){
     if(FirebaseConfig.atualisarFotoUser(uri)){
        usuarioLogado.setFoto(uri.toString());
        usuarioLogado.atualisar();
         Toast.makeText(getApplicationContext(),"Imagem atualisada",Toast.LENGTH_SHORT).show();
    }else{ Toast.makeText(getApplicationContext(),"nao foi possivel",Toast.LENGTH_SHORT).show();}
    }

    public void deletarImagem( StorageReference imagemRef){

        imagemRef.delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Ocurreu um erro ao atualizar o perfil, certifique-se que esta conectado a internet",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Sucesso ao deletar",Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        user=FirebaseAuth.getInstance().getCurrentUser();
        Uri uri= user.getPhotoUrl();

        if(uri!=null){
            downloadImagem(uri,profile);
        }else{
            profile.setImageResource(R.drawable.ic_usuario);
        }
        editNomeUser=findViewById(R.id.editNomePerfil);
        editNomeUser.setText(user.getDisplayName());
    }

    public void downloadImagem(Uri uri, CircleImageView imageView){

        Glide.with(this /* context */)
                .load(uri)
                .into(imageView);


    }
}
