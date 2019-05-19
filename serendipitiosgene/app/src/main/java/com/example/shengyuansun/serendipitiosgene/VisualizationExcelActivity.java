package com.example.shengyuansun.serendipitiosgene;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.excelpanel.excelpanel.ExcelPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.ResistanceGene;
import entity.SusceptibleAminoAcidCode;
import sdCardutil.ReadFromSDCard;
import views.VisualizationExcelView;

/**
 * Created by shengyuansun on 1/9/17.
 */

public class VisualizationExcelActivity extends Activity implements ExcelPanel.OnLoadMoreListener{
    //TextView textview_visualization_all_mutation;
    String filepath=null;
    DatabaseHelper db;

    public static final int PAGE_SIZE = 14;
    public static final int ROW_SIZE = 20;
    private ExcelPanel excelPanel;
    private ProgressBar progress;
    private VisualizationExcelView adapter;
    private Button button_print_excel;

    private List<String> SPnames;
    private List<List<HashMap<String,String>>> SpMutations;
    private List<ResistanceGene> resistanceGeneList;

    public views.VisualizationCircleView visualizationCircleView;
    public views.MoveView moveView;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        //setContentView(R.layout.visualization_view);
        setContentView(R.layout.visualization_circle_view);
        db = new DatabaseHelper(this);
        //
        Intent intent = getIntent();
        filepath = intent.getStringExtra("filepath");
        Log.i("filepath",filepath);
        //init view
        //textview_visualization_all_mutation = (TextView) findViewById(R.id.textview_visualization_all_mutation);
        //set listener

        //opertation
        initdata();
        //
        visualizationCircleView = (views.VisualizationCircleView) findViewById(R.id.visualization_circle_view);
        //moveView =(views.MoveView)findViewById(R.id.visualization_circle_view);

        progress = (ProgressBar) findViewById(R.id.progress);
        excelPanel = (ExcelPanel) findViewById(R.id.content_container);
        adapter = new VisualizationExcelView(this,blockListener);
        excelPanel.setAdapter(adapter);
        excelPanel.setOnLoadMoreListener(this);
        initcontext();

    }
    private View.OnClickListener blockListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onLoadHistory() {

    }
    public void initdata(){
        String text = ReadFromSDCard.getString(ReadFromSDCard.FileFromSDCard(filepath));
        ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodeArrayList = new ArrayList<SusceptibleAminoAcidCode>();
        SusceptibleAminoAcidCode susceptibleAminoAcidCode;
        SPnames= new ArrayList<String>();
        for(String sp :ReadFromSDCard.getSPNamelist(text)){
            susceptibleAminoAcidCode = new SusceptibleAminoAcidCode(Constatns.Pneumococcal,sp);
            susceptibleAminoAcidCode.setKeyPoisitionMutation(db.getMutation(Constatns.Pneumococcal,sp));
            susceptibleAminoAcidCodeArrayList.add(susceptibleAminoAcidCode);
            SPnames.add(susceptibleAminoAcidCode.getSP());
        }
        Constatns.susceptibleAminoAcidCodeArrayList = susceptibleAminoAcidCodeArrayList;
        ArrayList<ResistanceGene> resistanceGeneArrayList = db.getAllResistanceGene(Constatns.Pneumococcal);
        resistanceGeneList=resistanceGeneArrayList;
        resistanceGeneList.add(resistanceGeneArrayList.get(0));
        resistanceGeneList.add(resistanceGeneArrayList.get(0));
        resistanceGeneList.add(resistanceGeneArrayList.get(0));
        resistanceGeneList.add(resistanceGeneArrayList.get(0));
        resistanceGeneList.add(resistanceGeneArrayList.get(0));

        ArrayList<HashMap<String,String>> relist;
        SpMutations = new ArrayList<List<HashMap<String,String>>>();
        for(SusceptibleAminoAcidCode sus:Constatns.susceptibleAminoAcidCodeArrayList){
            relist = new ArrayList<HashMap<String,String>>();
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("year","attrubute");
            relist.add(map);
            map = new HashMap<String, String>();
            map.put("ST","attrubute");
            relist.add(map);
            map = new HashMap<String, String>();
            map.put("MIC","attrubute");
            relist.add(map);
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

    public void initcontext(){
        Message message = new Message();
        loadDataHandler.sendMessageDelayed(message, 1000);
    }
    private Handler loadDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            progress.setVisibility(View.GONE);
            adapter.setAllData(SPnames,resistanceGeneList,SpMutations);
            adapter.enableFooter();
            adapter.enableHeader();
        }
    };

}
