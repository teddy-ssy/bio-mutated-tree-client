package entity;

import android.service.autofill.SaveInfo;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import constants.Constatns;

/**
 * Created by shengyuansun on 1/9/17.
 */

public class SusceptibleAminoAcidCode implements Serializable {
    String StrainClass;
    String SP;
    int year;
    int ST;
    String MIC;
    String geneSequence;

    public ArrayList<HashMap<Integer,Character>> keyPoisitionMutation = new ArrayList<HashMap<Integer, Character>>();
    //public ArrayList<Character> geneSequence = new ArrayList<Character>();

    public String getSP() {
        return SP;
    }

    public void setSP(String SP) {
        this.SP = SP;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getST() {
        return ST;
    }

    public void setST(int ST) {
        this.ST = ST;
    }

    public String getMIC() {
        return MIC;
    }

    public void setMIC(String MIC) {
        this.MIC = MIC;
    }

    public ArrayList<HashMap<Integer, Character>> getKeyPoisitionMutation() {
        return keyPoisitionMutation;
    }

    public void setKeyPoisitionMutation(ArrayList<HashMap<Integer, Character>> keyPoisitionMutation) {
        this.keyPoisitionMutation = keyPoisitionMutation;
    }

    public String getStrainClass() {
        return StrainClass;
    }

    public void setStrainClass(String strainClass) {
        StrainClass = strainClass;
    }

    public void addNewmutation(int position , Character character){
        HashMap<Integer,Character> Resistantgene  = new HashMap<Integer,Character>();
        Resistantgene.put(position,character);
        keyPoisitionMutation.add(Resistantgene);
    }

    public String getGeneSequence() {
        return geneSequence;
    }

    public void setGeneSequence(String geneSequence) {
        this.geneSequence = geneSequence;
    }

    public SusceptibleAminoAcidCode(String strainClass, String SP){
        this.StrainClass = strainClass;
        this.SP = SP;
        this.keyPoisitionMutation = new ArrayList<HashMap<Integer, Character>>();
    }

    public void match(){
        for(ResistanceGene resistanceGene:Constatns.RESISTANCEGENELIST){
            //Log.i("sp: ",this.getSP());
            //Log.i(resistanceGene.getPostion()+","+resistanceGene.getOrginalCode(),String.valueOf(geneSequence.charAt(resistanceGene.getPostion() -1)));
            if(resistanceGene.getOrginalCode().equals(String.valueOf(geneSequence.charAt(resistanceGene.getPostion() -1)))){

            }else{
                HashMap<Integer,Character> key = new HashMap<Integer, Character>();
                key.put(resistanceGene.getPostion(),geneSequence.charAt(resistanceGene.getPostion() -1));
                this.keyPoisitionMutation.add(key);
            }
        }
    }
}
