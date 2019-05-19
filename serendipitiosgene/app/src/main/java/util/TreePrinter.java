package util;



import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import constants.Constatns;
import entity.Node;
import entity.Point;


public class TreePrinter {

    static double slope = 360;
    static PointF position;
    static double basicAngle=0;
    static int basicWidth =100;

    public static void print(String header, Node rootNode, Canvas canvas,Paint paint) {
       
        //StringBuilder sb = new StringBuilder();
        Log.i("root",rootNode.getLabel() );
        //sb = renderNode(rootNode, 0, sb, false);
        double tempSlope=360/rootNode.getChildren().size();
        if(tempSlope>120){
            tempSlope=120;
        }
        double tempBasicAngle = -(tempSlope/2);
        renderNode(rootNode, 0,false,360,tempBasicAngle,canvas,paint);


    }

    private static void indent( int level, boolean isLast) {
        Log.i("level",String.valueOf(level));
        Log.i("isLast",String.valueOf(isLast));
    }

    private static void renderNode(Node node, int level, boolean isLast) {

        Log.i("node:",node.getLabel());
        indent(level, isLast);
        List<Node> list = node.getChildren();

        for (int i = 0; i < list.size(); i++) {
            boolean last = ((i + 1) == list.size());
            if (list.get(i).getChildren().size() > 0) {
                renderNode(list.get(i), level + 1, last);
            } else {
                renderLeaf(list.get(i), level + 1, last);
            }
        }

    }

    private static void renderLeaf(Node node, int level, boolean isLast) {
        Log.i("leaf:",node.getLabel());
        indent( level, isLast);
    }

    //
    private static void renderNode(Node node, int level, boolean isLast,double slope,double basicAngle,Canvas canvas,Paint paint) {

        Log.i("node:",node.getLabel());
        indent(level, isLast,slope,basicAngle);
        List<Node> list = node.getChildren();
        if(level==0){
            DrawRoot(canvas,paint,list.size(),node);
        }else{
            if(list.size()>0){
                DrawNode(level,slope,basicAngle,list.size(),canvas,paint);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            boolean last = ((i + 1) == list.size());
            double tempSlope = slope/list.size();
            if(tempSlope>120){
                tempSlope=120;
            }
            double tempBasicAngle = basicAngle+(i*tempSlope);
            if (list.get(i).getChildren().size() > 0) {

                renderNode(list.get(i), level + 1, last,tempSlope,tempBasicAngle,canvas,paint);
            } else {
                renderLeaf(list.get(i), level + 1, last,tempSlope,tempBasicAngle,canvas,paint);
            }
        }

    }

    private static void renderLeaf(Node node, int level, boolean isLast,double slope,double basicAngle,Canvas canvas,Paint paint) {
        Log.i("leaf:",node.getLabel());
        indent( level, isLast,slope,basicAngle);
        DrawLeaf(level,slope,basicAngle,canvas,paint,node);
    }

    private static void indent( int level, boolean isLast,double slope,double basicAngle) {
        Log.i("level",String.valueOf(level));
        Log.i("isLast",String.valueOf(isLast));
        Log.i("slope",String.valueOf(slope));
        Log.i("basicAngle",String.valueOf(basicAngle));
    }

    public static void DrawRoot(Canvas canvas, Paint paint,int numberOfChilder,Node node){
        position = new PointF(0,0);
        position.set(position.x+canvas.getWidth()/2,position.y+canvas.getHeight()/2);
        Log.i("root position","X-"+position.x+"||||"+"Y-"+position.y);
        paint.setColor(Color.RED);
        canvas.drawCircle(position.x,position.y,5,paint);

        for(int i=0;i<numberOfChilder;i++){
            double tempslope=  360/numberOfChilder;
            double tempbasicAngle = tempslope/2+(i*tempslope);
            float X =(float)Math.sin((tempbasicAngle+(tempslope/2))/180*Math.PI)*basicWidth;
            float Y =(float)Math.cos((tempbasicAngle+(tempslope/2))/180*Math.PI)*basicWidth;
            paint.setColor(Color.GREEN);
            canvas.drawLine(position.x,position.y,X+(canvas.getWidth()/2),Y+(canvas.getHeight()/2),paint);
            for(String str: Constatns.alllabel){
                if(node.getLabel()==str){
                    paint.setStrokeWidth(1);
                    paint.setColor(Color.BLACK);
                    canvas.drawText(node.getLabel(),position.x+10,position.y+10,paint);
                    paint.setStrokeWidth(5);
                }
            }

        }

    }

    public static void DrawNode(int level ,double slope,double basicAngle,int numberOfChilder,Canvas canvas,Paint paint){
        float X =(float)Math.sin((basicAngle+(slope/2))/180*Math.PI)*basicWidth*level;
        float Y =(float)Math.cos((basicAngle+(slope/2))/180*Math.PI)*basicWidth*level;
        position = new PointF(X,Y);
        position.set(position.x+canvas.getWidth()/2,position.y+canvas.getHeight()/2);
        Log.i("node position","X-"+position.x+"||||"+"Y-"+position.y);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(position.x,position.y,5,paint);
        for(int i=0;i<numberOfChilder;i++){
            double tempslope=  slope/numberOfChilder;
            double tempbasicAngle = basicAngle+(i*tempslope);
            float X1 =(float)Math.sin((tempbasicAngle+(tempslope/2))/180*Math.PI)*basicWidth*(level+1);
            float Y1 =(float)Math.cos((tempbasicAngle+(tempslope/2))/180*Math.PI)*basicWidth*(level+1);
            paint.setColor(Color.GREEN);
            canvas.drawLine(position.x,position.y,X1+(canvas.getWidth()/2),Y1+(canvas.getHeight()/2),paint);
        }
    }

    public static void DrawLeaf(int level ,double slope,double basicAngle,Canvas canvas, Paint paint,Node node){
        float X =(float)Math.sin((basicAngle+(slope/2))/180*Math.PI)*basicWidth*level;
        float Y =(float)Math.cos((basicAngle+(slope/2))/180*Math.PI)*basicWidth*level;
        position = new PointF(X,Y);
        position.set(position.x+canvas.getWidth()/2,position.y+canvas.getHeight()/2);
        Log.i("leaf position","X-"+position.x+"||||"+"Y-"+position.y);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(position.x,position.y,5,paint);
        paint.setStrokeWidth(1);
        canvas.drawText(node.getLabel(),position.x+10,position.y+10,paint);
        paint.setStrokeWidth(5);

    }
}
