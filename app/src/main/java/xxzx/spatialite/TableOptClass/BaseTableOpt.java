package xxzx.spatialite.TableOptClass;

import java.util.List;

import xxzx.publicClass.MySingleClass;
import xxzx.spatialite.SpatialiteDataOpt;
import xxzx.spatialite.TableStruct.PoleDangerTableColumn;

/**
 * Created by ch on 2016/4/2.
 */
public abstract class BaseTableOpt {

    protected String tableName;
    protected SpatialiteDataOpt spatialiteDataOpt;

    public BaseTableOpt(){
        this.spatialiteDataOpt= MySingleClass.getInstance().getSpatialiteDataOpt();
    }

    public abstract int insert(Object row);
    public abstract Object getRow(int rowid);
    public abstract List<Object> getRow();


    /**
     * 删除所有数据
     * @return
     */
    public int delete(){
        int result=-1;
        StringBuilder sql = new StringBuilder("");
        sql.append("DELETE FROM ");
        sql.append(tableName);

        result=this.spatialiteDataOpt.DeleteExecute(sql.toString());
        return result;
    }



    /**
     * 压缩数据表
     * @return
     */
    public int VACUUM() {
        int result = -1;
        try {
            StringBuilder sql = new StringBuilder("VACUUM ");
            result = this.spatialiteDataOpt.Execute(sql.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }



    /**
     * 提交事务
     * @return
     */
    public int COMMIT() {
        int result = -1;
        try {
            StringBuilder sql = new StringBuilder("COMMIT");
            result = this.spatialiteDataOpt.Execute(sql.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }



    /**
     * 创建事务
     * @return
     */
    public int BEGIN() {
        int result = -1;
        try {
            StringBuilder sql = new StringBuilder("BEGIN ");
            result = this.spatialiteDataOpt.Execute(sql.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }


    /**
     * 回滚事务
     * @return
     */
    public int ROLLBACK() {
        int result = -1;
        try {
            StringBuilder sql = new StringBuilder("ROLLBACK ");
            result = this.spatialiteDataOpt.Execute(sql.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }


    public int checkSpatialIndex() {
        int result = 0;
        try {
            String sql = "select CheckSpatialIndex('" + this.tableName + "','Geometry')";
            result = this.spatialiteDataOpt.Execute(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    public int createSpatialiteIndex(){
        String sql="select CreateSpatialIndex('"+this.tableName+"','Geometry')";
        int result=-1;
        try {
            result = this.spatialiteDataOpt.Execute(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }



}
