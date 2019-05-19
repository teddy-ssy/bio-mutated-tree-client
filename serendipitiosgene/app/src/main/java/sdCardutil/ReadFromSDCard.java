package sdCardutil;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import constants.Constatns;
import entity.SusceptibleAminoAcidCode;

/**
 * Created by shengyuansun on 2/9/17.
 */

public class ReadFromSDCard {

    public static boolean Checkfilebypath(String path){
        File file = new File(path);
        file.mkdirs();
        if(! file.exists()){
            Log.i("Comp","no exists");

            Log.i("create","ok");
            return true;
        }else{
            Log.i("comp","exists");
        }
        return true;
    }
    public static boolean Checkfile(String path){
        File file = new File(path);
        if(! file.exists()){
            Log.i("file","no exists");

            return false;
        }
        return true;
    }

    public static FileInputStream FileFromSDCard(String path){
        try{
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = new File(path);
                FileInputStream fis = new FileInputStream(file);
                return fis;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static ArrayList<String> getSPNamelist(String str){
        ArrayList<String> SPs=new ArrayList<String>();
        String sp="";
        int mode=0;
        for(int i=1;i<str.length();i++){
            if(str.charAt(i-1)=='>'){
                mode =1;
            }
            if(str.charAt(i)=='_' && mode==1){
                mode =0;
                SPs.add(sp);
                //Log.i("SPname",sp);
                sp="";
            }
            if(mode ==1){
                sp=sp+str.charAt(i);
            }
        }
        return SPs;
    }


    public static ArrayList<SusceptibleAminoAcidCode> getSAAlist(String str){
        ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodelist = new ArrayList<SusceptibleAminoAcidCode>();
        SusceptibleAminoAcidCode susceptibleAminoAcidCode = null;
        String sp="";
        String geneSequence="";
        int mode=0;
        for(int i=1;i<str.length();i++){
            if(str.charAt(i-1)=='>'){
                mode =1;

            }
            if(str.charAt(i)=='_' && mode==1){
                mode =0;
                susceptibleAminoAcidCode = new SusceptibleAminoAcidCode(Constatns.Pneumococcal,sp);
                sp="";
            }
            if(mode ==1){
                sp=sp+str.charAt(i);
            }
            if(Character.isUpperCase(str.charAt(i))){
                geneSequence=geneSequence+str.charAt(i);
            }
            if(str.charAt(i)=='>'||i==str.length()-1){
                susceptibleAminoAcidCode.setGeneSequence(geneSequence);
                susceptibleAminoAcidCodelist.add(susceptibleAminoAcidCode);
                geneSequence="";
            }
        }

        return susceptibleAminoAcidCodelist;
    }

    public static ArrayList<String> getCloudFileList(String str){

        ArrayList<String> oo = new ArrayList<String>();
        String string ="";


        int mode =0;
        for(int i=1;i<str.length();i++){
            if(str.charAt(i-1)=='>' && mode ==0){
                mode=1;
            }
            if(str.charAt(i)=='>'&&mode==1){
                mode=0;
                oo.add(string);
                string="";
            }
            if(mode==1){
                string =string+str.charAt(i);
            }
            if(i==str.length()-1){
                oo.add(string);
            }
        }
        for(String s :oo){
            Log.i("str",s);
        }
        return oo;
    }

}
