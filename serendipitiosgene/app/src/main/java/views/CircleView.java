package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import constants.Constatns;
import entity.SusceptibleAminoAcidCode;
import entity.Tree;
import genetree.NJ;
import util.Matrix;
import util.TreePrinter;

/**
 * Created by shengyuansun on 16/10/17.
 */

public class CircleView extends View{

    private Context context;
    public ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodes;
    Matrix matrix;
    ArrayList<String> Headers;
    ArrayList<String> Sequences;
    ArrayList<ArrayList<Double>> Data;
    String[] sequences = new String[Constatns.Stringnumber];
    String[] headers = new String[Constatns.Stringnumber];

    double[][] data = new double[Constatns.Stringnumber][Constatns.Stringnumber];


    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        susceptibleAminoAcidCodes = Constatns.susceptibleAminoAcidCodeArrayList;

        for(int i=0;i<Constatns.Stringnumber;i++){
            if(i<Constatns.susceptibleAminoAcidCodeArrayList.size()){
                headers[i] = Constatns.susceptibleAminoAcidCodeArrayList.get(i).getSP();
                sequences[i] = Constatns.susceptibleAminoAcidCodeArrayList.get(i).getGeneSequence();
            }

        }


        initTree();
        //Log.i("matrix.header size",String.valueOf(matrix.getHeader().length));
        NJ nj = new NJ(matrix);
        Tree njTree = nj.run();

        //TreePrinter.print("print",njTree.getRootNode());


        data = matrix.getData();
        headers = matrix.getHeader();

        for(int i=0;i<headers.length;i++){
            if(headers[i]!=null && headers[i] !=""){
                Log.i("dp name",headers[i]);
            }
            else{
                Log.i("dp name","null");
            }

        }

       for(int i=0;i<data.length;i++){
           for(int j=0;j<data[i].length;j++){
                   Log.i("data"+String.valueOf(i)+"+"+String.valueOf(j),String.valueOf(data[i][j]));
           }
       }


        Data = new ArrayList<ArrayList<Double>>();
        for(int i=0;i< matrix.getData().length;i++){
            ArrayList<Double> temp = new ArrayList<Double>();
            for (int j=0;j<matrix.getData()[i].length;j++){
                temp.add(matrix.getData()[i][j]);
            }
            Data.add(temp);
        }
        Headers = new ArrayList<String>();
        for(String str :matrix.getHeader()){
            Headers.add(str);
        }
        String root = njTree.getRootNode().getLabel();
        int Root=0;
        for(int i=0;i<headers.length;i++){
            if(root.equals(headers[i])){
                Root=i;
            }

        }

        root(Data,Root);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
    }

    public void initTree(){

        double[][] data = new double[Constatns.Stringnumber][Constatns.StringLength];
        for (int i = 0; i < Constatns.StringLength; i++) {
            for (int y = 0; y < Constatns.Stringnumber; y++) {
                if(y<Constatns.susceptibleAminoAcidCodeArrayList.size()){
                    char gene = sequences[y].charAt(i);
                    int count = 0;
                    for(int z= 0;z<Constatns.Stringnumber;z++) {
                        if(z<Constatns.susceptibleAminoAcidCodeArrayList.size()){
                            if(sequences[y].charAt(i)==sequences[z].charAt(i)) {
                                count = count+1;
                            }
                        }
                    }
                    data[y][i]= count;
                }
            }
        }
        matrix = new Matrix(headers,data);
    }

    //get matrix by suscode
    public ArrayList<Integer> getChilder(ArrayList<ArrayList<Double>> data,int current,int father){
       ArrayList<Integer> result = new ArrayList<Integer>();
        //get childer with out father
        ArrayList<Double> line= data.get(current);
        for(int i=0;i<line.size();i++){
            if(line.get(i) == 1){
                if(i!=father){
                    result.add(i);
                }
            }
        }
        return result;
    }

    public ArrayList<Integer> getChilder(ArrayList<ArrayList<Double>> data,int current){
        ArrayList<Integer> result = new ArrayList<Integer>();
        //get childer with out father
        ArrayList<Double> line= data.get(current);
        
        for(int i=0;i<line.size();i++){
            if(line.get(i)!= 0){
                result.add(i);
            }
        }
        return result;
    }

    public void InerLoop(int current, int father, float slope){
       ArrayList<Integer> iner=getChilder(Data,current,father);
        if(iner.size()==0){
            //leaves
            Log.i("leave",Headers.get(current));
            Log.i("leave",String.valueOf(current));
            //draw leaves
        }else{
            Log.i("node",Headers.get(current));
            Log.i("node",String.valueOf(current));
            //draw link

            //loop
            for(int i=0;i<iner.size();i++){
                InerLoop(iner.get(i),current,slope/iner.size());
            }

        }
    }

    public void root(ArrayList<ArrayList<Double>> data,int root){
        for(String str :headers){
            Log.i("header",str);
        }
        //Log.i("root",headers.get(root));
        Log.i("root",headers[root]);
        Log.i("root",String.valueOf(root));
        //draw root
        ArrayList<Integer>childer = getChilder(data,root);
        //Log.i("childer size",String.valueOf(childer.size()));
        if(childer.size()!=0){
            float slope=360/childer.size();
            for(int i=0;i<childer.size();i++){
                InerLoop(childer.get(i),root,slope);
            }
        }

    }
}
