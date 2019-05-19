package com.example.shengyuansun.serendipitiosgene;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import azureUtil.AzureUtilNew;
import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.SusceptibleAminoAcidCode;
import sdCardutil.ReadFromSDCard;

/**
 * Created by shengyuansun on 20/10/17.
 */

public class CloudUploadActivity extends Activity{

    ListView listView;
    TextView textView;
    File currentParent;
    File[] currentFiles;
    DatabaseHelper db;
    Button button_select_file_return_root;
    EditText edit_text_cloud;
    //Button button_cloud;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);

            setContentView(R.layout.select_file_to_cloud);
            db = new DatabaseHelper(this);
            //init view
            listView = (ListView) findViewById(R.id.listview_select_file);
            textView = (TextView) findViewById(R.id.textview_file_select_path);
        edit_text_cloud =(EditText)findViewById(R.id.edit_text_cloud);
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
                            UpLoad(file);
                            setResult(102);
                            finish();

                        }
                        return;
                    }
                    Log.i("file", "not file");
                    File[] tmp = currentFiles[i].listFiles();
                    if (tmp == null || tmp.length == 0) {
                        Toast.makeText(CloudUploadActivity.this
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

    private void UpLoad(File file)
    {
        try {
            final Handler handler = new Handler();

            Thread th = new Thread(new Runnable() {
                public void run() {

                    try {
                        String str = edit_text_cloud.getText().toString();
                        final String imageName = AzureUtilNew.Uploadfasta(file, file.getName(),str);

                        handler.post(new Runnable() {

                            public void run() {
                                Toast.makeText(CloudUploadActivity.this, "Fasta file Uploaded Successfully. Name = " + imageName, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch(Exception ex) {
                        final String exceptionMessage = ex.getMessage();
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(CloudUploadActivity.this, exceptionMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }});
            th.start();
        }
        catch(Exception ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
