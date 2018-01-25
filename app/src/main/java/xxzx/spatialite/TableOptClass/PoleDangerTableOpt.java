package xxzx.spatialite.TableOptClass;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Stmt;
import xxzx.publicClass.DangerVersion;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.ChannelDangerTableColumn;
import xxzx.spatialite.TableStruct.PoleDangerTableColumn;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;

/**
 * Created by ch on 2016/4/2.
 */
public class PoleDangerTableOpt extends BaseTableOpt {

    private String TAG="PoleDangerTableOpt";

    public PoleDangerTableOpt(){
        super();
        this.tableName = MyString.pole_danger_table_name;
    }

    @Override
    public int insert(Object row){

        PoleDangerTableDataClass poleDangerTableDataClass = (PoleDangerTableDataClass)row;

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("insert into '" + MyString.pole_danger_table_name + "' ");
            sql.append("(");

            /////////////////////////////////////////////////////////////////////////
            sql.append(PoleDangerTableColumn.DangerLevel + ",");
            sql.append(PoleDangerTableColumn.DangerName + ",");
            sql.append(PoleDangerTableColumn.DateTime + ",");
            sql.append(PoleDangerTableColumn.DangerType + ",");
            sql.append(PoleDangerTableColumn.PowerName + ",");
            sql.append(PoleDangerTableColumn.PicsJson + ",");
            sql.append(PoleDangerTableColumn.PoleName + ",");
            sql.append(PoleDangerTableColumn.UserName + ",");
            sql.append(PoleDangerTableColumn.DangerMark + ",");
            sql.append(PoleDangerTableColumn.SpotMark + ",");
            sql.append(PoleDangerTableColumn.KeyID + ",");
            sql.append(ChannelDangerTableColumn.ImgNum + ",");
            sql.append(PoleDangerTableColumn.Version +",");
            sql.append(PoleDangerTableColumn.PoleObjectId  +",");
            sql.append(PoleDangerTableColumn.HuiLuType );
            sql.append(") values (");

            sql.append(poleDangerTableDataClass.getDangerLevel() + ",'");
            sql.append(poleDangerTableDataClass.getDangerName() + "','");
            sql.append(poleDangerTableDataClass.getDateTime() + "',");
            sql.append(poleDangerTableDataClass.getDangerType() + ",'");
            sql.append(poleDangerTableDataClass.getPowerName() + "','");
            sql.append(poleDangerTableDataClass.getPicsJson() + "','");
            sql.append(poleDangerTableDataClass.getPoleName() + "','");
            sql.append(poleDangerTableDataClass.getUserName() + "','");
            sql.append(poleDangerTableDataClass.getDangerMark() + "','");
            sql.append(poleDangerTableDataClass.getSpotMark() + "',");
            sql.append(poleDangerTableDataClass.getKeyID() + ",");
            sql.append(poleDangerTableDataClass.getImgNum() + ",");
            sql.append(poleDangerTableDataClass.getVersion() + ",");
            sql.append(poleDangerTableDataClass.getPoleObjectId() + ",");
            sql.append(poleDangerTableDataClass.getHuiLuType() + ")");
            long ll = this.spatialiteDataOpt.InsertExecute(sql.toString());

            rowid = Integer.parseInt(String.valueOf(ll));
            return rowid;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    /**
     * 获得某一杆塔下的所有隐患点
     * @param
     * @return
     */
    public List<Object> getRow(){
        List<Object> list = new ArrayList<Object>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }

    @Override
    public Object getRow(int rowid){

        PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.ImgNum + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.RowId + " = " + rowid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {

                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setImgNum(stmt.column_int(12));
                poleDangerTableDataClass.setVersion(stmt.column_int(13));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(14));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(15));
                //list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return poleDangerTableDataClass;
        }
    }



    /**
     * 获得某一杆塔下的所有隐患点
     * @param poleobjectid
     * @return
     */
    public List<PoleDangerTableDataClass> getRowFormObjectId(int poleobjectid){
        List<PoleDangerTableDataClass> list = new ArrayList<PoleDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.PoleObjectId + " = " + poleobjectid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }


    /**
     * 获得某一线路下的所有隐患点
     * @param powername
     * @return
     */
    public List<PoleDangerTableDataClass> getRowFormPlineName(String powername){
        List<PoleDangerTableDataClass> list = new ArrayList<PoleDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.PowerName + " = '" + powername + "'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }




    /**
     * 获得某一隐患点
     * @param
     * @return
     */
    public List<PoleDangerTableDataClass> getRowFormKeyID(int keyid){
        List<PoleDangerTableDataClass> list = new ArrayList<PoleDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.KeyID + " = " + keyid );

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    /**
     * 获得某一杆塔下的某一类型的隐患点
     * @param
     * @return
     */
    public List<PoleDangerTableDataClass> getRow(int poleobjectid,int dangertype){
        List<PoleDangerTableDataClass> list = new ArrayList<PoleDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.PoleObjectId + " = " + poleobjectid);
        sql.append(" and " + PoleDangerTableColumn.DangerType + " = " + dangertype + "");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    /**
     * 获得杆塔隐患点表中所有的更新过的隐患点信息
     * @return
     */
    public List<PoleDangerTableDataClass> getRowFromUpdate(){
        List<PoleDangerTableDataClass> list = new ArrayList<PoleDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.Version + " != "+DangerVersion.DANGER_VERSION_INIT);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }


    /**
     * 获得杆塔隐患点表中某一杆塔的更新过的隐患点信息
     * @return
     */
    public List<PoleDangerTableDataClass> getRowFromUpdate(String polename){
        List<PoleDangerTableDataClass> list = new ArrayList<PoleDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.Version + " != "+ DangerVersion.DANGER_VERSION_INIT);
        sql.append(" and " + PoleDangerTableColumn.PoleName + " = '"+polename+"'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }

    /**
     * 获得杆塔隐患点表中某一杆塔的更新过的隐患点信息
     * @return
     */
    public List<PoleDangerTableDataClass> getRowFromUpdate(int poleobjectid){
        List<PoleDangerTableDataClass> list = new ArrayList<PoleDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.Version + " != "+ DangerVersion.DANGER_VERSION_INIT);
        sql.append(" and " + PoleDangerTableColumn.PoleObjectId + " = "+poleobjectid+"");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }




    /**
     * 获得杆塔隐患点表中线路的更新过的隐患点信息
     * @return
     */
    public List<PoleDangerTableDataClass> getRowFromUpdate2(String plinename){
        List<PoleDangerTableDataClass> list = new ArrayList<PoleDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleDangerTableColumn.RowId + ",");
        sql.append(PoleDangerTableColumn.DateTime + ",");
        sql.append(PoleDangerTableColumn.PowerName + ",");
        sql.append(PoleDangerTableColumn.DangerType + ",");
        sql.append(PoleDangerTableColumn.PoleName + ",");
        sql.append(PoleDangerTableColumn.DangerName + ",");
        sql.append(PoleDangerTableColumn.DangerLevel + ",");
        sql.append(PoleDangerTableColumn.PicsJson + ",");
        sql.append(PoleDangerTableColumn.DangerMark + ",");
        sql.append(PoleDangerTableColumn.SpotMark + ",");
        sql.append(PoleDangerTableColumn.UserName + ",");
        sql.append(PoleDangerTableColumn.KeyID + ",");
        sql.append(PoleDangerTableColumn.Version +",");
        sql.append(PoleDangerTableColumn.PoleObjectId  +",");
        sql.append(PoleDangerTableColumn.HuiLuType );

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.Version + " != "+DangerVersion.DANGER_VERSION_INIT);
        sql.append(" and " + PoleDangerTableColumn.PowerName + " = '"+plinename+"'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }


    public int delete(List<String> plinenames){

        int result=-1;

        StringBuilder builder=new StringBuilder("");
        for(int i=0;i<plinenames.size();i++){
            if(i<plinenames.size()-1) {
                builder.append(PoleDangerTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
                builder.append(" or ");
            }else{
                builder.append(PoleDangerTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
            }
        }

        StringBuilder sql = new StringBuilder("");
        sql.append("DELETE FROM ");
        sql.append(tableName);
        sql.append(" WHERE " + builder.toString());

        result=this.spatialiteDataOpt.DeleteExecute(sql.toString());

        return result;
    }


    public int delete(int rowid){
        int result=-1;
        StringBuilder sql = new StringBuilder("");
        sql.append("DELETE FROM ");
        sql.append(tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.RowId+"="+rowid);

        result=this.spatialiteDataOpt.DeleteExecute(sql.toString());
        return result;
    }





    /**
     * 通过SQL语句获得杆塔隐患点表
     * @return
     */
    public List<PoleDangerTableDataClass> getRowFromSQL(String sql){
        List<PoleDangerTableDataClass> list = new ArrayList<PoleDangerTableDataClass>();

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql);

        try {
            while (stmt.step()) {
                PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();
                poleDangerTableDataClass.setRowId(stmt.column_int(0));
                poleDangerTableDataClass.setDateTime(stmt.column_string(1));
                poleDangerTableDataClass.setPowerName(stmt.column_string(2));
                poleDangerTableDataClass.setDangerType(stmt.column_int(3));
                poleDangerTableDataClass.setPoleName(stmt.column_string(4));
                poleDangerTableDataClass.setDangerName(stmt.column_string(5));
                poleDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                poleDangerTableDataClass.setPicsJson(stmt.column_string(7));
                poleDangerTableDataClass.setDangerMark(stmt.column_string(8));
                poleDangerTableDataClass.setSpotMark(stmt.column_string(9));
                poleDangerTableDataClass.setUserName(stmt.column_string(10));
                poleDangerTableDataClass.setKeyID(stmt.column_int(11));
                poleDangerTableDataClass.setVersion(stmt.column_int(12));
                poleDangerTableDataClass.setPoleObjectId(stmt.column_int(13));
                poleDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(poleDangerTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    /**
     * 更新隐患点的图片
     * @param poleDangerTableDataClass
     * @return
     */
    public int updateDangerPicsJson(PoleDangerTableDataClass poleDangerTableDataClass){

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");

            sql.append(PoleDangerTableColumn.PicsJson + "='" + poleDangerTableDataClass.getPicsJson()+"'");
            sql.append(" where ROWID=" + poleDangerTableDataClass.getRowId());

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }


    /**
     * 更新隐患点的版本
     * @param rowid
     * @param version
     * @return
     */
    public int updateDangerCompleted(int rowid,int keyid, int version){

        int result = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");

            sql.append(PoleDangerTableColumn.Version + "=" + version+",");
            sql.append(PoleDangerTableColumn.KeyID + "=" + keyid);
            sql.append(" where ROWID=" + rowid);

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            result = Integer.valueOf(String.valueOf(ll));
            return result;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }




    public int update(PoleDangerTableDataClass poleDangerTableDataClass){

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");

            sql.append(PoleDangerTableColumn.Version + "=" + poleDangerTableDataClass.getVersion() + ",");
            sql.append(PoleDangerTableColumn.PoleName + "='" + poleDangerTableDataClass.getPoleName() + "',");
            sql.append(PoleDangerTableColumn.PoleObjectId + "=" + poleDangerTableDataClass.getPoleObjectId() + ",");
            sql.append(PoleDangerTableColumn.HuiLuType + "=" + poleDangerTableDataClass.getHuiLuType() + ",");
            sql.append(PoleDangerTableColumn.SpotMark + "='" + poleDangerTableDataClass.getSpotMark() + "',");
            sql.append(PoleDangerTableColumn.DangerMark + "='" + poleDangerTableDataClass.getDangerMark() + "',");
            sql.append(PoleDangerTableColumn.PowerName + "='" + poleDangerTableDataClass.getPowerName() + "',");
            sql.append(PoleDangerTableColumn.PicsJson + "='" + poleDangerTableDataClass.getPicsJson() + "',");
            sql.append(PoleDangerTableColumn.DangerLevel + "=" + poleDangerTableDataClass.getDangerLevel() + ",");
            sql.append(PoleDangerTableColumn.DangerName + "='" + poleDangerTableDataClass.getDangerName() + "',");
            sql.append(PoleDangerTableColumn.DangerType + "=" + poleDangerTableDataClass.getDangerType() + ",");
            sql.append(PoleDangerTableColumn.DateTime + "='" + poleDangerTableDataClass.getDateTime() + "',");
            sql.append(PoleDangerTableColumn.UserName + "='" + poleDangerTableDataClass.getUserName() + "'");

            sql.append(" where ROWID=" + poleDangerTableDataClass.getRowId());

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }







}
