package entity;

import java.io.Serializable;

/**
 * Created by shengyuansun on 1/9/17.
 */

public class ResistanceGene implements Serializable {
    String StrainClass;
    String BindingProtein;
    int Postion;
    String orginalCode;

    public String getBindingProtein() {
        return BindingProtein;
    }

    public void setBindingProtein(String bindingProtein) {
        BindingProtein = bindingProtein;
    }

    public int getPostion() {
        return Postion;
    }

    public void setPostion(int postion) {
        Postion = postion;
    }

    public String getOrginalCode() {
        return orginalCode;
    }

    public void setOrginalCode(String orginalCode) {
        this.orginalCode = orginalCode;
    }

    public String getStrainClass() {
        return StrainClass;
    }

    public void setStrainClass(String strainClass) {
        StrainClass = strainClass;
    }

    public ResistanceGene(String strainClass,String BindingProtein, int postion, String orginalCode){
        this.StrainClass =strainClass;
        this.BindingProtein = BindingProtein;
        this.Postion = postion;
        this.orginalCode = orginalCode;
    }
}
