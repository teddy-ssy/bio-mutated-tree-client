package databbaseutil;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.ResistanceGene;

/**
 * Created by shengyuansun on 1/9/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper{


    public static final String DATABASE_NAME="SerendipitiousGene.db";

    public static final String RESISTANCEGENE_TABLE="resistancegenetable";
    public static String RESISTANCEGENE_CREATE="create table resistancegenetable ( strainclass varchar(20) , bindingprotein varchar(10) , position integer , orginalcode varchar(1) )";

    public static final String MUTATIONGENE_TABLE = "mutationgenetable";
    public static String MUTATIONGENE_CREATE ="create table mutationgenetable ( strainclass varchar(20) , sp varchar(10) , position integer , mutationcode varchar(1)) ";

    public static final String FILE_Table = "filetable";
    public static String FILE_CREATE= "create table filetable ( path varchar(70) , name varchar(70))";

    public static final String SP_TABLE = "sptable";
    public static String SP_CREATE = "create table sptable ( strainclass varchar(20), sp varchar(10) )";

    public DatabaseHelper(Context context){

        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(RESISTANCEGENE_CREATE);
        sqLiteDatabase.execSQL(MUTATIONGENE_CREATE);
        sqLiteDatabase.execSQL(FILE_CREATE);
        sqLiteDatabase.execSQL(SP_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+RESISTANCEGENE_TABLE);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+MUTATIONGENE_TABLE);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+FILE_Table);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+SP_TABLE);
        onCreate(sqLiteDatabase);
    }

    //read

    public int getNumberOfResistanceGene(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select count(*) from "+ RESISTANCEGENE_TABLE ;
        Cursor cursor= db.rawQuery(sql,null);
        cursor.moveToFirst();
        Integer count = cursor.getInt(0);
        cursor.close();;
        return count;
    }

    public int getNumberOfSP(String sp){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select count(*) from "+ SP_TABLE+ " where sp = ?" ;
        Cursor cursor= db.rawQuery(sql,new String[]{sp});
        cursor.moveToFirst();
        Integer count = cursor.getInt(0);
        cursor.close();;
        return count;
    }

    public int getNumberOffile(String path){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select count(*) from "+ FILE_Table+ " where path = ?" ;
        Cursor cursor= db.rawQuery(sql,new String[]{path});
        cursor.moveToFirst();
        Integer count = cursor.getInt(0);
        cursor.close();;
        return count;
    }

    public int getNumberOfResitanceGeneOnPosition(int position){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select count(*) from "+ RESISTANCEGENE_TABLE+ " where position = ?" ;
        Cursor cursor= db.rawQuery(sql,new String[]{String.valueOf(position)});
        cursor.moveToFirst();
        Integer count = cursor.getInt(0);
        cursor.close();;
        return count;
    }

    public ArrayList<ResistanceGene> getAllResistanceGene(String strainclass){
        SQLiteDatabase db= getReadableDatabase();
        String sql = "select strainclass , bindingprotein , position , orginalcode from "+RESISTANCEGENE_TABLE+" where strainclass = ?";
        Cursor cursor =db.rawQuery(sql, new String[] {strainclass });
        cursor.moveToFirst();
        ArrayList<ResistanceGene> resistanceGeneArrayList = new ArrayList<ResistanceGene>();
        while(cursor.isAfterLast()==false){
            String strainClass = cursor.getString(cursor.getColumnIndex("strainclass"));
            String bindingprotein = cursor.getString(cursor.getColumnIndex("bindingprotein"));
            int postion = cursor.getInt(2);
            String orginalcode = cursor.getString(cursor.getColumnIndex("orginalcode"));
            ResistanceGene resistanceGene = new ResistanceGene(strainClass,bindingprotein,postion,orginalcode);
            resistanceGeneArrayList.add(resistanceGene);
            cursor.moveToNext();
        }
        return resistanceGeneArrayList;
    }

    public ArrayList<HashMap<String,String>> getAllFilePath(){
        SQLiteDatabase db = getReadableDatabase();
        String sql= "select path,name from "+ FILE_Table;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        ArrayList<HashMap<String,String>> filepathlist = new ArrayList<HashMap<String, String>>();
        while(cursor.isAfterLast()==false){
            String path= cursor.getString(cursor.getColumnIndex("path"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            HashMap<String,String> filepath = new HashMap<String,String>();
            filepath.put(path,name);
            filepathlist.add(filepath);
            cursor.moveToNext();
        }
        return filepathlist;
    }

    public String getfilePathByname(String filename){
        SQLiteDatabase db = getReadableDatabase();
        String sql= "select path from "+ FILE_Table + " where name = ?" ;
        Cursor cursor = db.rawQuery(sql, new String[]{filename});
        cursor.moveToFirst();
        String filepath;
        try{
            filepath = cursor.getString(cursor.getColumnIndex("path"));
        }catch (Exception e){
            filepath= null;
            e.printStackTrace();
        }
        return filepath;
    }

    public ArrayList<HashMap<Integer,Character>> getMutation(String strainclass, String sp){
        HashMap<Integer,Character> result = new HashMap<Integer, Character>();
        ArrayList<HashMap<Integer,Character>> results = new ArrayList<HashMap<Integer,Character>>();
        SQLiteDatabase db= getReadableDatabase();
        String sql = "select position , mutationcode from "+MUTATIONGENE_TABLE+" where strainclass = ? and sp = ?";
        Cursor cursor =db.rawQuery(sql, new String[] {strainclass , sp});
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false){
            int postion = cursor.getInt(0);
            String mutationcode = cursor.getString(cursor.getColumnIndex("mutationcode"));
            Character mutationcodeC = mutationcode.charAt(0);
            result = new HashMap<Integer, Character>();
            result.put(postion,mutationcodeC);
            results.add(result);
            cursor.moveToNext();
        }
        return results;
    }


    //wrire

    public void DeleteSP(String sp){
        SQLiteDatabase db = getWritableDatabase();
        String[] arg = new String[]{sp};
        db.delete(SP_TABLE," sp = ? " , arg);
    }

    public void DeleteMutation(String sp){
        SQLiteDatabase db = getWritableDatabase();
        String[] arg = new String[]{sp};
        db.delete(MUTATIONGENE_TABLE," sp = ? " , arg);
    }

    public void DeleteResistanceGene(int position){
        SQLiteDatabase db = getWritableDatabase();
        String[] arg = new String[]{String.valueOf(position)};
        db.delete(RESISTANCEGENE_TABLE," position = ? " , arg);
    }

    public void AddOneFilePath(String path,String name){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(" path ",path);
        cv.put(" name ",name);
        db.insert(FILE_Table,null,cv);
    }

    public void AddOneSP(String strainclass,String sp){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(" strainclass ",strainclass);
        cv.put(" sp ",sp);
        db.insert(SP_TABLE,null,cv);
    }

    public void AddOneMutationGene(String strainclass ,String sp,int position ,String mutationcode){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(" strainclass ",strainclass);
        cv.put(" sp ",sp);
        cv.put(" position ",position);
        cv.put(" mutationcode ", mutationcode);
        db.insert(MUTATIONGENE_TABLE,null,cv);
    }

    public void DelectFilePath(String path){
        SQLiteDatabase db = getWritableDatabase();
        String[] arg = new String[]{path};
        db.delete(FILE_Table," path = ? " , arg);
    }

    public void insertResistanceGene(String strainclass,String bindingprotein,int position,String orginalcode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(" strainclass ",strainclass);
        cv.put(" bindingprotein ",bindingprotein);
        cv.put(" position ",position);
        cv.put(" orginalcode ",orginalcode);
        db.insert(RESISTANCEGENE_TABLE,null,cv);
    }

    //init

    public void initResistanceGene(){
        SQLiteDatabase db = getReadableDatabase();
        insertResistanceGene("Pneumococcal", "pbp1a" , 370 , "S");
        insertResistanceGene("Pneumococcal", "pbp1a" , 371 , "T");
        insertResistanceGene("Pneumococcal", "pbp1a" , 372 , "M");
        insertResistanceGene("Pneumococcal", "pbp1a" , 373 , "K");
        insertResistanceGene("Pneumococcal", "pbp1a" , 428 , "S");
        insertResistanceGene("Pneumococcal", "pbp1a" , 429 , "R");
        insertResistanceGene("Pneumococcal", "pbp1a" , 430 , "N");
        insertResistanceGene("Pneumococcal", "pbp1a" , 431 , "V");
        insertResistanceGene("Pneumococcal", "pbp1a" , 432 , "P");
        insertResistanceGene("Pneumococcal", "pbp1a" , 574 , "T");
        insertResistanceGene("Pneumococcal", "pbp1a" , 575 , "S");
        insertResistanceGene("Pneumococcal", "pbp2a" , 1169 , "S");
        insertResistanceGene("Pneumococcal", "pbp2a" , 1294 , "V");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1825 , "S");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1826 , "V");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1827 , "V");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1828 , "K");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1865 , "T");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1866 , "Q");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1882 , "S");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1883 , "S");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1884 , "N");
        insertResistanceGene("Pneumococcal", "pbp2b" , 1885 , "T");
        insertResistanceGene("Pneumococcal", "pbp2b" , 2054 , "K");
        insertResistanceGene("Pneumococcal", "pbp2b" , 2055 , "T");
        insertResistanceGene("Pneumococcal", "pbp2b" , 2056 , "G");
        insertResistanceGene("Pneumococcal", "pbp2b" , 2057 , "T");
        insertResistanceGene("Pneumococcal", "pbp2b" , 2058 , "A");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2456 , "S");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2457 , "T");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2458 , "M");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2459 , "K");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2513 , "H");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2514 , "S");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2515 , "S");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2516 , "N");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2519 , "M");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2520 , "T");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2665 , "L");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2666 , "K");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2667 , "S");
        insertResistanceGene("Pneumococcal", "pbp2x" , 2668 , "G");
    }

}

