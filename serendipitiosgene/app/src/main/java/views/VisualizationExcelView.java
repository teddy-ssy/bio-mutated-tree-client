package views;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.excelpanel.excelpanel.BaseExcelPanelAdapter;
import com.example.excelpanel.utils.Utils;
import com.example.shengyuansun.serendipitiosgene.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import constants.Constatns;
import databbaseutil.DatabaseHelper;
import entity.ResistanceGene;
import entity.SusceptibleAminoAcidCode;

/**
 * Created by shengyuansun on 4/9/17.
 */

public class VisualizationExcelView extends BaseExcelPanelAdapter<ResistanceGene, String, HashMap<String,String>> {

    private Context context;
    private View.OnClickListener blockListener;
    public DatabaseHelper db;
    public int resitancesize;

    public VisualizationExcelView(Context context, View.OnClickListener blockListener) {
        super(context);
        this.context = context;
        this.blockListener = blockListener;
        db = new DatabaseHelper(context);
        resitancesize = db.getAllResistanceGene(Constatns.Pneumococcal).size();

    }

    //cell container

    static class CellHolder extends RecyclerView.ViewHolder{
        public final TextView textview_mutation;
        public final LinearLayout cellContainer;

        public CellHolder(View view){
            super(view);
            textview_mutation = (TextView) view.findViewById(R.id.textview_container_mutation);
            cellContainer =(LinearLayout) view.findViewById(R.id.linearlayout_cell_container);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_container,parent,false);
        CellHolder cellHolder = new CellHolder(layout);
        return cellHolder;

    }

    @Override
    public void onBindCellViewHolder(RecyclerView.ViewHolder holder, int horizontalPosition , int verticalPosition) {
        HashMap<String,String> mutation = getMajorItem(horizontalPosition,verticalPosition);
        CellHolder viewHolder =(CellHolder) holder;
        viewHolder.cellContainer.setTag(mutation);
        viewHolder.cellContainer.setOnClickListener(blockListener);
        viewHolder.textview_mutation.setText(mutation.keySet().iterator().next());
        //set color
        List<ResistanceGene> resistanceGeneList = new ArrayList<ResistanceGene>();
        //resistanceGeneList = db.getAllResistanceGene(Constatns.Pneumococcal);
        List<SusceptibleAminoAcidCode> susceptibleAminoAcidCodeList=Constatns.susceptibleAminoAcidCodeArrayList;
        if(mutation.values().iterator().next().toString().equals("mutation")){
            viewHolder.cellContainer.setBackgroundColor(Color.argb(100,255,193,193));
        }
    }

    //top header

    static class TopHolder extends RecyclerView.ViewHolder{
        public final TextView textViewRowOne;
        public final TextView textViewRowTwo;
        public final TextView textViewRowThree;

        public TopHolder(View view){
            super(view);
            textViewRowOne = (TextView) view.findViewById(R.id.textview_top_one);
            textViewRowTwo =(TextView) view.findViewById(R.id.textview_top_two);
            textViewRowThree = (TextView) view.findViewById(R.id.textview_top_three);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateTopViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_top_header,parent,false);
        TopHolder topHolder = new TopHolder(layout);
        return  topHolder;
    }

    @Override
    public void onBindTopViewHolder(RecyclerView.ViewHolder holder, int position) {
        ResistanceGene resistanceGene = getTopItem(position);
        if (null == holder || !(holder instanceof TopHolder) || resistanceGene == null) {
            return;
        }
        TopHolder viewHolder = (TopHolder) holder;
        if(position == 0){
            viewHolder.textViewRowOne.setText("");
            viewHolder.textViewRowTwo.setText("year");
            viewHolder.textViewRowThree.setText("");
        }else if(position ==1){
            viewHolder.textViewRowOne.setText("");
            viewHolder.textViewRowTwo.setText("ST");
            viewHolder.textViewRowThree.setText("");
        }
        else if(position ==2){
            viewHolder.textViewRowOne.setText("");
            viewHolder.textViewRowTwo.setText("MIC");
            viewHolder.textViewRowThree.setText("");
        }else if( position == resitancesize+3){
            viewHolder.textViewRowOne.setText("");
            viewHolder.textViewRowTwo.setText("Phenotype");
            viewHolder.textViewRowThree.setText("");
        }else if(position ==resitancesize+4)
        {
            viewHolder.textViewRowOne.setText("number of");
            viewHolder.textViewRowTwo.setText(" mutated ");
            viewHolder.textViewRowThree.setText("amino acids");
        }


        for(int i=0;i<resitancesize;i++){
            if(position == i+3){
                viewHolder.textViewRowOne.setText(db.getAllResistanceGene(Constatns.Pneumococcal).get(i).getBindingProtein());
                viewHolder.textViewRowTwo.setText(String.valueOf(db.getAllResistanceGene(Constatns.Pneumococcal).get(i).getPostion()));
                viewHolder.textViewRowThree.setText(db.getAllResistanceGene(Constatns.Pneumococcal).get(i).getOrginalCode());
            }
        }

    }

    //left-header
    public static int[] height = {23,45,12,32,9,1,5};
    //public static int[] height = {40,60,30,50,20,10,15};

    static class LeftHolder extends RecyclerView.ViewHolder{
        public final TextView strainName;
        public final View root;
        public LeftHolder(View view){
            super(view);
            root = view.findViewById(R.id.root);
            strainName = (TextView) view.findViewById(R.id.textview_left_header);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateLeftViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_left_header,parent,false);
        LeftHolder ledtHolder = new LeftHolder(layout);
        return ledtHolder;
    }

    @Override
    public void onBindLeftViewHolder(RecyclerView.ViewHolder holder, int position) {
        String SP = getLeftItem(position);
        if (null == holder || !(holder instanceof LeftHolder) || SP == null) {
            return;
        }
        LeftHolder viewHolder = (LeftHolder) holder;
        viewHolder.strainName.setText(SP);
        //test height
        ViewGroup.LayoutParams lp = viewHolder.root.getLayoutParams();
        lp.height = Utils.dp2px(56, context) + Utils.dp2px(height[position % height.length], context);
        viewHolder.root.setLayoutParams(lp);
    }

    //left-top
    @Override
    public View onCreateTopLeftView() {
        return LayoutInflater.from(context).inflate(R.layout.cell_left_top, null);
    }
}
