package com.example.shengyuansun.serendipitiosgene;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.SusceptibleAminoAcidCode;
import sdCardutil.ReadFromSDCard;

/**
 * Created by shengyuansun on 1/9/17.
 */

public class SelectFileActivity extends Activity {

    ListView listView;
    TextView textView;
    File currentParent;
    File[] currentFiles;
    DatabaseHelper db;
    Button button_select_file_return_root;
    Button button_cloud;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.select_file_view);
        db = new DatabaseHelper(this);
        //init view
        listView = (ListView) findViewById(R.id.listview_select_file);
        textView = (TextView) findViewById(R.id.textview_file_select_path);
        Intent receiver=getIntent();
        File root = new File("/mnt/sdcard/");;
        if(receiver.getStringExtra("path")!=null && receiver.getStringExtra("path")!=""){
            Log.i("path",receiver.getStringExtra("path"));
            root = new File(receiver.getStringExtra("path"));
        }else{
            root = new File("/mnt/sdcard/");
        }

        if (root.exists()) {
            currentParent = root;
            currentFiles = root.listFiles();
            inflateListView(currentFiles);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("touch", "yes");
                if (currentFiles[i].isFile()) {
                    Log.i("file", "file");
                    File file = currentFiles[i];
                    String fName = file.getName();
                    boolean isFastaFile = false;
                    String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                            fName.length()).toLowerCase();
                    if (FileEnd.equals("fas") || FileEnd.equals("fasta")||FileEnd.indexOf("fas")>=0) {
                        isFastaFile = true;
                    } else {
                        isFastaFile = false;
                    }
                    if (isFastaFile) {
                        if (db.getNumberOffile(file.getPath())==0)
                        {
                            db.AddOneFilePath(file.getPath(), file.getName());
                        }
                        ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodeArrayList = ReadFromSDCard.getSAAlist(ReadFromSDCard.getString(ReadFromSDCard.FileFromSDCard(file.getPath())));
                        for (SusceptibleAminoAcidCode susceptibleAminoAcidCode : susceptibleAminoAcidCodeArrayList) {
                            if(db.getNumberOfSP(susceptibleAminoAcidCode.getSP())==0) {
                                susceptibleAminoAcidCode.match();
                                db.AddOneSP(Constatns.Pneumococcal, susceptibleAminoAcidCode.getSP());
                                for (HashMap<Integer, Character> entity : susceptibleAminoAcidCode.getKeyPoisitionMutation()) {
                                    Iterator iter = entity.keySet().iterator();
                                    while (iter.hasNext()) {
                                        Object key = iter.next();
                                        Object val = entity.get(key);
                                        db.AddOneMutationGene(Constatns.Pneumococcal, susceptibleAminoAcidCode.getSP(), (int) key, String.valueOf(val));
                                    }
                                }
                            }
                        }
                        setResult(102);
                        finish();

                    }
                    return;
                }
                Log.i("file", "not file");
                File[] tmp = currentFiles[i].listFiles();
                if (tmp == null || tmp.length == 0) {
                    Toast.makeText(SelectFileActivity.this
                            , "not file in this floder",
                            Toast.LENGTH_SHORT).show();
                    Log.i("tmp", "null");
                } else {
                    Log.i("tmp", "yes");
                    currentParent = currentFiles[i];
                    currentFiles = tmp;
                    inflateListView(currentFiles);
                }
            }
        });

        button_select_file_return_root = (Button) findViewById(R.id.button_return_selectfile_start);
        button_select_file_return_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                try {
                    if (!currentParent.getCanonicalPath()
                            .equals("/mnt/sdcard")) {
                        currentParent = currentParent.getParentFile();
                        currentFiles = currentParent.listFiles();
                        inflateListView(currentFiles);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        button_cloud =(Button)findViewById(R.id.button_select_file_return_root);
        button_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplication(),CloudViewNewActivity.class);
                Intent intent = new Intent(getApplication(),CloudViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void inflateListView(File[] files) {
        List<Map<String, Object>> listItems =
                new ArrayList<Map<String, Object>>();
        for (int i = 0; i < files.length; i++) {
            Map<String, Object> listItem =
                    new HashMap<String, Object>();
            if (files[i].isDirectory()) {
                listItem.put("icon", R.drawable.folder);
            } else {
                String fName = files[i].getName();
                boolean isFastaFile = false;
                String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                        fName.length()).toLowerCase();
                if (FileEnd.equals("fas") || FileEnd.equals("fasta")||FileEnd.equals("fas ")||FileEnd.indexOf("fas")>=0) {
                    isFastaFile = true;
                    listItem.put("icon", R.drawable.fastafile);
                } else {
                    listItem.put("icon", R.drawable.file);
                    isFastaFile = false;
                }
                //listItem.put("icon", R.drawable.file);
            }
            listItem.put("fileName", files[i].getName());
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this
                , listItems, R.layout.adapter_selectfile_listview
                , new String[]{"icon", "fileName"}
                , new int[]{R.id.imageview_filetype, R.id.textview_filepath});
        listView.setAdapter(simpleAdapter);
        try {
            textView.setText("filepath: "
                    + currentParent.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
