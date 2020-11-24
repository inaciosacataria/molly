package com.decode.molly.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissoes {

    public static boolean testarPermicoes(String[]permicoes, Activity activity,int requestCode){
        List<String>listaDePermissoes=new ArrayList<>();
        if(Build.VERSION.SDK_INT>=23){


            for (String permi: permicoes){
             boolean tempermisao=   ContextCompat.checkSelfPermission(activity,permi)== PackageManager.PERMISSION_GRANTED;
             if(!tempermisao){
                 listaDePermissoes.add(permi);
             }
            }

            if(listaDePermissoes.isEmpty()){
                return true;

            } String[]novasPermissoes= new String[listaDePermissoes.size()];
            listaDePermissoes.toArray(novasPermissoes);
            ActivityCompat.requestPermissions(activity,novasPermissoes,requestCode);
        }

       return true;
    }

}
