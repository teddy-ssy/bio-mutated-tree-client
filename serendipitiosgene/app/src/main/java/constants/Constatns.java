package constants;

import java.util.ArrayList;
import java.util.HashMap;

import entity.ResistanceGene;
import entity.SusceptibleAminoAcidCode;

/**
 * Created by shengyuansun on 1/9/17.
 */

public class Constatns {

    public static ArrayList<HashMap<String,String>> FILEPATHLIST = new ArrayList<HashMap<String, String>>();

    public static String Pneumococcal = "Pneumococcal";

    public static ArrayList<SusceptibleAminoAcidCode> susceptibleAminoAcidCodeArrayList = new ArrayList<SusceptibleAminoAcidCode>();

    public static ArrayList<ResistanceGene> RESISTANCEGENELIST = new ArrayList<ResistanceGene>();

    public static int StringLength ;
    public static int Stringnumber;
    public static String filedirct = "/mnt/sdcard/COMP5216/";

    public static ArrayList<String> alllabel;
}
