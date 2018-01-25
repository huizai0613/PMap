package xxzx.spatialite.TableOptClass;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Stmt;
import xxzx.publicClass.DangerVersion;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.ChannelDangerTableColumn;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.DangerSgTableColumn;
import xxzx.spatialite.TableStruct.DangerSgTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableColumn;

/**
 * Created by ch on 2016/4/2.
 */
public class DangerSgTableOpt extends BaseTableOpt {

    private String TAG="DangerSgTableOpt";

    public DangerSgTableOpt(){
        super();
        this.tableName= MyString.danger_sg_table_name;
    }

    @Override
    public int insert(Object object){

        DangerSgTableDataClass dangerSgTableDataClass = (DangerSgTableDataClass)object;

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("insert into '" + MyString.danger_sg_table_name + "' ");
            sql.append("(");

            /////////////////////////////////////////////////////////////////////////
            sql.append(DangerSgTableColumn.Location + ",");
            sql.append(DangerSgTableColumn.Guank1 + ",");
            sql.append(DangerSgTableColumn.Guank2 + ",");
            sql.append(DangerSgTableColumn.Guank3 + ",");
            sql.append(DangerSgTableColumn.Guank4 + ",");
            sql.append(DangerSgTableColumn.Guank5 + ",");
            sql.append(DangerSgTableColumn.Keeper + ",");
            sql.append(DangerSgTableColumn.Keeperphone + ",");
            sql.append(DangerSgTableColumn.Sgcontact + ",");
            sql.append(DangerSgTableColumn.Sgdepartment + ",");
            sql.append(DangerSgTableColumn.Sgphone + ",");
            sql.append(DangerSgTableColumn.Ywcontact + ",");
            sql.append(DangerSgTableColumn.Ywphone + ",");
            sql.append(DangerSgTableColumn.PlineName + ",");
            sql.append(DangerSgTableColumn.ChnName + ",");
            sql.append(DangerSgTableColumn.ChnObjectId + ",");
            sql.append(DangerSgTableColumn.DangerRowId);

            sql.append(") values (");

            sql.append("'");
            sql.append(dangerSgTableDataClass.getLocation() + "','");
            sql.append(dangerSgTableDataClass.getGuank1() + "','");
            sql.append(dangerSgTableDataClass.getGuank2() + "','");
            sql.append(dangerSgTableDataClass.getGuank3() + "','");
            sql.append(dangerSgTableDataClass.getGuank4() + "','");
            sql.append(dangerSgTableDataClass.getGuank5() + "','");
            sql.append(dangerSgTableDataClass.getKeeper() + "','");
            sql.append(dangerSgTableDataClass.getKeeperphone() + "','");
            sql.append(dangerSgTableDataClass.getSgcontact() + "','");
            sql.append(dangerSgTableDataClass.getSgdepartment() + "','");
            sql.append(dangerSgTableDataClass.getSgphone() + "','");
            sql.append(dangerSgTableDataClass.getYwcontact() + "','");
            sql.append(dangerSgTableDataClass.getYwphone() + "','");
            sql.append(dangerSgTableDataClass.getPlineName() + "','");
            sql.append(dangerSgTableDataClass.getChnName() + "',");
            sql.append(dangerSgTableDataClass.getChnObjectId() + ",");
            sql.append(dangerSgTableDataClass.getDangerRowId());
            sql.append(")");

            long ll = this.spatialiteDataOpt.InsertExecute(sql.toString());

            rowid = Integer.parseInt(String.valueOf(ll));
            return rowid;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    @Override
    public List<Object> getRow(){
        List<Object> list = new ArrayList<>();
        return list;
    }


    @Override
    public Object getRow(int rowid){

        DangerSgTableDataClass dangerSgTableDataClass = new DangerSgTableDataClass();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");

        sql.append(DangerSgTableColumn.RowId + ",");
        sql.append(DangerSgTableColumn.Location + ",");
        sql.append(DangerSgTableColumn.Guank1 + ",");
        sql.append(DangerSgTableColumn.Guank2 + ",");
        sql.append(DangerSgTableColumn.Guank3 + ",");
        sql.append(DangerSgTableColumn.Guank4 + ",");
        sql.append(DangerSgTableColumn.Guank5 + ",");
        sql.append(DangerSgTableColumn.Keeper + ",");
        sql.append(DangerSgTableColumn.Keeperphone + ",");
        sql.append(DangerSgTableColumn.Sgcontact + ",");
        sql.append(DangerSgTableColumn.Sgdepartment + ",");
        sql.append(DangerSgTableColumn.Sgphone + ",");
        sql.append(DangerSgTableColumn.Ywcontact + ",");
        sql.append(DangerSgTableColumn.Ywphone + ",");
        sql.append(DangerSgTableColumn.PlineName + ",");
        sql.append(DangerSgTableColumn.ChnName + ",");
        sql.append(DangerSgTableColumn.ChnObjectId + ",");
        sql.append(DangerSgTableColumn.DangerRowId);

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + DangerSgTableColumn.RowId + " = " + rowid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {

                dangerSgTableDataClass.setRowId(stmt.column_int(0));
                dangerSgTableDataClass.setLocation(stmt.column_string(1));
                dangerSgTableDataClass.setGuank1(stmt.column_string(2));
                dangerSgTableDataClass.setGuank2(stmt.column_string(3));
                dangerSgTableDataClass.setGuank3(stmt.column_string(4));
                dangerSgTableDataClass.setGuank4(stmt.column_string(5));
                dangerSgTableDataClass.setGuank5(stmt.column_string(6));
                dangerSgTableDataClass.setKeeper(stmt.column_string(7));
                dangerSgTableDataClass.setKeeperphone(stmt.column_string(8));
                dangerSgTableDataClass.setSgcontact(stmt.column_string(9));
                dangerSgTableDataClass.setSgdepartment(stmt.column_string(10));
                dangerSgTableDataClass.setSgphone(stmt.column_string(11));
                dangerSgTableDataClass.setYwcontact(stmt.column_string(12));
                dangerSgTableDataClass.setYwphone(stmt.column_string(13));
                dangerSgTableDataClass.setPlineName(stmt.column_string(14));
                dangerSgTableDataClass.setChnName(stmt.column_string(15));
                dangerSgTableDataClass.setChnObjectId(stmt.column_int(16));
                dangerSgTableDataClass.setDangerRowId(stmt.column_int(17));
            }
            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return dangerSgTableDataClass;
        }
    }


    public DangerSgTableDataClass getRowFormDangerRowID(int danger_rowid){

        DangerSgTableDataClass dangerSgTableDataClass = new DangerSgTableDataClass();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append(DangerSgTableColumn.RowId + ",");
        sql.append(DangerSgTableColumn.Location + ",");
        sql.append(DangerSgTableColumn.Guank1 + ",");
        sql.append(DangerSgTableColumn.Guank2 + ",");
        sql.append(DangerSgTableColumn.Guank3 + ",");
        sql.append(DangerSgTableColumn.Guank4 + ",");
        sql.append(DangerSgTableColumn.Guank5 + ",");
        sql.append(DangerSgTableColumn.Keeper + ",");
        sql.append(DangerSgTableColumn.Keeperphone + ",");
        sql.append(DangerSgTableColumn.Sgcontact + ",");
        sql.append(DangerSgTableColumn.Sgdepartment + ",");
        sql.append(DangerSgTableColumn.Sgphone + ",");
        sql.append(DangerSgTableColumn.Ywcontact + ",");
        sql.append(DangerSgTableColumn.Ywphone + ",");
        sql.append(DangerSgTableColumn.PlineName + ",");
        sql.append(DangerSgTableColumn.ChnName + ",");
        sql.append(DangerSgTableColumn.ChnObjectId + ",");
        sql.append(DangerSgTableColumn.DangerRowId);

        sql.append(" FROM " + tableName);
        sql.append(" WHERE " + DangerSgTableColumn.DangerRowId + " = " + danger_rowid);

        Stmt stmt = this.spatialiteDataOpt.QueryExecute(sql.toString());

        try {
            while (stmt.step()) {

                dangerSgTableDataClass.setRowId(stmt.column_int(0));
                dangerSgTableDataClass.setLocation(stmt.column_string(1));
                dangerSgTableDataClass.setGuank1(stmt.column_string(2));
                dangerSgTableDataClass.setGuank2(stmt.column_string(3));
                dangerSgTableDataClass.setGuank3(stmt.column_string(4));
                dangerSgTableDataClass.setGuank4(stmt.column_string(5));
                dangerSgTableDataClass.setGuank5(stmt.column_string(6));
                dangerSgTableDataClass.setKeeper(stmt.column_string(7));
                dangerSgTableDataClass.setKeeperphone(stmt.column_string(8));
                dangerSgTableDataClass.setSgcontact(stmt.column_string(9));
                dangerSgTableDataClass.setSgdepartment(stmt.column_string(10));
                dangerSgTableDataClass.setSgphone(stmt.column_string(11));
                dangerSgTableDataClass.setYwcontact(stmt.column_string(12));
                dangerSgTableDataClass.setYwphone(stmt.column_string(13));
                dangerSgTableDataClass.setPlineName(stmt.column_string(14));
                dangerSgTableDataClass.setChnName(stmt.column_string(15));
                dangerSgTableDataClass.setChnObjectId(stmt.column_int(16));
                dangerSgTableDataClass.setDangerRowId(stmt.column_int(17));

            }
            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            return dangerSgTableDataClass;
        }
    }


    public int deleteFromDangerRowID(int dangerrowid){

        int result=-1;
        StringBuilder sql = new StringBuilder("");
        sql.append("DELETE FROM ");
        sql.append(tableName);
        sql.append(" WHERE " + DangerSgTableColumn.DangerRowId +"="+dangerrowid);

        result=this.spatialiteDataOpt.DeleteExecute(sql.toString());

        return result;
    }



    public int delete(List<String> plinenames){

        int result=-1;

        StringBuilder builder=new StringBuilder("");
        for(int i=0;i<plinenames.size();i++){
            if(i<plinenames.size()-1) {
                builder.append(DangerSgTableColumn.PlineName + " = '" + plinenames.get(i) + "'");
                builder.append(" or ");
            }else{
                builder.append(DangerSgTableColumn.PlineName + " = '" + plinenames.get(i) + "'");
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




    public int update(DangerSgTableDataClass dangerSgTableDataClass){

        int rowid = -1;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("update '" + tableName + "'");
            sql.append(" set ");
            sql.append(DangerSgTableColumn.Location + "='" + dangerSgTableDataClass.getLocation() + "',");
            sql.append(DangerSgTableColumn.Guank1 + "='" + dangerSgTableDataClass.getGuank1() + "',");
            sql.append(DangerSgTableColumn.Guank2 + "='" + dangerSgTableDataClass.getGuank2() + "',");
            sql.append(DangerSgTableColumn.Guank3 + "='" + dangerSgTableDataClass.getGuank3() + "',");
            sql.append(DangerSgTableColumn.Guank4 + "='" + dangerSgTableDataClass.getGuank4() + "',");
            sql.append(DangerSgTableColumn.Guank5 + "='" + dangerSgTableDataClass.getGuank5() + "',");
            sql.append(DangerSgTableColumn.Keeper + "='" + dangerSgTableDataClass.getKeeper() + "',");
            sql.append(DangerSgTableColumn.Keeperphone + "='" + dangerSgTableDataClass.getKeeperphone() + "',");
            sql.append(DangerSgTableColumn.Sgcontact + "='" + dangerSgTableDataClass.getSgcontact() + "',");
            sql.append(DangerSgTableColumn.Sgdepartment + "='" + dangerSgTableDataClass.getSgdepartment() + "',");
            sql.append(DangerSgTableColumn.Sgphone + "='" + dangerSgTableDataClass.getSgphone() + "',");
            sql.append(DangerSgTableColumn.Ywcontact + "='" + dangerSgTableDataClass.getYwcontact() + "',");
            sql.append(DangerSgTableColumn.Ywphone + "='" + dangerSgTableDataClass.getYwphone() + "',");
            sql.append(DangerSgTableColumn.PlineName + "='" + dangerSgTableDataClass.getPlineName() + "',");
            sql.append(DangerSgTableColumn.ChnName + "='" + dangerSgTableDataClass.getChnName() + "',");
            sql.append(DangerSgTableColumn.ChnObjectId + "=" + dangerSgTableDataClass.getChnObjectId() + ",");
            sql.append(DangerSgTableColumn.DangerRowId + "=" + dangerSgTableDataClass.getDangerRowId());

            sql.append(" where ROWID=" + dangerSgTableDataClass.getRowId());

            Long ll = this.spatialiteDataOpt.UpdateExecute(sql.toString());
            rowid = Integer.valueOf(String.valueOf(ll));
            return rowid;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return -1;
        }
    }


}
