package xxzx.spatialite.TableStruct;

import java.io.Serializable;

/**
 * Created by ch on 2016/4/1.
 */
public class DangerSgTableDataClass implements Serializable {

    private int RowId = -1;// 数据库的行号
    private int DangerRowId = -1;//关联隐患数据库的RowID
    private String PlineName = "";///线路名称
    private String ChnName = "";///通道名称
    private int ChnObjectId = -1;///通道编号
    public String Sgdepartment ="";/// 施工单位
    public String Sgcontact ="";/// 施工单位联系人
    public String Sgphone ="";/// 施工单位联系人联系方式
    public String Ywcontact ="";/// 运维单位联系人
    public String Ywphone ="";/// 运维单位联系人联系方式
    public String Keeper ="";/// 看守人员
    public String Keeperphone ="";/// 看守人员联系方式
    public String Guank1 ="";/// 安全告知书下达
    public String Guank2 ="";/// 安全协议签订
    public String Guank3 ="";/// 行政许可办理
    public String Guank4 ="";/// 警示、限高措施
    public String Guank5 ="";/// 视频安装
    public String Location ="";///位置描述



    public int getRowId() {
        return RowId;
    }

    public void setRowId(int rowId) {
        RowId = rowId;
    }

    public int getDangerRowId() {
        return DangerRowId;
    }

    public void setDangerRowId(int dangerRowId) {
        DangerRowId = dangerRowId;
    }

    public String getSgdepartment() {
        return Sgdepartment;
    }

    public void setSgdepartment(String sgdepartment) {
        Sgdepartment = sgdepartment;
    }

    public String getSgcontact() {
        return Sgcontact;
    }

    public void setSgcontact(String sgcontact) {
        Sgcontact = sgcontact;
    }

    public String getSgphone() {
        return Sgphone;
    }

    public void setSgphone(String sgphone) {
        Sgphone = sgphone;
    }

    public String getYwcontact() {
        return Ywcontact;
    }

    public void setYwcontact(String ywcontact) {
        Ywcontact = ywcontact;
    }

    public String getYwphone() {
        return Ywphone;
    }

    public void setYwphone(String ywphone) {
        Ywphone = ywphone;
    }

    public String getKeeper() {
        return Keeper;
    }

    public void setKeeper(String keeper) {
        Keeper = keeper;
    }

    public String getKeeperphone() {
        return Keeperphone;
    }

    public void setKeeperphone(String keeperphone) {
        Keeperphone = keeperphone;
    }

    public String getGuank1() {
        return Guank1;
    }

    public void setGuank1(String guank1) {
        Guank1 = guank1;
    }

    public String getGuank2() {
        return Guank2;
    }

    public void setGuank2(String guank2) {
        Guank2 = guank2;
    }

    public String getGuank3() {
        return Guank3;
    }

    public void setGuank3(String guank3) {
        Guank3 = guank3;
    }

    public String getGuank4() {
        return Guank4;
    }

    public void setGuank4(String guank4) {
        Guank4 = guank4;
    }

    public String getGuank5() {
        return Guank5;
    }

    public void setGuank5(String guank5) {
        Guank5 = guank5;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPlineName() {
        return PlineName;
    }

    public void setPlineName(String plineName) {
        PlineName = plineName;
    }

    public String getChnName() {
        return ChnName;
    }

    public void setChnName(String chnName) {
        ChnName = chnName;
    }

    public int getChnObjectId() {
        return ChnObjectId;
    }

    public void setChnObjectId(int chnObjectId) {
        ChnObjectId = chnObjectId;
    }
}
