package xxzx.spatialite.TableStruct;

/**
 * Created by ch on 2016/4/1.
 */
public class PowerlineTableDataClass {

    private int RowId = -1;// 数据库的行号

    private String PowerName = "";// 电力线名称

    private String UserName = "";// 人员

    private String DateTime = "";// 时间

    private int IsSelect = 0;// 标记为我的任务

    private int IsInMap = 0;//标记是否导入地图

    private String Geometry = "";// 几何字段


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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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

    public int getIsInMap() {
        return IsInMap;
    }

    public void setIsInMap(int isInMap) {
        IsInMap = isInMap;
    }

    public String getGeometry() {
        return Geometry;
    }

    public void setGeometry(String geometry) {
        Geometry = geometry;
    }
}
