package xxzx.spatialite.TableStruct;

import xxzx.publicClass.DangerVersion;

/**
 * Created by ch on 2016/4/26.
 */
public abstract class BaseDangerTableDataClass {

    public int RowId = -1;// 数据库的行号

    public String DangerName = "";// 名称

    public String PowerName = "";// 所属电力线名称

    public int DangerType = -1;// 类型

    public int  DangerLevel = -1; //危险级别

    public String  PicsJson = ""; //图片json

    public String DateTime = "";// 时间

    public String DangerMark="";//备注

    //如果keyID=-1，说明是本地采集的数据，Version只能等于-1
    //如果keyID!=-1，说明是从服务器端下载的数据，Version可以等于-1,0,1,其中-1表示没有修改，0表示未消缺，1表示消缺
    public int Version = DangerVersion.DANGER_VERSION_INIT;//更新类别

    public int KeyID =-1; //默认标示

    public String UserName="";//巡查人员

    public int ImgNum = 0; //网络隐患点的图片数量

    private String SpotMark="";//隐患点现场巡视情况

    private int HuiLuType = -1;//回路信息，-1表示没有填写，0为左，1为中，2为右 ,暂定

    public int getRowId() {
        return RowId;
    }

    public void setRowId(int rowId) {
        RowId = rowId;
    }

    public String getDangerName() {
        return DangerName;
    }

    public void setDangerName(String dangerName) {
        DangerName = dangerName;
    }

    public String getPowerName() {
        return PowerName;
    }

    public void setPowerName(String powerName) {
        PowerName = powerName;
    }

    public int getDangerType() {
        return DangerType;
    }

    public void setDangerType(int dangerType) {
        DangerType = dangerType;
    }

    public String getDangerMark() {
        return DangerMark;
    }

    public void setDangerMark(String dangerMark) {
        DangerMark = dangerMark;
    }

    public int getDangerLevel() {
        return DangerLevel;
    }

    public void setDangerLevel(int dangerLevel) {
        DangerLevel = dangerLevel;
    }

    public String getPicsJson() {
        return PicsJson;
    }

    public void setPicsJson(String picsJson) {
        PicsJson = picsJson;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getKeyID() {
        return KeyID;
    }

    public void setKeyID(int keyID) {
        KeyID = keyID;
    }

    public int getImgNum() {
        return ImgNum;
    }

    public void setImgNum(int imgNum) {
        ImgNum = imgNum;
    }

    public String getSpotMark() {
        return SpotMark;
    }

    public void setSpotMark(String spotMark) {
        SpotMark = spotMark;
    }

    public int getHuiLuType() {
        return HuiLuType;
    }

    public void setHuiLuType(int huiLuType) {
        HuiLuType = huiLuType;
    }
}
