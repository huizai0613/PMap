package xxzx.spatialite.TableOptClass;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Stmt;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.PowerlineTableColumn;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

/**
 * Created by ch on 2016/4/2.
 */
public class PowerlineTableOpt extends BaseTableOpt {

    private String TAG = "PowerlineTableOpt";

    public PowerlineTableOpt() {
        super();
        this.tableName = MyString.powerline_table_name;
    }

    @Override
    public int insert(Object row) {
        PowerlineTableDataClass powerlineTableDataClass = (PowerlineTableDataClass) row;
        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("insert into '" + MyString.powerline_table_name + "' ");
            sql.append("(");

            sql.append(PowerlineTableColumn.IsInMap + ",");
            sql.append(PowerlineTableColumn.IsSelect + ",");
            sql.append(PowerlineTableColumn.DateTime + ",");
            sql.append(PowerlineTableColumn.PowerName + ",");
            sql.append(PowerlineTableColumn.UserName + ",");
            sql.append(PowerlineTableColumn.Geometry);

            sql.append(") values (");

            sql.append(powerlineTableDataClass.getIsInMap() + ",");
            sql.append(powerlineTableDataClass.getIsSelect() + ",'");
            sql.append(powerlineTableDataClass.getDateTime() + "','");
            sql.append(powerlineTableDataClass.getPowerName() + "','");
            sql.append(powerlineTableDataClass.getUserName() + "',");

            sql.append("GeomFromText('");
            sql.append(powerlineTableDataClass.getGeometry() + "'," + MyString.gps_srid);
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
    public List<Object> getRow(int rowid) {
        List<Object> list = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PowerlineTableColumn.RowId + ",");
        sql.append(PowerlineTableColumn.DateTime + ",");
        sql.append(PowerlineTableColumn.IsSelect + ",");
        sql.append(PowerlineTableColumn.PowerName + ",");
        sql.append(PowerlineTableColumn.UserName + ",");
        sql.append(PowerlineTableColumn.IsInMap + ",");
        sql.append("AsText(" + PowerlineTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PowerlineTableColumn.RowId + " = " + rowid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                PowerlineTableDataClass powerlineTableDataClass = new PowerlineTableDataClass();
                powerlineTableDataClass.setRowId(stmt.column_int(0));
                powerlineTableDataClass.setDateTime(stmt.column_string(1));
                powerlineTableDataClass.setIsSelect(stmt.column_int(2));
                powerlineTableDataClass.setPowerName(stmt.column_string(3));
                powerlineTableDataClass.setUserName(stmt.column_string(4));
                powerlineTableDataClass.setIsInMap(stmt.column_int(5));
                powerlineTableDataClass.setGeometry(stmt.column_string(6));
                list.add(powerlineTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }


    public List<PowerlineTableDataClass> getRow(String powername){

        List<PowerlineTableDataClass> list = new ArrayList<PowerlineTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PowerlineTableColumn.RowId + ",");
        sql.append(PowerlineTableColumn.DateTime + ",");
        sql.append(PowerlineTableColumn.PowerName + ",");
        sql.append(PowerlineTableColumn.IsSelect + ",");
        sql.append("AsText(" + PowerlineTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PowerlineTableColumn.PowerName + " = '" + powername + "'");

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                PowerlineTableDataClass powerlineTableDataClass = new PowerlineTableDataClass();
                powerlineTableDataClass.setRowId(stmt.column_int(0));
                powerlineTableDataClass.setDateTime(stmt.column_string(1));
                powerlineTableDataClass.setPowerName(stmt.column_string(2));
                powerlineTableDataClass.setIsSelect(stmt.column_int(3));
                powerlineTableDataClass.setGeometry(stmt.column_string(4));

                list.add(powerlineTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    public int getRowCountFormPowername(String powername) {
        int count = 0;
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT COUNT(*) FROM " + tableName);
        sql.append(" WHERE " + PowerlineTableColumn.PowerName + " = '" + powername + "'");



        try {
            Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

            while (stmt.step()) {
                count = stmt.column_int(0);
            }
            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return count;
        }
    }

    public List<PowerlineTableDataClass> getRowUnTask() {

        List<PowerlineTableDataClass> list = new ArrayList<PowerlineTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PowerlineTableColumn.RowId + ",");
        sql.append(PowerlineTableColumn.UserName + ",");
        sql.append(PowerlineTableColumn.PowerName + ",");
        sql.append(PowerlineTableColumn.IsSelect + ",");
        sql.append(PowerlineTableColumn.DateTime + ",");
        sql.append(PowerlineTableColumn.IsInMap + ",");
        sql.append("AsText(" + PowerlineTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PowerlineTableColumn.IsSelect + " = 0");


        try {
            Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

            while (stmt.step()) {
                PowerlineTableDataClass powerlineTableDataClass = new PowerlineTableDataClass();
                powerlineTableDataClass.setRowId(stmt.column_int(0));
                powerlineTableDataClass.setUserName(stmt.column_string(1));
                powerlineTableDataClass.setPowerName(stmt.column_string(2));
                powerlineTableDataClass.setIsSelect(stmt.column_int(3));
                powerlineTableDataClass.setDateTime(stmt.column_string(4));
                powerlineTableDataClass.setIsInMap(stmt.column_int(5));
                powerlineTableDataClass.setGeometry(stmt.column_string(6));

                list.add(powerlineTableDataClass);
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
    public List<Object> getRow() {

        List<Object> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PowerlineTableColumn.RowId + ",");
        sql.append(PowerlineTableColumn.UserName + ",");
        sql.append(PowerlineTableColumn.PowerName + ",");
        sql.append(PowerlineTableColumn.IsSelect + ",");
        sql.append(PowerlineTableColumn.DateTime + ",");
        sql.append(PowerlineTableColumn.IsInMap + ",");
        sql.append("AsText(" + PowerlineTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        try {

            Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

            while (stmt.step()) {
                PowerlineTableDataClass powerlineTableDataClass = new PowerlineTableDataClass();
                powerlineTableDataClass.setRowId(stmt.column_int(0));
                powerlineTableDataClass.setUserName(stmt.column_string(1));
                powerlineTableDataClass.setPowerName(stmt.column_string(2));
                powerlineTableDataClass.setIsSelect(stmt.column_int(3));
                powerlineTableDataClass.setDateTime(stmt.column_string(4));
                powerlineTableDataClass.setIsInMap(stmt.column_int(5));
                powerlineTableDataClass.setGeometry(stmt.column_string(6));

                list.add(powerlineTableDataClass);
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
     * 获取载入地图中的电力线
     * @return
     */
    public List<PowerlineTableDataClass> getRowInMap() {

        List<PowerlineTableDataClass> list = new ArrayList<PowerlineTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PowerlineTableColumn.RowId + ",");
        sql.append(PowerlineTableColumn.UserName + ",");
        sql.append(PowerlineTableColumn.PowerName + ",");
        sql.append(PowerlineTableColumn.IsSelect + ",");
        sql.append(PowerlineTableColumn.DateTime + ",");
        sql.append(PowerlineTableColumn.IsInMap + ",");
        sql.append("AsText(" + PowerlineTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PowerlineTableColumn.IsInMap + " = 1");

        try {
            Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

            while (stmt.step()) {
                PowerlineTableDataClass powerlineTableDataClass = new PowerlineTableDataClass();
                powerlineTableDataClass.setRowId(stmt.column_int(0));
                powerlineTableDataClass.setUserName(stmt.column_string(1));
                powerlineTableDataClass.setPowerName(stmt.column_string(2));
                powerlineTableDataClass.setIsSelect(stmt.column_int(3));
                powerlineTableDataClass.setDateTime(stmt.column_string(4));
                powerlineTableDataClass.setIsInMap(stmt.column_int(5));
                powerlineTableDataClass.setGeometry(stmt.column_string(6));

                list.add(powerlineTableDataClass);
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
                builder.append(PowerlineTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
                builder.append(" or ");
            }else{
                builder.append(PowerlineTableColumn.PowerName + " = '" + plinenames.get(i) + "'");
            }
        }

        StringBuilder sql = new StringBuilder("");
        sql.append("DELETE FROM ");
        sql.append(tableName);
        sql.append(" WHERE " + builder.toString());

        try {
            result = this.spatialiteDataOpt.DeleteExecute(sql.toString());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }

    }

    /**
     * 获得我的任务中的线路
     * @return
     */
    public List<PowerlineTableDataClass> getMyTaskRow() {

        List<PowerlineTableDataClass> list = new ArrayList<PowerlineTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PowerlineTableColumn.RowId + ",");
        sql.append(PowerlineTableColumn.UserName + ",");
        sql.append(PowerlineTableColumn.PowerName + ",");
        sql.append(PowerlineTableColumn.IsSelect + ",");
        sql.append(PowerlineTableColumn.DateTime + ",");
        sql.append(PowerlineTableColumn.IsInMap + ",");
        sql.append("AsText(" + PowerlineTableColumn.Geometry + ")");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PowerlineTableColumn.IsSelect + " = 1");


        try {

            Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

            while (stmt.step()) {
                PowerlineTableDataClass powerlineTableDataClass = new PowerlineTableDataClass();
                powerlineTableDataClass.setRowId(stmt.column_int(0));
                powerlineTableDataClass.setUserName(stmt.column_string(1));
                powerlineTableDataClass.setPowerName(stmt.column_string(2));
                powerlineTableDataClass.setIsSelect(stmt.column_int(3));
                powerlineTableDataClass.setDateTime(stmt.column_string(4));
                powerlineTableDataClass.setIsInMap(stmt.column_int(5));
                powerlineTableDataClass.setGeometry(stmt.column_string(6));

                list.add(powerlineTableDataClass);
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
     * 得到线路表中的所有线路的名称
     * @return
     */
    public List<String> getPlineNames() {
        List<String> list = new ArrayList<String>();
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PowerlineTableColumn.PowerName);
        sql.append(" FROM " + tableName);

        try {

            Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

            while (stmt.step()) {
                list.add(stmt.column_string(0));
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
     * 线路按要求标记是否为我的任务
     * @param plineName 线路名称
     * @param checked
     * @return
     */
    public int updateTask(String plineName,boolean checked){
        int rowid = -1;
        try {

            int select=checked?1:0;

            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");
            sql.append(PowerlineTableColumn.IsSelect + "=" + select + "");

            sql.append(" where ");
            sql.append(PowerlineTableColumn.PowerName + "='" + plineName+"'");

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    /**
     * 线路按要求标记是否假如地图
     * @param plineName 线路名称
     * @param checked
     * @return
     */
    public int updateInMap(String plineName,boolean checked){
        int rowid = -1;
        try {

            int select=checked?1:0;

            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");
            sql.append(PowerlineTableColumn.IsInMap + "=" + select + "");

            sql.append(" where ");
            sql.append(PowerlineTableColumn.PowerName + "='" + plineName+"'");

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }
}
