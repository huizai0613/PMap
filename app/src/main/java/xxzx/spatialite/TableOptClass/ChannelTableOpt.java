package xxzx.spatialite.TableOptClass;

import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Stmt;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.ChannelTableColumn;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableColumn;

/**
 * Created by ch on 2016/4/2.
 */
public class ChannelTableOpt extends BaseTableOpt{

    private String TAG="ChannelTableOpt";

    public ChannelTableOpt(){
        super();
        this.tableName=MyString.channel_table_name;
    }

    @Override
    public int insert(Object row){

        ChannelTableDataClass channelTableDataClass = (ChannelTableDataClass)row;

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("insert into '" + MyString.channel_table_name + "' ");
            sql.append("(");

            /////////////////////////////////////////////////////////////////////////
            sql.append(ChannelTableColumn.IsSelect + ",");
            sql.append(ChannelTableColumn.DateTime + ",");
            sql.append(ChannelTableColumn.PowerName + ",");
            sql.append(ChannelTableColumn.ChannelName + ",");
            sql.append(ChannelTableColumn.ChannelObjectId + ",");
            sql.append(ChannelTableColumn.ChannelNumber + ",");
            sql.append(ChannelTableColumn.DangerCount + ",");
            sql.append(ChannelTableColumn.Geometry);

            sql.append(") values (");
            sql.append(channelTableDataClass.getIsSelect() + ",'");
            sql.append(channelTableDataClass.getDateTime() + "','");
            sql.append(channelTableDataClass.getPowerName() + "','");
            sql.append(channelTableDataClass.getChannelName() + "',");
            sql.append(channelTableDataClass.getChannelObjectId() + ",");
            sql.append(channelTableDataClass.getChannelNumber() + ",");
            sql.append(channelTableDataClass.getDangerCount() + ",");
            sql.append("GeomFromText('");
            sql.append(channelTableDataClass.getGeometry() + "'," + MyString.gps_srid);
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
    public List<Object> getRow(int rowid){
        List<Object>  list = new ArrayList<Object>();


        return list;
    }


    @Override
    public List<Object> getRow(){
        List<Object>  list = new ArrayList<Object>();


        return list;
    }





    public List<ChannelTableDataClass> getRow(String powername){

        List<ChannelTableDataClass> list = new ArrayList<ChannelTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelTableColumn.RowId + ",");
        sql.append(ChannelTableColumn.DateTime + ",");
        sql.append(ChannelTableColumn.PowerName + ",");
        sql.append(ChannelTableColumn.IsSelect + ",");
        sql.append(ChannelTableColumn.ChannelName + ",");
        sql.append(ChannelTableColumn.ChannelNumber + ",");
        sql.append(ChannelTableColumn.DangerCount + ",");
        sql.append(ChannelTableColumn.ChannelObjectId + ",");
        sql.append("AsText(" + ChannelTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelTableColumn.PowerName + " = '" + powername + "'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelTableDataClass channelTableDataClass = new ChannelTableDataClass();
                channelTableDataClass.setRowId(stmt.column_int(0));
                channelTableDataClass.setDateTime(stmt.column_string(1));
                channelTableDataClass.setPowerName(stmt.column_string(2));
                channelTableDataClass.setIsSelect(stmt.column_int(3));
                channelTableDataClass.setChannelName(stmt.column_string(4));
                channelTableDataClass.setChannelNumber(stmt.column_int(5));
                channelTableDataClass.setDangerCount(stmt.column_int(6));
                channelTableDataClass.setChannelObjectId(stmt.column_int(7));
                channelTableDataClass.setGeometry(stmt.column_string(8));

                list.add(channelTableDataClass);
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
     * 通过矩形空间查询数据表，不使用空间检索
     * @param powername
     * @param minLatLng
     * @param maxLatLng
     * @return
     */
    public List<ChannelTableDataClass> getRow(String powername,LatLng minLatLng,LatLng maxLatLng){

        List<ChannelTableDataClass> list = new ArrayList<ChannelTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelTableColumn.RowId + ",");
        sql.append(ChannelTableColumn.DateTime + ",");
        sql.append(ChannelTableColumn.PowerName + ",");
        sql.append(ChannelTableColumn.IsSelect + ",");
        sql.append(ChannelTableColumn.ChannelName + ",");
        sql.append(ChannelTableColumn.ChannelNumber + ",");
        sql.append(ChannelTableColumn.DangerCount + ",");
        sql.append(ChannelTableColumn.ChannelObjectId + ",");
        sql.append("AsText(" + ChannelTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);

        sql.append(" WHERE ");
        sql.append("X(ST_Centroid("+ChannelTableColumn.Geometry+")) > "+minLatLng.longitude);
        sql.append(" AND ");
        sql.append("Y(ST_Centroid("+ChannelTableColumn.Geometry+")) > "+minLatLng.latitude);
        sql.append(" AND ");
        sql.append("X(ST_Centroid("+ChannelTableColumn.Geometry+")) < "+maxLatLng.longitude);
        sql.append(" AND ");
        sql.append("Y(ST_Centroid("+ChannelTableColumn.Geometry+")) < "+maxLatLng.latitude);
        sql.append(" AND ");
        sql.append(PoleTableColumn.PowerName + " = '" + powername + "'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {
                ChannelTableDataClass channelTableDataClass = new ChannelTableDataClass();
                channelTableDataClass.setRowId(stmt.column_int(0));
                channelTableDataClass.setDateTime(stmt.column_string(1));
                channelTableDataClass.setPowerName(stmt.column_string(2));
                channelTableDataClass.setIsSelect(stmt.column_int(3));
                channelTableDataClass.setChannelName(stmt.column_string(4));
                channelTableDataClass.setChannelNumber(stmt.column_int(5));
                channelTableDataClass.setDangerCount(stmt.column_int(6));
                channelTableDataClass.setChannelObjectId(stmt.column_int(7));
                channelTableDataClass.setGeometry(stmt.column_string(8));

                list.add(channelTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }




    public List<ChannelTableDataClass> getRowFromChannelName(String channelname){

        List<ChannelTableDataClass> list = new ArrayList<ChannelTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelTableColumn.RowId + ",");
        sql.append(ChannelTableColumn.DateTime + ",");
        sql.append(ChannelTableColumn.PowerName + ",");
        sql.append(ChannelTableColumn.IsSelect + ",");
        sql.append(ChannelTableColumn.ChannelName + ",");
        sql.append(ChannelTableColumn.ChannelNumber + ",");
        sql.append(ChannelTableColumn.DangerCount + ",");
        sql.append(ChannelTableColumn.ChannelObjectId + ",");
        sql.append("AsText(" + ChannelTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelTableColumn.ChannelName + " = '" + channelname + "'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                ChannelTableDataClass channelTableDataClass = new ChannelTableDataClass();
                channelTableDataClass.setRowId(stmt.column_int(0));
                channelTableDataClass.setDateTime(stmt.column_string(1));
                channelTableDataClass.setPowerName(stmt.column_string(2));
                channelTableDataClass.setIsSelect(stmt.column_int(3));
                channelTableDataClass.setChannelName(stmt.column_string(4));
                channelTableDataClass.setChannelNumber(stmt.column_int(5));
                channelTableDataClass.setDangerCount(stmt.column_int(6));
                channelTableDataClass.setChannelObjectId(stmt.column_int(7));
                channelTableDataClass.setGeometry(stmt.column_string(8));

                list.add(channelTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }

    public List<ChannelTableDataClass> getRowFromChnObjectId(int chnobjectid){

        List<ChannelTableDataClass> list = new ArrayList<ChannelTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelTableColumn.RowId + ",");
        sql.append(ChannelTableColumn.DateTime + ",");
        sql.append(ChannelTableColumn.PowerName + ",");
        sql.append(ChannelTableColumn.IsSelect + ",");
        sql.append(ChannelTableColumn.ChannelName + ",");
        sql.append(ChannelTableColumn.ChannelNumber + ",");
        sql.append(ChannelTableColumn.DangerCount + ",");
        sql.append(ChannelTableColumn.ChannelObjectId + ",");
        sql.append("AsText(" + ChannelTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelTableColumn.ChannelObjectId + " = " + chnobjectid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                ChannelTableDataClass channelTableDataClass = new ChannelTableDataClass();
                channelTableDataClass.setRowId(stmt.column_int(0));
                channelTableDataClass.setDateTime(stmt.column_string(1));
                channelTableDataClass.setPowerName(stmt.column_string(2));
                channelTableDataClass.setIsSelect(stmt.column_int(3));
                channelTableDataClass.setChannelName(stmt.column_string(4));
                channelTableDataClass.setChannelNumber(stmt.column_int(5));
                channelTableDataClass.setDangerCount(stmt.column_int(6));
                channelTableDataClass.setChannelObjectId(stmt.column_int(7));
                channelTableDataClass.setGeometry(stmt.column_string(8));

                list.add(channelTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    public List<ChannelTableDataClass> getMyTaskRow(String powername){

        List<ChannelTableDataClass> list = new ArrayList<ChannelTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(ChannelTableColumn.RowId + ",");
        sql.append(ChannelTableColumn.DateTime + ",");
        sql.append(ChannelTableColumn.PowerName + ",");
        sql.append(ChannelTableColumn.IsSelect + ",");
        sql.append(ChannelTableColumn.ChannelName + ",");
        sql.append(ChannelTableColumn.ChannelNumber + ",");
        sql.append(ChannelTableColumn.DangerCount + ",");
        sql.append(ChannelTableColumn.ChannelObjectId + ",");
        sql.append("AsText(" + ChannelTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + ChannelTableColumn.PowerName + " = '" + powername + "'");
        sql.append(" and " + ChannelTableColumn.IsSelect + " = 1");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                ChannelTableDataClass channelTableDataClass = new ChannelTableDataClass();
                channelTableDataClass.setRowId(stmt.column_int(0));
                channelTableDataClass.setDateTime(stmt.column_string(1));
                channelTableDataClass.setPowerName(stmt.column_string(2));
                channelTableDataClass.setIsSelect(stmt.column_int(3));
                channelTableDataClass.setChannelName(stmt.column_string(4));
                channelTableDataClass.setChannelNumber(stmt.column_int(5));
                channelTableDataClass.setDangerCount(stmt.column_int(6));
                channelTableDataClass.setChannelObjectId(stmt.column_int(7));
                channelTableDataClass.setGeometry(stmt.column_string(8));

                list.add(channelTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    public int update(ChannelTableDataClass channelTableDataClass){

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");

            sql.append(ChannelTableColumn.PowerName + "='" + channelTableDataClass.getPowerName() + "',");
            sql.append(ChannelTableColumn.ChannelNumber + "=" + channelTableDataClass.getChannelNumber() + ",");
            sql.append(ChannelTableColumn.ChannelName + "='" + channelTableDataClass.getChannelName() + "',");
            sql.append(ChannelTableColumn.DateTime + "='" + channelTableDataClass.getDateTime() + "',");
            sql.append(ChannelTableColumn.DangerCount + "=" + channelTableDataClass.getDangerCount() + ",");
            sql.append(ChannelTableColumn.ChannelObjectId + "=" + channelTableDataClass.getChannelObjectId() + ",");

            sql.append(ChannelTableColumn.Geometry );
            sql.append("= GeomFromText('" );
            sql.append(channelTableDataClass.getGeometry()+ "'," + MyString.gps_srid + ")");

            sql.append( ",");
            sql.append(ChannelTableColumn.IsSelect + "=" + channelTableDataClass.getIsSelect() + "");

            sql.append(" where ROWID=" + channelTableDataClass.getRowId());

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }


    /**
     * 线路按要求标记是否为我的任务
     * @param channelname 通道名称
     * @param checked
     * @return
     */
    public int updateTaskFromChnName(String channelname,boolean checked){
        int rowid = -1;
        try {

            int select=checked?1:0;

            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");
            sql.append(ChannelTableColumn.IsSelect + "=" + select + "");

            sql.append(" where ");
            sql.append(ChannelTableColumn.ChannelName + "='" + channelname+"'");

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }


    /**
     * 线路按要求标记是否为我的任务
     * @param plinename 线路名称
     * @param checked
     * @return
     */
    public int updateTaskFormPlineName(String plinename,boolean checked){
        int rowid = -1;
        try {

            int select=checked?1:0;

            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");
            sql.append(ChannelTableColumn.IsSelect + "=" + select + "");

            sql.append(" where ");
            sql.append(ChannelTableColumn.PowerName + "='" + plinename+"'");

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }


    /**
     * 删除某一条线路
     * @param plinenames
     * @return
     */
    public int delete(List<String> plinenames){

        int result=-1;

        StringBuilder builder=new StringBuilder("");
        for(int i=0;i<plinenames.size();i++){
            if(i<plinenames.size()-1) {
                builder.append(ChannelTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
                builder.append(" or ");
            }else{
                builder.append(ChannelTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
            }
        }

        StringBuilder sql = new StringBuilder("");
        sql.append("DELETE FROM ");
        sql.append(tableName);
        sql.append(" WHERE " + builder.toString());

        result=this.spatialiteDataOpt.DeleteExecute(sql.toString());

        return result;
    }





}
