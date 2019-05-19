package com.example.shengyuansun.serendipitiosgene;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.ResistanceGene;
import entity.SusceptibleAminoAcidCode;

/**
 * Created by shengyuansun on 27/9/17.
 */

public class VisualizationCircleActivity extends Activity {

    public views.VisualizationCircleView visualizationCircleView;
    private List<ResistanceGene> resistanceGeneList;
    public static ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodeList;
    private List<String> SPnames;
    private List<List<HashMap<String,String>>> SpMutations;
    public DatabaseHelper db;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.visualization_circle_view);
        visualizationCircleView = (views.VisualizationCircleView) findViewById(R.id.visualization_circle_view);
        InitData();

    }

    private void InitData(){
        db = new DatabaseHelper(this);
        resistanceGeneList =db.getAllResistanceGene(Constatns.Pneumococcal);
        susceptibleAminoAcidCodeList = Constatns.susceptibleAminoAcidCodeArrayList;
        SPnames = new ArrayList<String>();
        for(SusceptibleAminoAcidCode sus : susceptibleAminoAcidCodeList){
            SPnames.add(sus.getSP());
        }
        ArrayList<HashMap<String,String>> relist;
        SpMutations = new ArrayList<List<HashMap<String,String>>>();
        for(SusceptibleAminoAcidCode sus:Constatns.susceptibleAminoAcidCodeArrayList){
            relist = new ArrayList<HashMap<String,String>>();
            HashMap<String,String> map = new HashMap<String, String>();
            for(int x=0;x<resistanceGeneList.size()-5;x++){
                //operaterList
                boolean found= false;
                for(HashMap<Integer,Character> iter:sus.getKeyPoisitionMutation()){
                    if(resistanceGeneList.get(x).getPostion()==iter.keySet().iterator().next()){
                        map = new HashMap<String,String>();
                        map.put(iter.values().iterator().next().toString(),"mutation");
                        relist.add(map);
                        found=true;
                    }
                }
                if(found==false){
                    map = new HashMap<String,String>();
                    map.put(resistanceGeneList.get(x).getOrginalCode().toString(),"NOmutation");
                    relist.add(map);
                }
            }
            map = new HashMap<String, String>();
            if(sus.getKeyPoisitionMutation().size()<3){
                map.put("fully susceptible","attrubute");
                relist.add(map);
            }else if(sus.getKeyPoisitionMutation().size()<=12){
                map.put("intermediate","attrubute");
                relist.add(map);
            }else{
                map.put("resistant","mutation");
                relist.add(map);
            }
            map = new HashMap<String, String>();
            map.put(String.valueOf(sus.getKeyPoisitionMutation().size()),"attrubute");
            relist.add(map);
            SpMutations.add(relist);
        }
    }


}
