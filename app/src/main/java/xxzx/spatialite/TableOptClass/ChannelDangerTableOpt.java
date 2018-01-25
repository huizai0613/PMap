package xxzx.spatialite.TableOptClass;

import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Stmt;
import xxzx.publicClass.DangerVersion;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.ChannelDangerTableColumn;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableColumn;
import xxzx.spatialite.TableStruct.PoleTableColumn;
import xxzx.spatialite.TableStruct.PoleTableDataClass;

/**
 * Created by ch on 2016/4/2.
 */
public class ChannelDangerTableOpt extends BaseTableOpt {

    private String TAG="ChannelDangerTableOpt";

    public ChannelDangerTableOpt(){
        super();
        this.tableName= MyString.channel_danger_table_name;
    }

    @Override
    public int insert(Object row){

        ChannelDangerTableDataClass chnDangerTableDataClass = (ChannelDangerTableDataClass)row;

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("insert into '" + MyString.channel_danger_table_name + "' ");
            sql.append("(");

            /////////////////////////////////////////////////////////////////////////
            sql.append(ChannelDangerTableColumn.DangerLevel + ",");
            sql.append(ChannelDangerTableColumn.DangerName + ",");
            sql.append(ChannelDangerTableColumn.DateTime + ",");
            sql.append(ChannelDangerTableColumn.DangerType + ",");
            sql.append(ChannelDangerTableColumn.PowerName + ",");
            sql.append(ChannelDangerTableColumn.PicsJson + ",");
            sql.append(ChannelDangerTableColumn.ChannelName + ",");
            sql.append(ChannelDangerTableColumn.ChannelObjectId + ",");
            sql.append(ChannelDangerTableColumn.UserName + ",");
            sql.append(ChannelDangerTableColumn.DangerMark + ",");
            sql.append(ChannelDangerTableColumn.SpotMark + ",");
            sql.append(ChannelDangerTableColumn.KeyID + ",");
            sql.append(ChannelDangerTableColumn.ImgNum + ",");
            sql.append(ChannelDangerTableColumn.Version + "," );
            sql.append(ChannelDangerTableColumn.HuiLuType + ",");

            sql.append(ChannelDangerTableColumn.Geometry);

            sql.append(") values (");

            sql.append(chnDangerTableDataClass.getDangerLevel() + ",'");
            sql.append(chnDangerTableDataClass.getDangerName() + "','");
            sql.append(chnDangerTableDataClass.getDateTime() + "',");
            sql.append(chnDangerTableDataClass.getDangerType() + ",'");
            sql.append(chnDangerTableDataClass.getPowerName() + "','");
            sql.append(chnDangerTableDataClass.getPicsJson() + "','");
            sql.append(chnDangerTableDataClass.getChannelName() + "',");
            sql.append(chnDangerTableDataClass.getChannelObjectId() + ",'");
            sql.append(chnDangerTableDataClass.getUserName() + "','");
            sql.append(chnDangerTableDataClass.getDangerMark() + "','");
            sql.append(chnDangerTableDataClass.getSpotMark() + "',");
            sql.append(chnDangerTableDataClass.getKeyID() + ",");
            sql.append(chnDangerTableDataClass.getImgNum() + ",");
            sql.append(chnDangerTableDataClass.getVersion() + ",");
            sql.append(chnDangerTableDataClass.getHuiLuType() + ",");
            sql.append("GeomFromText('");
            sql.append(chnDangerTableDataClass.getGeometry() + "'," + MyString.gps_srid);
            sql.append("))");

            long ll = this.spatialiteDataOpt.InsertExecute(sql.toString());

            rowid = Integer.parseInt(String.valueOf(ll));
            return rowid;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }



    @Override
    public Object getRow(int rowid){

        ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version+",");
        sql.append(ChannelDangerTableColumn.ImgNum+",");
        sql.append(ChannelDangerTableColumn.HuiLuType + ",");

        sql.append("AsText(" + ChannelDangerTableColumn.Geometry + ")");

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.RowId + " = " + rowid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {

                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(5));
                channelDangerTableDataClass.setDangerName(stmt.column_string(6));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(7));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(8));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(9));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(10));
                channelDangerTableDataClass.setUserName(stmt.column_string(11));
                channelDangerTableDataClass.setKeyID(stmt.column_int(12));
                channelDangerTableDataClass.setVersion(stmt.column_int(13));
                channelDangerTableDataClass.setImgNum(stmt.column_int(14));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(15));
                channelDangerTableDataClass.setGeometry(stmt.column_string(16));
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return channelDangerTableDataClass;
        }
    }


    /**
     * 获得所有隐患点
     * @param
     * @return
     */
    @Override
    public List<Object> getRow(){
        List<Object> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version+",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId + ",");
        sql.append(ChannelDangerTableColumn.HuiLuType);

        sql.append(" FROM " + tableName);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(channelDangerTableDataClass);
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
     * 获得某一通道下的所有隐患点
     * @param chnobjectid
     * @return
     */
    public List<ChannelDangerTableDataClass> getRowFromObjectId(int chnobjectid){
        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId+ ",");
        sql.append(ChannelDangerTableColumn.HuiLuType);
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelDangerTableColumn.ChannelObjectId + " = " + chnobjectid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(channelDangerTableDataClass);
            }

            stmt.close();
        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    public List<ChannelDangerTableDataClass> getRowFormKeyID(int keyid){
        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId+ ",");
        sql.append(ChannelDangerTableColumn.HuiLuType);

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelDangerTableColumn.KeyID + " = " + keyid );

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(channelDangerTableDataClass);
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
    public List<ChannelDangerTableDataClass> getRowFormPlineName(String powername){
        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId+ ",");
        sql.append(ChannelDangerTableColumn.HuiLuType);

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelDangerTableColumn.PowerName + " = '" + powername + "'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(channelDangerTableDataClass);
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
     * 获得某一通道下的某一类型的隐患点
     * @param
     * @return
     */
    public List<ChannelDangerTableDataClass> getRow(int chnobjectid,int dangertype){
        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId+ ",");
        sql.append(ChannelDangerTableColumn.HuiLuType);


        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelDangerTableColumn.ChannelObjectId + " = " + chnobjectid );
        sql.append(" and " + ChannelDangerTableColumn.DangerType + " = " + dangertype + "");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                list.add(channelDangerTableDataClass);
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
     * 获得隐患点表中线路的更新过的隐患点信息
     * @return
     */
    public List<ChannelDangerTableDataClass> getRowFromUpdate2(String plinename){
        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.Geometry + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId+ ",");
        sql.append(ChannelDangerTableColumn.HuiLuType);


        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleDangerTableColumn.Version + " != "+DangerVersion.DANGER_VERSION_INIT);
        sql.append(" and " + PoleDangerTableColumn.PowerName + " = '"+plinename+"'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelDangerTableDataClass chnDangerTableDataClass = new ChannelDangerTableDataClass();
                chnDangerTableDataClass.setRowId(stmt.column_int(0));
                chnDangerTableDataClass.setDateTime(stmt.column_string(1));
                chnDangerTableDataClass.setPowerName(stmt.column_string(2));
                chnDangerTableDataClass.setDangerType(stmt.column_int(3));
                chnDangerTableDataClass.setChannelName(stmt.column_string(4));
                chnDangerTableDataClass.setDangerName(stmt.column_string(5));
                chnDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                chnDangerTableDataClass.setPicsJson(stmt.column_string(7));
                chnDangerTableDataClass.setDangerMark(stmt.column_string(8));
                chnDangerTableDataClass.setSpotMark(stmt.column_string(9));
                chnDangerTableDataClass.setUserName(stmt.column_string(10));
                chnDangerTableDataClass.setKeyID(stmt.column_int(11));
                chnDangerTableDataClass.setGeometry(stmt.column_string(12));
                chnDangerTableDataClass.setVersion(stmt.column_int(13));
                chnDangerTableDataClass.setChannelObjectId(stmt.column_int(14));
                chnDangerTableDataClass.setHuiLuType(stmt.column_int(15));
                list.add(chnDangerTableDataClass);
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
                builder.append(ChannelDangerTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
                builder.append(" or ");
            }else{
                builder.append(ChannelDangerTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
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
        sql.append(" WHERE " + ChannelDangerTableColumn.RowId+"="+rowid);

        result=this.spatialiteDataOpt.DeleteExecute(sql.toString());

        return result;
    }




    public int update(ChannelDangerTableDataClass channelDangerTableDataClass){

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");

            sql.append(ChannelDangerTableColumn.Version + "=" + channelDangerTableDataClass.getVersion() + ",");
            sql.append(ChannelDangerTableColumn.ChannelName + "='" + channelDangerTableDataClass.getChannelName() + "',");
            sql.append(ChannelDangerTableColumn.ChannelObjectId + "=" + channelDangerTableDataClass.getChannelObjectId() + ",");
            sql.append(ChannelDangerTableColumn.HuiLuType + "=" + channelDangerTableDataClass.getHuiLuType() + ",");
            sql.append(ChannelDangerTableColumn.DangerMark + "='" + channelDangerTableDataClass.getDangerMark() + "',");
            sql.append(ChannelDangerTableColumn.SpotMark + "='" + channelDangerTableDataClass.getSpotMark() + "',");
            sql.append(ChannelDangerTableColumn.PowerName + "='" + channelDangerTableDataClass.getPowerName() + "',");
            sql.append(ChannelDangerTableColumn.PicsJson + "='" + channelDangerTableDataClass.getPicsJson() + "',");
            sql.append(ChannelDangerTableColumn.DangerLevel + "=" + channelDangerTableDataClass.getDangerLevel() + ",");
            sql.append(ChannelDangerTableColumn.DangerName + "='" + channelDangerTableDataClass.getDangerName() + "',");
            sql.append(ChannelDangerTableColumn.DangerType + "=" + channelDangerTableDataClass.getDangerType() + ",");
            sql.append(ChannelDangerTableColumn.DateTime + "='" + channelDangerTableDataClass.getDateTime() + "',");
            sql.append(ChannelDangerTableColumn.UserName + "='" + channelDangerTableDataClass.getUserName() + "',");
            sql.append(ChannelDangerTableColumn.Geometry + "=");
            sql.append("GeomFromText('");
            sql.append(channelDangerTableDataClass.getGeometry() + "'," + MyString.gps_srid);
            sql.append(")");

            sql.append(" where ROWID=" + channelDangerTableDataClass.getRowId());

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }


    /**
     * 获得通道隐患点表中某一通道所有的更新过的隐患点信息
     * @return
     */
    public List<ChannelDangerTableDataClass> getRowFromUpdate(String channelname){
        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId + ",");
        sql.append(ChannelDangerTableColumn.HuiLuType + ",");
        sql.append(ChannelDangerTableColumn.Geometry);

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelDangerTableColumn.Version + " != "+ DangerVersion.DANGER_VERSION_INIT);
        sql.append(" and " + ChannelDangerTableColumn.ChannelName + " = '"+channelname+"'");
        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                channelDangerTableDataClass.setGeometry(stmt.column_string(15));
                list.add(channelDangerTableDataClass);
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
     * 获得通道隐患点表中某一通道所有的更新过的隐患点信息
     * @return
     */
    public List<ChannelDangerTableDataClass> getRowFromUpdate(int chnobjectid){
        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId + ",");
        sql.append(ChannelDangerTableColumn.HuiLuType + ",");

        sql.append(ChannelDangerTableColumn.Geometry);

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelDangerTableColumn.Version + " != "+ DangerVersion.DANGER_VERSION_INIT);
        sql.append(" and " + ChannelDangerTableColumn.ChannelObjectId + " = "+chnobjectid);
        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                channelDangerTableDataClass.setGeometry(stmt.column_string(15));
                list.add(channelDangerTableDataClass);
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
     * 获得通道隐患点表中所有的更新过的隐患点信息
     * @return
     */
    public List<ChannelDangerTableDataClass> getRowFromUpdate(){
        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId + ",");
        sql.append(ChannelDangerTableColumn.HuiLuType + ",");
        sql.append(ChannelDangerTableColumn.Geometry);

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelDangerTableColumn.Version + " != "+DangerVersion.DANGER_VERSION_INIT);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                channelDangerTableDataClass.setGeometry(stmt.column_string(15));
                list.add(channelDangerTableDataClass);
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

            sql.append(ChannelDangerTableColumn.Version + "=" + version+",");
            sql.append(ChannelDangerTableColumn.KeyID + "=" + keyid);
            sql.append(" where ROWID=" + rowid);

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            result = Integer.valueOf(String.valueOf(ll));
            return result;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }


    /**
     * 更新隐患点的图片
     * @param channelDangerTableDataClass
     * @return
     */
    public int updateDangerPicsJson(ChannelDangerTableDataClass channelDangerTableDataClass){

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");

            sql.append(PoleDangerTableColumn.PicsJson + "='" + channelDangerTableDataClass.getPicsJson()+"'");
            sql.append(" where ROWID=" + channelDangerTableDataClass.getRowId());

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }


    /**
     *
     * @param geometrywkt
     * @return
     */
    public List<ChannelDangerTableDataClass> getRowWithinGeometry(String geometrywkt){
        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId + ",");
        sql.append(ChannelDangerTableColumn.HuiLuType + ",");
        sql.append(ChannelDangerTableColumn.Geometry);

        sql.append(" FROM " + tableName);

        sql.append(" A WHERE " );
        sql.append("ST_Within(GeomFromText(ST_AsText(A." + ChannelDangerTableColumn.Geometry + ")), GeomFromText('"+geometrywkt+"')) = 1");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                channelDangerTableDataClass.setGeometry(stmt.column_string(15));
                list.add(channelDangerTableDataClass);
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
     * 按照矩形查询
     * @param minLatLng
     * @param maxLatLng
     * @return
     */
    public List<ChannelDangerTableDataClass> getRow(LatLng minLatLng, LatLng maxLatLng){

        List<ChannelDangerTableDataClass> list = new ArrayList<ChannelDangerTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.RowId + ",");
        sql.append(ChannelDangerTableColumn.DateTime + ",");
        sql.append(ChannelDangerTableColumn.PowerName + ",");
        sql.append(ChannelDangerTableColumn.DangerType + ",");
        sql.append(ChannelDangerTableColumn.ChannelName + ",");
        sql.append(ChannelDangerTableColumn.DangerName + ",");
        sql.append(ChannelDangerTableColumn.DangerLevel + ",");
        sql.append(ChannelDangerTableColumn.PicsJson + ",");
        sql.append(ChannelDangerTableColumn.DangerMark + ",");
        sql.append(ChannelDangerTableColumn.SpotMark + ",");
        sql.append(ChannelDangerTableColumn.UserName + ",");
        sql.append(ChannelDangerTableColumn.KeyID + ",");
        sql.append(ChannelDangerTableColumn.Version + ",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId + ",");
        sql.append(ChannelDangerTableColumn.HuiLuType + ",");
        sql.append(ChannelDangerTableColumn.Geometry);
        sql.append(" FROM " + tableName);

        sql.append(" WHERE ");
        sql.append("X("+PoleTableColumn.Geometry+") > "+minLatLng.longitude);
        sql.append(" AND ");
        sql.append("Y("+PoleTableColumn.Geometry+") > "+minLatLng.latitude);
        sql.append(" AND ");
        sql.append("X("+PoleTableColumn.Geometry+") < "+maxLatLng.longitude);
        sql.append(" AND ");
        sql.append("Y("+PoleTableColumn.Geometry+") < "+maxLatLng.latitude);


        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();
                channelDangerTableDataClass.setRowId(stmt.column_int(0));
                channelDangerTableDataClass.setDateTime(stmt.column_string(1));
                channelDangerTableDataClass.setPowerName(stmt.column_string(2));
                channelDangerTableDataClass.setDangerType(stmt.column_int(3));
                channelDangerTableDataClass.setChannelName(stmt.column_string(4));
                channelDangerTableDataClass.setDangerName(stmt.column_string(5));
                channelDangerTableDataClass.setDangerLevel(stmt.column_int(6));
                channelDangerTableDataClass.setPicsJson(stmt.column_string(7));
                channelDangerTableDataClass.setDangerMark(stmt.column_string(8));
                channelDangerTableDataClass.setSpotMark(stmt.column_string(9));
                channelDangerTableDataClass.setUserName(stmt.column_string(10));
                channelDangerTableDataClass.setKeyID(stmt.column_int(11));
                channelDangerTableDataClass.setVersion(stmt.column_int(12));
                channelDangerTableDataClass.setChannelObjectId(stmt.column_int(13));
                channelDangerTableDataClass.setHuiLuType(stmt.column_int(14));
                channelDangerTableDataClass.setGeometry(stmt.column_string(15));
                list.add(channelDangerTableDataClass);
            }
            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



}
