package xxzx.publicClass.MyListView;

import android.graphics.drawable.Drawable;

/**
 * Created by ch on 2016/2/16.
 * 离线地图操作界面上的操作按钮item
 */
public class ListItemOpt {

    private int chnorpole = -1;
    private String optName;
    private int objectid = -1;
    private Drawable icon;
    private int dangerCount=0;

    public int getChnorpole() {
        return chnorpole;
    }

    public void setChnorpole(int chnorpole) {
        this.chnorpole = chnorpole;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getDangerCount() {
        return dangerCount;
    }

    public void setDangerCount(int dangerCount) {
        this.dangerCount = dangerCount;
    }

    public int getObjectid() {
        return objectid;
    }

    public void setObjectid(int objectid) {
        this.objectid = objectid;
    }
}
