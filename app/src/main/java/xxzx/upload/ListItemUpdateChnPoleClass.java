package xxzx.upload;

import android.graphics.drawable.Drawable;

/**
 * Created by ch on 2016/2/16.
 * 离线地图操作界面上的操作按钮item
 */
public class ListItemUpdateChnPoleClass {

    private String title="";
    private int objectid = -1;
    private Drawable icon;
    private int dangerNum=0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getDangerNum() {
        return dangerNum;
    }

    public void setDangerNum(int dangerNum) {
        this.dangerNum = dangerNum;
    }

    public int getObjectid() {
        return objectid;
    }

    public void setObjectid(int objectid) {
        this.objectid = objectid;
    }
}
