package com.decode.molly.FireBaseConfig;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.decode.molly.Helper.Base64Helper;
import com.decode.molly.Models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseConfig {

    private static StorageReference firebaseStorage;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser firebaseUser;
    private static DatabaseReference databaseReference;


    public static FirebaseAuth getAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }

        return firebaseAuth;
    }

    public static DatabaseReference getReference() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static StorageReference getStorage() {
        if (firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance().getReference();
        }
        return firebaseStorage;
    }

    public static String getUserKey() {

            firebaseAuth = FirebaseAuth.getInstance();
        String key = Base64Helper.encriptar(firebaseAuth.getCurrentUser().getEmail());
        return key;
    }

    public static FirebaseUser getUserActual() {
        if (firebaseUser == null) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        }
        return firebaseUser;
    }

    public static boolean atualisarFotoUser(Uri uri) {
        try {
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri).build();
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("perfil", "erro ao atualisar ");
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualisarNome(String nome) {
        try {
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome).build();
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("perfil", "erro ao atualisar ");
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario getDadosDoUsuario(){
        FirebaseUser firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        Usuario u= new Usuario();
        u.setEmail(firebaseuser.getEmail());
        u.setNome(firebaseuser.getDisplayName());

        if(firebaseuser.getPhotoUrl()!=null) {
            u.setFoto(firebaseuser.getPhotoUrl() + "");
        }else {
            u.setFoto(firebaseuser.getPhotoUrl() + "");}

        return u;
        }


}
