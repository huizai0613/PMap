package xxzx.upload;

import java.util.List;

/**
 * Created by ch on 2016/2/16.
 * 离线地图操作界面上的操作按钮item
 */
public class ListItemUpdateDangerClass {

    private int rowID=-1;
    private String title="";
    private int objectid = -1;
    private int dangerType = 0;
    private String date="";
    private String userName="";
    private List<String> picsName;
    private int dangetLevel = 0;
    private int chnOrpole;

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getDangerType() {
        return dangerType;
    }

    public void setDangerType(int dangerType) {
        this.dangerType = dangerType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getPicsName() {
        return picsName;
    }

    public void setPicsName(List<String> picsName) {
        this.picsName = picsName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDangetLevel() {
        return dangetLevel;
    }

    public void setDangetLevel(int dangetLevel) {
        this.dangetLevel = dangetLevel;
    }

    public int getChnOrpole() {
        return chnOrpole;
    }

    public void setChnOrpole(int chnOrpole) {
        this.chnOrpole = chnOrpole;
    }

    public int getObjectid() {
        return objectid;
    }

    public void setObjectid(int objectid) {
        this.objectid = objectid;
    }
}
