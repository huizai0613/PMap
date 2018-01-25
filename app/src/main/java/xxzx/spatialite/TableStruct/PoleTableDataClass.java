package xxzx.spatialite.TableStruct;


import java.io.Serializable;

/**
 * Created by ch on 2016/4/1.
 */
public class PoleTableDataClass implements Serializable {

    private int RowId = -1;// 数据库的行号

    public String PoleName = "";// 名称

    public int PoleObjectId = -1;//编号

    public String PowerName = "";// 所属电力线名称

    public int  PoleNumber = -1; //杆塔编号

    public String DateTime = "";// 时间

    public int IsSelect = 0;// 标记为我的任务

    public int dangerCount = 0;//隐患点数量

    public String Geometry = "";// 几何字段


    public int getRowId() {
        return RowId;
    }

    public void setRowId(int rowId) {
        RowId = rowId;
    }


    public String getPowerName() {
        return PowerName;
    }

    public void setPowerName(String powerName) {
        PowerName = powerName;
    }

    public String getPoleName() {
        return PoleName;
    }

    public void setPoleName(String poleName) {
        PoleName = poleName;
    }

    public int getPoleObjectId() {
        return PoleObjectId;
    }

    public void setPoleObjectId(int poleObjectId) {
        PoleObjectId = poleObjectId;
    }

    public int getPoleNumber() {
        return PoleNumber;
    }

    public void setPoleNumber(int poleNumber) {
        PoleNumber = poleNumber;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public int getIsSelect() {
        return IsSelect;
    }

    public void setIsSelect(int isSelect) {
        IsSelect = isSelect;
    }

    public int getDangerCount() {
        return dangerCount;
    }

    public void setDangerCount(int dangerCount) {
        this.dangerCount = dangerCount;
    }

    public String getGeometry() {
        return Geometry;
    }

    public void setGeometry(String geometry) {
        Geometry = geometry;
    }
}
