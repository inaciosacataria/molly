package com.decode.molly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.decode.molly.Activities.CadrastroActivity;
import com.decode.molly.Activities.PrincipalActivity;
import com.decode.molly.FireBaseConfig.FirebaseConfig;
import com.decode.molly.Models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    TextInputEditText editEmailLogin, editPasswordLogin;
    Button btnLogar;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar= findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        editEmailLogin=findViewById(R.id.editEmailogin);
        editPasswordLogin=findViewById(R.id.editPasswordLogin);
        btnLogar=findViewById(R.id.btnLogar);

        firebaseAuth = FirebaseConfig.getAuth();
        databaseReference= FirebaseConfig.getReference();

    }


    //Metodos personalisados
    public void cadastro(View view){
        Intent intent = new Intent(this, CadrastroActivity.class);
        startActivity(intent);
    }
    public void Login(View view){

        String email=editEmailLogin.getText().toString();
        String password=editPasswordLogin.getText().toString();
        if (!email.equals("")){
            if (!password.equals("")){
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent= new Intent(getApplicationContext(), PrincipalActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            String excessao ="";
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidCredentialsException e) {
                                excessao = "Email e senha nao correspodem";
                            }catch (FirebaseAuthInvalidUserException e){
                                excessao="Usuario nao existe!";
                            }catch (Exception e){
                                excessao="erro ao logar";
                            }
                            Toast.makeText(getApplicationContext(),excessao,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(),R.string.password_hint,Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),R.string.Email_hint,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent = new Intent(this,PrincipalActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
