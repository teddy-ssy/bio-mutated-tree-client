package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.SusceptibleAminoAcidCode;
import entity.Tree;
import genetree.NJArr;
import util.MatrixArr;
import util.TreePrinter;

/**
 * Created by shengyuansun on 16/10/17.
 */

public class CircleArrView extends View{

    private Context context;
    public ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodes;
    MatrixArr matrix;
    ArrayList<String> Headers;
    ArrayList<String> Sequences;
    ArrayList<ArrayList<Double>> Data;
    Tree njTree;

    public float startX1 = 0, startX2 = 0,startX3=0, startY1 = 0, startY2 = 0,startY3=0, endX1 = 0, endX2 = 0,endX3=0, endY1 = 0, endY2 = 0,endY3=0, X, Y;
    public float zoom = 0;
    public int Guestermode = 0;
    public float scaleX = getScaleX(), scaleY = getScaleY();


    public CircleArrView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Headers = new ArrayList<String>();
        Sequences = new ArrayList<String>();

        susceptibleAminoAcidCodes = Constatns.susceptibleAminoAcidCodeArrayList;
        for(int i=0;i<Constatns.Stringnumber;i++){
            if(i<Constatns.susceptibleAminoAcidCodeArrayList.size()){
                Headers.add(Constatns.susceptibleAminoAcidCodeArrayList.get(i).getSP());;
                Sequences.add(Constatns.susceptibleAminoAcidCodeArrayList.get(i).getGeneSequence());
            }
        }

        initTree();
        NJArr nj = new NJArr(matrix);
        Constatns.alllabel = matrix.getHeader();
        njTree = nj.run();
        //TreePrinter.print("print",njTree.getRootNode(),canvas,paint);


    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        canvas.translate(X + endX1 - startX1, Y + endY1 - startY1);
        canvas.scale(scaleX,scaleY);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(30);
        paint.setAntiAlias(true);
        paint.setDither(true);

        TreePrinter.print("print",njTree.getRootNode(),canvas,paint);
    }

    public void initTree(){

        double[][] data = new double[Constatns.Stringnumber][Constatns.StringLength];
        for (int i = 0; i < Constatns.StringLength; i++) {
            for (int y = 0; y < Constatns.Stringnumber; y++) {
                if(y<Constatns.susceptibleAminoAcidCodeArrayList.size()){
                    char gene = Sequences.get(y).charAt(i);
                    int count = 0;
                    for(int z= 0;z<Constatns.Stringnumber;z++) {
                        if(z<Constatns.susceptibleAminoAcidCodeArrayList.size()){
                            if(Sequences.get(y).charAt(i)==Sequences.get(z).charAt(i)) {
                                count = count+1;
                            }
                        }
                    }
                    data[y][i]= count;
                }
            }
        }
        Data = new ArrayList<ArrayList<Double>>();
        for (int i=0;i<data.length;i++){
            ArrayList<Double> temp = new ArrayList<Double>();
            for(int j=0;j<data[i].length;j++){
                temp.add(data[i][j]);
            }
            Data.add(temp);
        }
        matrix = new MatrixArr(Headers,Data);
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
            //Log.i("leave",Headers.get(current));
            //Log.i("leave",String.valueOf(current));
            //draw leaves
        }else{
            //Log.i("node",Headers.get(current));
            //Log.i("node",String.valueOf(current));
            //draw link

            //loop
            for(int i=0;i<iner.size();i++){
                InerLoop(iner.get(i),current,slope/iner.size());
            }

        }
    }

    public void root(ArrayList<ArrayList<Double>> data,int root){
        for(String str :Headers){
            //Log.i("header",str);
        }
        //Log.i("root",headers.get(root));
        //Log.i("root",Headers.get(root));
        //Log.i("root",String.valueOf(root));
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getPointerCount() == 1) {
                startX1 = event.getX();
                endX1 = startX1;
                startY1 = event.getY();
                endY1 = startY1;
            }
            else if (event.getPointerCount() > 1) {

                endX1 = startX1;

                endY1 = startY1;
                startX3 = event.getX();
                endX3 = startX3;
                startY3 = event.getY();
                endY3 = startY3;
                startX2 = event.getX(1);
                endX2 = startX2;
                startY2 = event.getY(1);
                endY2 = startY2;
            }

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if(event.getPointerCount()==2){
                startX1 = event.getX();
                endX1 = startX1;
                startY1 = event.getY();
                endY1 = startY1;
                startX3 = 0;
                startX2 = 0;
                startY3 = 0;
                startY2 = 0;
                endX3 = 0;
                endX2 = 0;
                endY3 = 0;
                endY2 = 0;
                Guestermode = 1;
            }else if(event.getPointerCount()==1){
                X = X + endX1 - startX1;
                Y = Y + endY1 - startY1;
                startX1 = 0;
                startY1 = 0;
                endX1 = 0;
                endY1 = 0;
                Guestermode = 0;
            }

        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            if (event.getPointerCount() == 1) {
                Guestermode = 1;
                endX1 = event.getX();
                endY1 = event.getY();

            } else if (event.getPointerCount() > 1) {

                endX3 = event.getX();
                endY3 = event.getY();
                endX2 = event.getX(1);
                endY2 = event.getY(1);
                Guestermode = 2;
                scaleX=getScaleX()*(1+(((startX3-startX2)+(endX3-endX2))/metrics.widthPixels));
                scaleY=getScaleY()*(1+(((startY3-startY2)+(endY3-endY2))/metrics.heightPixels));
                if(scaleX>scaleY){
                    scaleY=scaleX;
                }else{
                    scaleX=scaleY;
                }

            }

        }
        invalidate();
        return true;

    }
}
