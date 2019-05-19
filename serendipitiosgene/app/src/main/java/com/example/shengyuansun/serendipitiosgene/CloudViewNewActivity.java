package com.example.shengyuansun.serendipitiosgene;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import azureUtil.AzureUtil;
import azureUtil.DownUtil;
import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.SusceptibleAminoAcidCode;
import sdCardutil.ReadFromSDCard;

/**
 * Created by shengyuansun on 20/10/17.
 */

public class CloudViewNewActivity extends Activity {

    String[] names;
    ListView lvDownloadFileList;
    ProgressBar Downloadbar;
    ArrayList<String> filename;
    ArrayList<String> filepath;
    DatabaseHelper DB;
    final ByteArrayOutputStream fileStream = new ByteArrayOutputStream();
    DownUtil downUtil;
    private int mDownStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main__activity_main);

        DB = new DatabaseHelper(this);
        ReadFromSDCard.Checkfilebypath(Constatns.filedirct);

        lvDownloadFileList = (ListView) findViewById(R.id.lvDownloadFileList);
        Downloadbar = (ProgressBar) findViewById(R.id.Downloadbar);
        //
        final Handler handler1 = new Handler();

        Thread th = new Thread(new Runnable() {
            public void run() {

                try {

                    final String[] files = AzureUtil.Listallfile();
                    final String[] paths = AzureUtil.ListallfilePath();

                    handler1.post(new Runnable() {

                        public void run() {
                            names = files;
                            filename = new ArrayList<String>();
                            filepath = new ArrayList<String>();
                            for(int i=0;i<files.length;i++){
                                filename.add(files[i]);
                                filepath.add(paths[i]);
                            }
                            inflateListView(filename,filepath);
                        }
                    });
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }});
        th.start();

        //

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    Downloadbar.setProgress(mDownStatus);
                }
            }
        };

        //inflateListView(filename,filepath);

        lvDownloadFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = filename.get(position);
                String path = filepath.get(position);
                downUtil = new DownUtil(path,
                        Constatns.filedirct + name, 6);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            downUtil.download();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                double completeRate = downUtil.getCompleteRate();
                                mDownStatus = (int) (completeRate * 100);

                                handler.sendEmptyMessage(0x123);
                                if (mDownStatus >= 100) {
                                    timer.cancel();
                                    //
                                    File currentFiles = new File(Constatns.filedirct + name);
                                    if (currentFiles.isFile()) {
                                        Log.i("file", "file");
                                        File file = currentFiles;
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
                                            if (DB.getNumberOffile(file.getPath())==0)
                                            {
                                                DB.AddOneFilePath(file.getPath(), file.getName());
                                                Log.i("add","DB");
                                            }
                                            ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodeArrayList = ReadFromSDCard.getSAAlist(ReadFromSDCard.getString(ReadFromSDCard.FileFromSDCard(file.getPath())));
                                            for (SusceptibleAminoAcidCode susceptibleAminoAcidCode : susceptibleAminoAcidCodeArrayList) {
                                                if(DB.getNumberOfSP(susceptibleAminoAcidCode.getSP())==0) {
                                                    susceptibleAminoAcidCode.match();
                                                    DB.AddOneSP(Constatns.Pneumococcal, susceptibleAminoAcidCode.getSP());
                                                    for (HashMap<Integer, Character> entity : susceptibleAminoAcidCode.getKeyPoisitionMutation()) {
                                                        Iterator iter = entity.keySet().iterator();
                                                        while (iter.hasNext()) {
                                                            Object key = iter.next();
                                                            Object val = entity.get(key);
                                                            DB.AddOneMutationGene(Constatns.Pneumococcal, susceptibleAminoAcidCode.getSP(), (int) key, String.valueOf(val));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }else{
                                        Log.i("file", " no file");
                                    }
                                    Intent intent = new Intent(getApplication(),MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("path",Constatns.filedirct);
                                    startActivity(intent);
                                }
                            }
                        }, 0, 100);
                    }
                }.start();
            }
        });
    }

    private void inflateListView(ArrayList<String> name,ArrayList<String> path) {
        List<Map<String, Object>> listItems =
                new ArrayList<Map<String, Object>>();
        for (int i = 0; i < name.size(); i++) {
            Map<String, Object> listItem =
                    new HashMap<String, Object>();
            if(DB.getfilePathByname(name.get(i))!=null){
                listItem.put("icon",R.drawable.clouddownload);
            }else {
                listItem.put("icon",R.drawable.cloudadd);
            }
            listItem.put("filename",name.get(i));
            listItem.put("filepath", path.get(i));
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this
                , listItems, R.layout.main__item_download
                , new String[]{"filename", "filepath","icon"}
                , new int[]{R.id.Textview_filename, R.id.Textview_filepath,R.id.cloudicon});
        lvDownloadFileList.setAdapter(simpleAdapter);

    }

}
