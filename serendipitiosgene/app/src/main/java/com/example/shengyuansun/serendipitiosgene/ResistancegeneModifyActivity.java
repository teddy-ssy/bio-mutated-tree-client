package com.example.shengyuansun.serendipitiosgene;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.ResistanceGene;
import sdCardutil.ReadFromSDCard;


/**
 * Created by shengyuansun on 20/10/17.
 */

public class ResistancegeneModifyActivity extends Activity {

    Button button_database_upload;
    EditText EditText_database_add_bindingprotein;
    EditText EditText_database_add_position;
    EditText EditText_database_add_orginalcode;
    ListView listview_database_upload;
    DatabaseHelper DB;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.database_upload_view);

        DB = new DatabaseHelper(this);

        ArrayList<ResistanceGene> resistanceGenes = new ArrayList<ResistanceGene>();
        resistanceGenes = DB.getAllResistanceGene(Constatns.Pneumococcal);
        button_database_upload = (Button) findViewById(R.id.button_database_upload);
        EditText_database_add_bindingprotein = (EditText) findViewById(R.id.EditText_database_add_bindingprotein);
        EditText_database_add_position = (EditText) findViewById(R.id.EditText_database_add_position);
        EditText_database_add_orginalcode = (EditText) findViewById(R.id.EditText_database_add_orginalcode);
        listview_database_upload = (ListView) findViewById(R.id.listview_database_upload);
        InitAdapter(resistanceGenes);
        button_database_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String orginalcode = EditText_database_add_orginalcode.getText().toString();
                    String bindingprotein = EditText_database_add_bindingprotein.getText().toString();
                    int position = Integer.parseInt(EditText_database_add_position.getText().toString());
                    if(DB.getNumberOfResitanceGeneOnPosition(position)==0 && bindingprotein!=null &&bindingprotein!="" && orginalcode.length()==1 &&orginalcode!=null ){
                        DB.insertResistanceGene("Pneumococcal",bindingprotein,position,orginalcode);
                        Log.i("insert resistance gene","success");
                    }else{
                        Log.i("insert resistance gene","not success");
                    }
                    InitAdapter(DB.getAllResistanceGene(Constatns.Pneumococcal));
                    EditText_database_add_bindingprotein.setText("");
                    EditText_database_add_position.setText("");
                    EditText_database_add_orginalcode.setText("");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        listview_database_upload.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ResistancegeneModifyActivity.this);
                builder.setTitle("more operater").setMessage("are you sure you want to delect the position:"+DB.getAllResistanceGene(Constatns.Pneumococcal).get(position).getPostion() +" gene").setPositiveButton("delect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DB.DeleteResistanceGene(DB.getAllResistanceGene(Constatns.Pneumococcal).get(position).getPostion());
                        InitAdapter(DB.getAllResistanceGene(Constatns.Pneumococcal));
                    }
                }).setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
                return true;
            }
        });

    }

    public void InitAdapter(ArrayList<ResistanceGene> resistanceGenes) {
        List<Map<String, Object>> listItems =
                new ArrayList<Map<String, Object>>();
        for (int i = 0; i < resistanceGenes.size(); i++) {
            Map<String, Object> listItem =
                    new HashMap<String, Object>();
            listItem.put("bond",resistanceGenes.get(i).getBindingProtein());
            listItem.put("position", resistanceGenes.get(i).getPostion());
            listItem.put("gene",resistanceGenes.get(i).getOrginalCode());
            listItems.add(listItem);
        }


        SimpleAdapter simpleAdapter = new SimpleAdapter(this
                , listItems, R.layout.adapter_resistence_gene
                , new String[]{"bond","position", "gene"}
                , new int[]{R.id.Textview_bond, R.id.Textview_position,R.id.Textview_orgingene});
        listview_database_upload.setAdapter(simpleAdapter);
    }
}
