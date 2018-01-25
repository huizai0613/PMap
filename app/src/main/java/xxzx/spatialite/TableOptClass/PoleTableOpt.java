package xxzx.spatialite.TableOptClass;

import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Stmt;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.PoleTableColumn;
import xxzx.spatialite.TableStruct.PoleTableDataClass;

/**
 * Created by ch on 2016/4/2.
 */
public class PoleTableOpt extends BaseTableOpt{

    private String TAG="PoleTableOpt";

    public PoleTableOpt(){
        super();
        this.tableName=MyString.pole_table_name;
    }

    @Override
    public int insert(Object row){

        PoleTableDataClass patrolTableDataClass = (PoleTableDataClass)row;

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("insert into '" + this.tableName + "' ");
            sql.append("(");

            /////////////////////////////////////////////////////////////////////////
            sql.append(PoleTableColumn.IsSelect + ",");
            sql.append(PoleTableColumn.DateTime + ",");
            sql.append(PoleTableColumn.PowerName + ",");
            sql.append(PoleTableColumn.PoleName + ",");
            sql.append(PoleTableColumn.PoleObjectId + ",");
            sql.append(PoleTableColumn.PoleNumber + ",");
            sql.append(PoleTableColumn.DangerCount + ",");
            sql.append(PoleTableColumn.Geometry);

            sql.append(") values (");
            sql.append(patrolTableDataClass.getIsSelect() + ",'");
            sql.append(patrolTableDataClass.getDateTime() + "','");
            sql.append(patrolTableDataClass.getPowerName() + "','");
            sql.append(patrolTableDataClass.getPoleName() + "',");
            sql.append(patrolTableDataClass.getPoleObjectId() + ",");
            sql.append(patrolTableDataClass.getPoleNumber() + ",");
            sql.append(patrolTableDataClass.getDangerCount() + ",");
            sql.append("GeomFromText('");
            sql.append(patrolTableDataClass.getGeometry() + "'," + MyString.gps_srid);
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



    public List<PoleTableDataClass> getRow(String powername){
        List<PoleTableDataClass> list = new ArrayList<PoleTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleTableColumn.RowId + ",");
        sql.append(PoleTableColumn.DateTime + ",");
        sql.append(PoleTableColumn.PowerName + ",");
        sql.append(PoleTableColumn.IsSelect + ",");
        sql.append(PoleTableColumn.PoleName + ",");
        sql.append(PoleTableColumn.PoleNumber + ",");
        sql.append(PoleTableColumn.DangerCount + ",");
        sql.append(PoleTableColumn.PoleObjectId + ",");
        sql.append("AsText(" + PoleTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleTableColumn.PowerName + " = '" + powername + "'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                PoleTableDataClass poleTableDataClass = new PoleTableDataClass();
                poleTableDataClass.setRowId(stmt.column_int(0));
                poleTableDataClass.setDateTime(stmt.column_string(1));
                poleTableDataClass.setPowerName(stmt.column_string(2));
                poleTableDataClass.setIsSelect(stmt.column_int(3));
                poleTableDataClass.setPoleName(stmt.column_string(4));
                poleTableDataClass.setPoleNumber(stmt.column_int(5));
                poleTableDataClass.setDangerCount(stmt.column_int(6));
                poleTableDataClass.setPoleObjectId(stmt.column_int(7));
                poleTableDataClass.setGeometry(stmt.column_string(8));

                list.add(poleTableDataClass);
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
    public List<PoleTableDataClass> getRow(String powername,LatLng minLatLng,LatLng maxLatLng){

        List<PoleTableDataClass> list = new ArrayList<PoleTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleTableColumn.RowId + ",");
        sql.append(PoleTableColumn.DateTime + ",");
        sql.append(PoleTableColumn.PowerName + ",");
        sql.append(PoleTableColumn.IsSelect + ",");
        sql.append(PoleTableColumn.PoleName + ",");
        sql.append(PoleTableColumn.PoleNumber + ",");
        sql.append(PoleTableColumn.DangerCount + ",");
        sql.append(PoleTableColumn.PoleObjectId + ",");
        sql.append("AsText(" + PoleTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);

        sql.append(" WHERE ");
        sql.append("X("+PoleTableColumn.Geometry+") > "+minLatLng.longitude);
        sql.append(" AND ");
        sql.append("Y("+PoleTableColumn.Geometry+") > "+minLatLng.latitude);
        sql.append(" AND ");
        sql.append("X("+PoleTableColumn.Geometry+") < "+maxLatLng.longitude);
        sql.append(" AND ");
        sql.append("Y("+PoleTableColumn.Geometry+") < "+maxLatLng.latitude);
        sql.append(" AND ");
        sql.append(PoleTableColumn.PowerName + " = '" + powername + "'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                PoleTableDataClass poleTableDataClass = new PoleTableDataClass();
                poleTableDataClass.setRowId(stmt.column_int(0));
                poleTableDataClass.setDateTime(stmt.column_string(1));
                poleTableDataClass.setPowerName(stmt.column_string(2));
                poleTableDataClass.setIsSelect(stmt.column_int(3));
                poleTableDataClass.setPoleName(stmt.column_string(4));
                poleTableDataClass.setPoleNumber(stmt.column_int(5));
                poleTableDataClass.setDangerCount(stmt.column_int(6));
                poleTableDataClass.setPoleObjectId(stmt.column_int(7));
                poleTableDataClass.setGeometry(stmt.column_string(8));

                list.add(poleTableDataClass);
            }
            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    public List<PoleTableDataClass> getRowFromObjectId(int poleobjectid){
        List<PoleTableDataClass> list = new ArrayList<PoleTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleTableColumn.RowId + ",");
        sql.append(PoleTableColumn.DateTime + ",");
        sql.append(PoleTableColumn.PowerName + ",");
        sql.append(PoleTableColumn.IsSelect + ",");
        sql.append(PoleTableColumn.PoleName + ",");
        sql.append(PoleTableColumn.PoleNumber + ",");
        sql.append(PoleTableColumn.DangerCount + ",");
        sql.append(PoleTableColumn.PoleObjectId + ",");
        sql.append("AsText(" + PoleTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleTableColumn.PoleObjectId + " = " + poleobjectid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                PoleTableDataClass poleTableDataClass = new PoleTableDataClass();
                poleTableDataClass.setRowId(stmt.column_int(0));
                poleTableDataClass.setDateTime(stmt.column_string(1));
                poleTableDataClass.setPowerName(stmt.column_string(2));
                poleTableDataClass.setIsSelect(stmt.column_int(3));
                poleTableDataClass.setPoleName(stmt.column_string(4));
                poleTableDataClass.setPoleNumber(stmt.column_int(5));
                poleTableDataClass.setDangerCount(stmt.column_int(6));
                poleTableDataClass.setPoleObjectId(stmt.column_int(7));
                poleTableDataClass.setGeometry(stmt.column_string(8));

                list.add(poleTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }




    public List<PoleTableDataClass> getMyTaskRow(String powername){
        List<PoleTableDataClass> list = new ArrayList<PoleTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleTableColumn.RowId + ",");
        sql.append(PoleTableColumn.DateTime + ",");
        sql.append(PoleTableColumn.PowerName + ",");
        sql.append(PoleTableColumn.IsSelect + ",");
        sql.append(PoleTableColumn.PoleName + ",");
        sql.append(PoleTableColumn.PoleNumber + ",");
        sql.append(PoleTableColumn.DangerCount + ",");
        sql.append(PoleTableColumn.PoleObjectId + ",");
        sql.append("AsText(" + PoleTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PoleTableColumn.PowerName + " = '" + powername + "'");
        sql.append(" and " + PoleTableColumn.IsSelect + " = 1");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                PoleTableDataClass poleTableDataClass = new PoleTableDataClass();
                poleTableDataClass.setRowId(stmt.column_int(0));
                poleTableDataClass.setDateTime(stmt.column_string(1));
                poleTableDataClass.setPowerName(stmt.column_string(2));
                poleTableDataClass.setIsSelect(stmt.column_int(3));
                poleTableDataClass.setPoleName(stmt.column_string(4));
                poleTableDataClass.setPoleNumber(stmt.column_int(5));
                poleTableDataClass.setDangerCount(stmt.column_int(6));
                poleTableDataClass.setPoleObjectId(stmt.column_int(7));
                poleTableDataClass.setGeometry(stmt.column_string(8));

                list.add(poleTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    public int update(PoleTableDataClass poleTableDataClass){

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");

            sql.append(PoleTableColumn.PowerName + "='" + poleTableDataClass.getPowerName() + "',");
            sql.append(PoleTableColumn.PoleNumber + "=" + poleTableDataClass.getPoleNumber() + ",");
            sql.append(PoleTableColumn.PoleName + "='" + poleTableDataClass.getPoleName() + "',");
            sql.append(PoleTableColumn.PoleObjectId + "=" + poleTableDataClass.getPoleName() + ",");
            sql.append(PoleTableColumn.DateTime + "='" + poleTableDataClass.getDateTime() + "',");
            sql.append(PoleTableColumn.DangerCount + "=" + poleTableDataClass.getDangerCount() + ",");
            sql.append(PoleTableColumn.Geometry );
            sql.append("= GeomFromText('" );
            sql.append(poleTableDataClass.getGeometry()+ "'," + MyString.gps_srid + ")");

            sql.append( ",");
            sql.append(PoleTableColumn.IsSelect + "=" + poleTableDataClass.getIsSelect() + "");

            sql.append(" where ROWID=" + poleTableDataClass.getRowId());

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
     * @param polename 杆塔名称
     * @param checked
     * @return
     */
    public int updateTaskFromPoleName(String polename,boolean checked){
        int rowid = -1;
        try {

            int select=checked?1:0;

            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");
            sql.append(PoleTableColumn.IsSelect + "=" + select + "");

            sql.append(" where ");
            sql.append(PoleTableColumn.PoleName + "='" + polename+"'");

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
    public int updateTaskFromPlineName(String plinename,boolean checked){
        int rowid = -1;
        try {
            int select=checked?1:0;

            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");
            sql.append(PoleTableColumn.IsSelect + "=" + select + "");

            sql.append(" where ");
            sql.append(PoleTableColumn.PowerName + "='" + plinename+"'");

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }


    public int delete(List<String> plinenames){

        int result=-1;

        StringBuilder builder=new StringBuilder("");
        for(int i=0;i<plinenames.size();i++){
            if(i<plinenames.size()-1) {
                builder.append(PoleTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
                builder.append(" or ");
            }else{
                builder.append(PoleTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
            }
        }

        StringBuilder sql = new StringBuilder("");
        sql.append("DELETE FROM ");
        sql.append(tableName);
        sql.append(" WHERE " + builder.toString());

        result=this.spatialiteDataOpt.DeleteExecute(sql.toString());

        return result;
    }



    public List<PoleTableDataClass> getRowWithinGeometry(String geometrywkt){
        List<PoleTableDataClass> list = new ArrayList<PoleTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PoleTableColumn.PoleObjectId);
        sql.append(" FROM " + tableName);

        sql.append(" A WHERE " );
        sql.append("ST_Within(GeomFromText(ST_AsText(A." + PoleTableColumn.Geometry + ")), GeomFromText('"+geometrywkt+"')) = 1");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                PoleTableDataClass poleTableDataClass = new PoleTableDataClass();
                poleTableDataClass.setPoleObjectId(stmt.column_int(0));
                list.add(poleTableDataClass);
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
