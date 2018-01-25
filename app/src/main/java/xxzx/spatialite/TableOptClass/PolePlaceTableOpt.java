package xxzx.spatialite.TableOptClass;

import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Stmt;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.ChannelTableColumn;
import xxzx.spatialite.TableStruct.PolePlaceTableColumn;
import xxzx.spatialite.TableStruct.PolePlaceTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableColumn;
import xxzx.spatialite.TableStruct.PoleTableDataClass;

/**
 * Created by ch on 2016/4/2.
 */
public class PolePlaceTableOpt extends BaseTableOpt{

    private String TAG="PoleTableOpt";

    public PolePlaceTableOpt(){
        super();
        this.tableName=MyString.pole_place_table_name;
    }

    @Override
    public int insert(Object row){

        PolePlaceTableDataClass polePlaceTableDataClass = (PolePlaceTableDataClass)row;

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("insert into '" + this.tableName + "' ");
            sql.append("(");

            /////////////////////////////////////////////////////////////////////////
            sql.append(PolePlaceTableColumn.DateTime + ",");
            sql.append(PolePlaceTableColumn.PowerName + ",");
            sql.append(PolePlaceTableColumn.PoleName + ",");
            sql.append(PolePlaceTableColumn.PoleObjectId + ",");
            sql.append(PolePlaceTableColumn.UserName);

            sql.append(") values ('");
            sql.append(polePlaceTableDataClass.getDateTime() + "','");
            sql.append(polePlaceTableDataClass.getPowerName() + "','");
            sql.append(polePlaceTableDataClass.getPoleName() + "',");
            sql.append(polePlaceTableDataClass.getPoleObjectId() + ",'");
            sql.append(polePlaceTableDataClass.getUserName() + "')");

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
    public List<Object> getRow() {
        List<Object> list = new ArrayList<Object>();
        return list;
    }



    public List<PolePlaceTableDataClass> getAllRow(){
        List<PolePlaceTableDataClass>  list = new ArrayList<PolePlaceTableDataClass>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PolePlaceTableColumn.RowId + ",");
        sql.append(PolePlaceTableColumn.DateTime + ",");
        sql.append(PolePlaceTableColumn.PowerName + ",");
        sql.append(PolePlaceTableColumn.UserName + ",");
        sql.append(PolePlaceTableColumn.PoleName + ",");
        sql.append(PolePlaceTableColumn.PoleObjectId);

        sql.append(" FROM " + tableName);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                PolePlaceTableDataClass polePlaceTableDataClass = new PolePlaceTableDataClass();
                polePlaceTableDataClass.setRowId(stmt.column_int(0));
                polePlaceTableDataClass.setDateTime(stmt.column_string(1));
                polePlaceTableDataClass.setPowerName(stmt.column_string(2));
                polePlaceTableDataClass.setUserName(stmt.column_string(3));
                polePlaceTableDataClass.setPoleName(stmt.column_string(4));
                polePlaceTableDataClass.setPoleObjectId(stmt.column_int(5));

                list.add(polePlaceTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    public List<PolePlaceTableDataClass> getRowFromObjectId(int poleobjectid){

        List<PolePlaceTableDataClass> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(PolePlaceTableColumn.RowId + ",");
        sql.append(PolePlaceTableColumn.DateTime + ",");
        sql.append(PolePlaceTableColumn.PowerName + ",");
        sql.append(PolePlaceTableColumn.UserName + ",");
        sql.append(PolePlaceTableColumn.PoleName + ",");
        sql.append(PolePlaceTableColumn.PoleObjectId);

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + PolePlaceTableColumn.PoleObjectId + " = " + poleobjectid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {

            while (stmt.step()) {
                PolePlaceTableDataClass polePlaceTableDataClass = new PolePlaceTableDataClass();
                polePlaceTableDataClass.setRowId(stmt.column_int(0));
                polePlaceTableDataClass.setDateTime(stmt.column_string(1));
                polePlaceTableDataClass.setPowerName(stmt.column_string(2));
                polePlaceTableDataClass.setUserName(stmt.column_string(3));
                polePlaceTableDataClass.setPoleName(stmt.column_string(4));
                polePlaceTableDataClass.setPoleObjectId(stmt.column_int(5));

                list.add(polePlaceTableDataClass);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return list;
        }
    }



    public int update(PolePlaceTableDataClass polePlaceTableDataClass){

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");

            sql.append(PolePlaceTableColumn.PowerName + "='" + polePlaceTableDataClass.getPowerName() + "',");
            sql.append(PolePlaceTableColumn.UserName + "='" + polePlaceTableDataClass.getUserName() + "',");
            sql.append(PolePlaceTableColumn.PoleName + "='" + polePlaceTableDataClass.getPoleName() + "',");
            sql.append(PolePlaceTableColumn.PoleObjectId + "=" + polePlaceTableDataClass.getPoleObjectId() + ",");
            sql.append(PolePlaceTableColumn.DateTime + "='" + polePlaceTableDataClass.getDateTime() + "'");

            sql.append(" where ROWID = " + polePlaceTableDataClass.getRowId());

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }



    public int delete(){

        int result=-1;

        StringBuilder sql = new StringBuilder("");
        sql.append("DELETE FROM ");
        sql.append(tableName);

        result = this.spatialiteDataOpt.DeleteExecute(sql.toString());

        return result;
    }





}
