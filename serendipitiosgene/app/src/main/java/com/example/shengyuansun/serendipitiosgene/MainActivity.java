package com.example.shengyuansun.serendipitiosgene;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.ResistanceGene;
import sdCardutil.ReadFromSDCard;

public class MainActivity extends Activity {

    Button button_main_printallfile;
    Button button_main_update;
    Button button_main_view_resistence_gene;
    GridView gridview_main_picture;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        ReadFromSDCard.Checkfilebypath(Constatns.filedirct);
        db = new DatabaseHelper(this);
        if(db.getNumberOfResistanceGene()==0 ){
            db.initResistanceGene();

        }
        Log.i("resistence gene count",String.valueOf(db.getNumberOfResistanceGene()));
        Constatns.RESISTANCEGENELIST = db.getAllResistanceGene(Constatns.Pneumococcal);

        //init view

        //button_main_printallfile =(Button)findViewById(R.id.button_main_view_resistence_gene);
        button_main_update =(Button) findViewById(R.id.button_main_update);
        button_main_view_resistence_gene =(Button)findViewById(R.id.button_main_view_resistence_gene);
        gridview_main_picture= (GridView) findViewById(R.id.gridview_main_picture);
        gridview_main_picture.setAdapter(getallfilename());

        //listener
        button_main_view_resistence_gene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),ResistancegeneModifyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        button_main_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),CloudUploadActivity.class);
                startActivityForResult(intent,407);
            }
        });

        gridview_main_picture.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("more operater").setMessage("are you sure you want to delect the :"+ Constatns.FILEPATHLIST.get(position).values().toString()+" or view the tree").setPositiveButton("delect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Iterator iter = Constatns.FILEPATHLIST.get(position).keySet().iterator();
                        while (iter.hasNext()) {
                            Object key = iter.next();
                            Object val = Constatns.FILEPATHLIST.get(position).get(key);
                            db.DelectFilePath(String.valueOf(key));
                            for(String name:ReadFromSDCard.getSPNamelist(ReadFromSDCard.getString(ReadFromSDCard.FileFromSDCard(String.valueOf(key))))){
                                //db.DeleteMutation(name);
                                //db.DeleteSP(name);
                            }

                        }
                        gridview_main_picture.setAdapter(getallfilename());
                    }
                }).setNegativeButton("view gene tree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplication(),CircleActivity.class);
                        //Log.i("filename",Constatns.FILEPATHLIST.get(position).values().iterator().next());
                        intent.putExtra("filename",Constatns.FILEPATHLIST.get(position).values().iterator().next());
                        startActivityForResult(intent,201);
                    }
                });
                builder.create().show();

                return true;
            }
        });

        gridview_main_picture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //last one use for add new item
                Log.i("position", String.valueOf(i));
                if (i == Constatns.FILEPATHLIST.size()) {
                    Intent intent = new Intent(getApplication(), SelectFileActivity.class);
                    startActivityForResult(intent, 101);
                } else {
                    Intent intent = new Intent(getApplication(), VisualizationExcelActivity.class);
                    String filepath = null;
                    Iterator iter = Constatns.FILEPATHLIST.get(i).keySet().iterator();
                    while (iter.hasNext()) {
                        Object key = iter.next();
                        filepath = String.valueOf(key);
                    }
                    intent.putExtra("filepath", filepath);
                    startActivityForResult(intent, 103);
                }

            }
        });

    }

    public SimpleAdapter getallfilename(){
        Constatns.FILEPATHLIST = db.getAllFilePath();
        List<Map<String, Object>> listItems =
                new ArrayList<Map<String, Object>>();
        for (int i = 0; i < Constatns.FILEPATHLIST.size(); i++)
        {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("icon", R.drawable.fastafile);
            Iterator iter = Constatns.FILEPATHLIST.get(i).keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                Object val = Constatns.FILEPATHLIST.get(i).get(key);
                listItem.put("fileName",val);
            }

            listItems.add(listItem);
        }
        Map<String, Object> listItem = new HashMap<String, Object>();
        listItem.put("icon", R.drawable.addfile);
        listItem.put("fileName", null);
        listItems.add(listItem);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this
                , listItems, R.layout.adapter_main_gridview
                , new String[]{ "icon", "fileName"}
                , new int[]{R.id.imageview_adapter_gridview_filetype, R.id.textview_adapter_gridview_filename});
        return simpleAdapter;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        if(resultCode==102){
            gridview_main_picture.setAdapter(getallfilename());
        }
        if(resultCode==202){

        }
    }

    @Override
    public void onBackPressed() {
        Log.i("TAG", "back button pressed");
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("EXIT").setMessage("are you sure you want to exit the app").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
                //ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                //manager.restartPackage(getPackageName());
                //android.os.Process.killProcess(android.os.Process.myPid());

            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();

    }
}
