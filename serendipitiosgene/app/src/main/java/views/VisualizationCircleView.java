package views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.shengyuansun.serendipitiosgene.R;
import com.example.shengyuansun.serendipitiosgene.VisualizationCircleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.SusceptibleAminoAcidCode;

/**
 * Created by shengyuansun on 1/9/17.
 */

public class VisualizationCircleView extends View {

    private Context context;
    public DatabaseHelper db;
    public float startX1 = 0, startX2 = 0,startX3=0, startY1 = 0, startY2 = 0,startY3=0, endX1 = 0, endX2 = 0,endX3=0, endY1 = 0, endY2 = 0,endY3=0, X, Y;

    public float zoom = 0;
    //record Horizontal zoom
    //init zoomX=0

    public int Guestermode = 0;
    //no figure mode =0
    //one figure mode = 1 drag
    //two figure mode = 2 zoomX

    public float moveX = 0;
    public float moveY = 0;
    int width;
    float positionzoom;
    public float scaleX = getScaleX(), scaleY = getScaleY();


    public VisualizationCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(X + endX1 - startX1, Y + endY1 - startY1);
        canvas.scale(scaleX,scaleY);
        //canvas.translate(moveX,moveY);
        canvas.drawColor(Color.WHITE);
        Paint paintPBP = new Paint();
        paintPBP.setStrokeWidth(20);
        paintPBP.setStyle(Paint.Style.FILL_AND_STROKE);
        initDrawCircle(canvas, paintPBP);
        DrawGeneList(Constatns.susceptibleAminoAcidCodeArrayList, canvas);

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

    public boolean initDrawCircle(Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(100, 170, 221, 225));//pbp1a
        if (this.getWidth() > this.getHeight()) {
            width = ((this.getHeight() / 2) - 100) / 4;
        } else {
            width = ((this.getWidth() / 2) - 100) / 4;
        }
        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, (this.getWidth() / 2) - 50, paint);
        paint.setColor(Color.argb(100, 67, 180, 224));//pbp1b
        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, (this.getWidth() / 2) - 50 - width, paint);
        paint.setColor(Color.argb(100, 29, 143, 218));//pbp2a
        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, (this.getWidth() / 2) - 50 - width - width, paint);
        paint.setColor(Color.argb(100, 5, 114, 186));//pbp2b
        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, (this.getWidth() / 2) - 50 - width - width - width, paint);
        //paint.setColor(Color.argb(100,255,255,255));
        //canvas.drawCircle(this.getWidth()/2,this.getHeight()/2,(this.getWidth()/2)-50-width-width-width-width,paint);
        Log.i("width= " + String.valueOf(this.getWidth()), "heigh= " + String.valueOf(this.getHeight()));
        return true;
    }

    public boolean DrawOneGene(SusceptibleAminoAcidCode sus, double Slope, Canvas canvas) {
        Paint paintSus = new Paint();
        paintSus.setColor(Color.BLACK);
        paintSus.setStyle(Paint.Style.FILL_AND_STROKE);
        paintSus.setStrokeWidth(5);
        //4width = 2668
        positionzoom = 2668 / (4 * width);
        //draw main genechain
        //
        double angle = Slope / 180 * Math.PI;
        float Xposition = (float) (Math.cos(angle) * ((4 * width) + 80));
        float Yposition = (float) (Math.sin(angle) * ((4 * width) + 80));
        //
        if (Slope == 0) {
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope == 90) {
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope == 180) {
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope == 270) {
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope < 90 && Slope > 0) {//one
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope > 90 && Slope < 180) {//two
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope > 180 && Slope < 270) {//three
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope > 270 && Slope < 360) {//four
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        }
        //
        canvas.drawLine(this.getWidth() / 2, this.getHeight() / 2, Xposition, Yposition, paintSus);
        paintSus.setStrokeWidth(2);
        paintSus.setTextSize(20);
        canvas.drawText(sus.getSP(),Xposition+5,Yposition,paintSus);

        //draw mutation tag
        for (HashMap<Integer, Character> entity : sus.getKeyPoisitionMutation()) {
            int point = entity.keySet().iterator().next();
            Character mutationto = entity.values().iterator().next();
            //draw cross;
            DrawCross(point, mutationto, Slope, canvas);
        }
        return true;
    }

    public void DrawCross(int position, Character mutation, double Slope, Canvas canvas) {
        //
        double angle = Slope / 180 * Math.PI;
        float Xposition = (float) (Math.cos(angle) * ((position / positionzoom) + 50));
        float Yposition = (float) (Math.sin(angle) * ((position / positionzoom) + 50));

        if (Slope == 0) {
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope == 90) {
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope == 180) {
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope == 270) {
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope < 90 && Slope > 0) {//one
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope > 90 && Slope < 180) {//two
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope > 180 && Slope < 270) {//three
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        } else if (Slope > 270 && Slope < 360) {//four
            Xposition = (this.getWidth() / 2) + Xposition;
            Yposition = (this.getHeight() / 2) - Yposition;
        }
        //
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(Xposition, Yposition, 3, paint);
    }

    public boolean DrawGeneList(ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodeArrayList, Canvas canvas) {
        int sizeofList = susceptibleAminoAcidCodeArrayList.size();
        double sigleSlope = 360 / sizeofList;
        double slope = 0;
        for (SusceptibleAminoAcidCode sus : susceptibleAminoAcidCodeArrayList) {
            Log.i("angle", String.valueOf(slope));
            DrawOneGene(sus, slope, canvas);
            slope = (slope + sigleSlope);
        }
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, (this.getWidth() / 2) - 50 - width - width - width - width, paint);
        return true;
    }

}
