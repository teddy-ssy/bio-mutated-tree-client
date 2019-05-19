package com.example.shengyuansun.serendipitiosgene;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.SusceptibleAminoAcidCode;
import sdCardutil.ReadFromSDCard;

/**
 * Created by shengyuansun on 17/10/17.
 */

public class CircleActivity extends Activity {
    DatabaseHelper DB;
    ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodes;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        Intent intent = getIntent();
        String filename = intent.getStringExtra("filename");
        DB = new DatabaseHelper(this);
        String filepath = DB.getfilePathByname(filename);
        susceptibleAminoAcidCodes = ReadFromSDCard.getSAAlist(ReadFromSDCard.getString(ReadFromSDCard.FileFromSDCard(filepath)));;
        Constatns.susceptibleAminoAcidCodeArrayList = susceptibleAminoAcidCodes;

        Constatns.StringLength = susceptibleAminoAcidCodes.get(0).getGeneSequence().length();
        for(SusceptibleAminoAcidCode susceptibleAminoAcidCode : susceptibleAminoAcidCodes){
            if(susceptibleAminoAcidCode.getGeneSequence().length()<Constatns.StringLength){
                Constatns.StringLength = susceptibleAminoAcidCode.getGeneSequence().length();
            }
        }
        Constatns.StringLength = Constatns.StringLength;
        Constatns.Stringnumber = susceptibleAminoAcidCodes.size();
        setContentView(R.layout.circle_view);

    }
}
