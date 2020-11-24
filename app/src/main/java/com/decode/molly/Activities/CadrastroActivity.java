package com.decode.molly.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.decode.molly.FireBaseConfig.FirebaseConfig;
import com.decode.molly.Models.Usuario;
import com.decode.molly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

public class CadrastroActivity extends AppCompatActivity {

    TextInputEditText editEmailCadastro, editPasswordCadastro, editNomeCadastro;
    TextView btnCadastrar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;

    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadrastro);

        //recuperarcao dos views
        editEmailCadastro=findViewById(R.id.edtiEmailCadastro);
        editNomeCadastro=findViewById(R.id.editNomeCadastro);
        editPasswordCadastro=findViewById(R.id.editPasswordCadastro);
        btnCadastrar=findViewById(R.id.btnCadastro);

        // instagias
        firebaseAuth= FirebaseConfig.getAuth();
        databaseReference=FirebaseConfig.getReference();
        progressBar= findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

    }


    //Metodos Personalisados

    public void cadastrar(View view){

        String nome=editNomeCadastro.getText().toString();
        String email=editEmailCadastro.getText().toString();
        String password=editPasswordCadastro.getText().toString();

        if(!nome.equals("")){
            if(!email.equals("")){
                if(!password.equals("")){

                    Usuario usuario= new Usuario();
                    usuario.setEmail(email);
                    usuario.setNome(nome);
                    usuario.setPassword(password);
                    usuario.setFoto("");
                    criarUsernam(usuario);

                }else{
                    Toast.makeText(getApplicationContext(),R.string.password_hint,Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),R.string.Email_hint,Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getApplicationContext(),R.string.Nome_hint,Toast.LENGTH_SHORT).show();
        }
    }

    public void criarUsernam( final  Usuario u){
        progressBar.setVisibility(View.VISIBLE);
        u.inserirNoDatabase();
        firebaseAuth.createUserWithEmailAndPassword(u.getEmail(),u.getPassword()).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Criado com sucesso",Toast.LENGTH_SHORT).show();
                    FirebaseConfig.atualisarNome(u.getNome());
                    Intent intent = new Intent(getApplicationContext(),PrincipalActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    String execao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        execao = "Escolha uma palavra-passe mais forte";
                    } catch (FirebaseAuthUserCollisionException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        execao = "Ja possui uma conta";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        execao = "digite um email valido";
                    } catch (Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        execao = "Erro ao cadastrar";
                    }
                    Toast.makeText(getApplicationContext(),execao,Toast.LENGTH_SHORT).show();


                }
            }
        });
    }


    // Fim dos metodos personalisados
}
