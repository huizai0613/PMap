package xxzx.spatialite.TableStruct;


import java.io.Serializable;

/**
 * Created by ch on 2016/4/1.
 */
public class PolePlaceTableDataClass implements Serializable {

    private int RowId = -1;// 数据库的行号

    public String PoleName = "";// 名称

    public int PoleObjectId = -1;//编号

    public String PowerName = "";// 所属电力线名称

    public String DateTime = "";// 时间

    public String UserName = "";//签到用户


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


    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
