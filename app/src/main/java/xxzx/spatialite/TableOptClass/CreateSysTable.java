package xxzx.spatialite.TableOptClass;

import android.util.Log;

import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.ChannelDangerTableColumn;
import xxzx.spatialite.TableStruct.ChannelTableColumn;
import xxzx.spatialite.TableStruct.DangerSgTableColumn;
import xxzx.spatialite.TableStruct.PoleDangerTableColumn;
import xxzx.spatialite.TableStruct.PolePlaceTableColumn;
import xxzx.spatialite.TableStruct.PoleTableColumn;
import xxzx.spatialite.TableStruct.PowerlineTableColumn;
import xxzx.spatialite.SpatialiteDataOpt;

/**
 * Created by ch on 2016/4/2.
 */
public class CreateSysTable {

    private String TAG="CreateSysTable";

    public CreateSysTable() {
    }

    public int create() {
        this.createPowerlineTable();
        this.createChannelTable();
        this.createPoleTable();
        this.createChannelDangerTable();
        this.createPoleDangerTable();
        this.createDangerSgTable();
        this.createPolePlaceTable();

        //创建杆塔和通道表的索引
        //this.createPoleAndChnTableIndex();
        return 1;
    }


    //删除所有表
    public int deleteAll(){
        this.deleteTable(MyString.powerline_table_name);
        this.deleteTable(MyString.channel_table_name);
        this.deleteTable(MyString.channel_danger_table_name);
        this.deleteTable(MyString.pole_table_name);
        this.deleteTable(MyString.pole_danger_table_name);
        this.deleteTable(MyString.danger_sg_table_name);
        this.deleteTable(MyString.pole_place_table_name);
        return 1;
    }

    /**
     * 创建索引
     * @return
     */
    private int createPoleAndChnTableIndex() {
        int result = 0;
        String sql_pole = "SELECT CreateSpatialIndex('pole', 'Geometry')";
        String sql_chn = "SELECT CreateSpatialIndex('channel', 'Geometry')";

        SpatialiteDataOpt spatialiteDataOpt = MySingleClass.getInstance().getSpatialiteDataOpt();

        try {
            //String sql = "select CheckSpatialIndex('pole','Geometry')";
            spatialiteDataOpt.Execute(sql_pole);
            spatialiteDataOpt.Execute(sql_chn);
            result=1;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }



    private boolean createPowerlineTable() {
        // 创建表
        String sql1 = "CREATE TABLE "
                + MyString.powerline_table_name + " (PK_UID INTEGER NOT NULL PRIMARY KEY,"
                + PowerlineTableColumn.PowerName + " TEXT,"
                + PowerlineTableColumn.UserName + " TEXT,"
                + PowerlineTableColumn.DateTime + " DATETIME,"
                + PowerlineTableColumn.IsSelect + " INTEGER,"
                + PowerlineTableColumn.IsInMap + " INTEGER,"
                + PowerlineTableColumn.Geometry + " LINESTRING)";

        // 插入表信息
        String sql2 = "insert into 'geometry_columns' (f_table_name, f_geometry_column, geometry_type, coord_dimension, srid, spatial_index_enabled) VALUES('"
                + MyString.powerline_table_name + "','geometry',2,2," + MyString.gps_srid + ",0)";

        //检查点表是否存在，不存在则创建
        boolean value = this.CreateGeometryTable(MyString.powerline_table_name, sql1, sql2);

        return value;
    }


    private boolean createPoleTable() {
        // 创建表
        String sql1 = "CREATE TABLE "
                + MyString.pole_table_name + " (PK_UID INTEGER NOT NULL PRIMARY KEY,"
                + PoleTableColumn.PowerName + " TEXT,"
                + PoleTableColumn.PoleName + " TEXT,"
                + PoleTableColumn.PoleObjectId + " INTEGER,"
                + PoleTableColumn.PoleNumber + " INTEGER,"
                + PoleTableColumn.DateTime + " DATETIME,"
                + PoleTableColumn.IsSelect + " INTEGER,"
                + PoleTableColumn.DangerCount + " INTEGER,"
                + PoleTableColumn.Geometry + " POINT)";

        // 插入表信息
        String sql2 = "insert into 'geometry_columns' (f_table_name, f_geometry_column, geometry_type, coord_dimension, srid, spatial_index_enabled) VALUES('"
                + MyString.pole_table_name + "','geometry',1,2," + MyString.gps_srid + ",0)";

        //检查点表是否存在，不存在则创建
        boolean value = this.CreateGeometryTable(MyString.pole_table_name, sql1, sql2);

        return value;
    }

    private boolean createChannelTable() {

        // 创建表
        String sql1 = "CREATE TABLE "
                + MyString.channel_table_name + " (PK_UID INTEGER NOT NULL PRIMARY KEY,"
                + ChannelTableColumn.PowerName + " TEXT,"
                + ChannelTableColumn.ChannelName + " TEXT,"
                + ChannelTableColumn.ChannelObjectId + " INTEGER,"
                + ChannelTableColumn.ChannelNumber + " INTEGER,"
                + ChannelTableColumn.DateTime + " DATETIME,"
                + ChannelTableColumn.IsSelect + " INTEGER,"
                + ChannelTableColumn.DangerCount + " INTEGER,"
                + ChannelTableColumn.Geometry + " LINESTRING)";

        // 插入表信息
        String sql2 = "insert into 'geometry_columns' (f_table_name, f_geometry_column, geometry_type, coord_dimension, srid, spatial_index_enabled) VALUES('"
                + MyString.channel_table_name + "','geometry',2,2," + MyString.gps_srid + ",0)";

        //检查点表是否存在，不存在则创建
        boolean value = this.CreateGeometryTable(MyString.channel_table_name, sql1, sql2);

        return value;
    }

    private boolean createPoleDangerTable() {
        // 创建表
        String sql1 = "CREATE TABLE "
                + MyString.pole_danger_table_name + " (PK_UID INTEGER NOT NULL PRIMARY KEY,"
                + PoleDangerTableColumn.PowerName + " TEXT,"
                + PoleDangerTableColumn.PoleName + " TEXT,"
                + PoleDangerTableColumn.PoleObjectId + " INTEGER,"
                + PoleDangerTableColumn.HuiLuType + " INTEGER,"
                + PoleDangerTableColumn.DangerName + " TEXT,"
                + PoleDangerTableColumn.DangerLevel + " INTEGER,"
                + PoleDangerTableColumn.DangerMark + " TEXT,"
                + PoleDangerTableColumn.UserName + " TEXT,"
                + PoleDangerTableColumn.SpotMark + " TEXT,"
                + PoleDangerTableColumn.Version + " INTEGER,"
                + PoleDangerTableColumn.DateTime + " DATETIME,"
                + PoleDangerTableColumn.DangerType + " INTEGER,"
                + PoleDangerTableColumn.KeyID + " INTEGER,"
                + PoleDangerTableColumn.ImgNum + " INTEGER,"
                + PoleDangerTableColumn.PicsJson + " TEXT)";
        //检查点表是否存在，不存在则创建
        boolean value = this.CreateTable(MyString.pole_danger_table_name, sql1);

        return value;
    }

    private Boolean createChannelDangerTable() {

        // 创建表
        String sql1 = "CREATE TABLE "
                + MyString.channel_danger_table_name + " (PK_UID INTEGER NOT NULL PRIMARY KEY,"
                + ChannelDangerTableColumn.PowerName + " TEXT,"
                + ChannelDangerTableColumn.ChannelName + " TEXT,"
                + ChannelDangerTableColumn.ChannelObjectId + " INTEGER,"
                + ChannelDangerTableColumn.HuiLuType + " INTEGER,"
                + ChannelDangerTableColumn.DangerName + " TEXT,"
                + ChannelDangerTableColumn.DangerLevel + " INTEGER,"
                + ChannelDangerTableColumn.DangerMark + " TEXT,"
                + ChannelDangerTableColumn.UserName + " TEXT,"
                + ChannelDangerTableColumn.SpotMark + " TEXT,"
                + ChannelDangerTableColumn.Version + " INTEGER,"
                + ChannelDangerTableColumn.DateTime + " DATETIME,"
                + ChannelDangerTableColumn.DangerType + " INTEGER,"
                + ChannelDangerTableColumn.KeyID + " INTEGER,"
                + ChannelDangerTableColumn.PicsJson + " TEXT,"
                + ChannelDangerTableColumn.ImgNum + " INTEGER,"
                + ChannelDangerTableColumn.Geometry + " POLYGON)";


        // 插入表信息
        String sql2 = "insert into 'geometry_columns' (f_table_name, f_geometry_column, geometry_type, coord_dimension, srid, spatial_index_enabled) VALUES('"
                + MyString.channel_danger_table_name + "','geometry',3,2," + MyString.gps_srid + ",0)";

        //检查点表是否存在，不存在则创建
        boolean value = this.CreateGeometryTable(MyString.channel_danger_table_name, sql1, sql2);

        return value;
    }

    /**
     * 创建施工信息表
     * @return
     */
    private Boolean createDangerSgTable() {
        // 创建表
        String sql1 = "CREATE TABLE "
                + MyString.danger_sg_table_name + " (PK_UID INTEGER NOT NULL PRIMARY KEY,"
                + DangerSgTableColumn.DangerRowId + " INTEGER,"
                + DangerSgTableColumn.Location + " TEXT,"
                + DangerSgTableColumn.Guank1 + " TEXT,"
                + DangerSgTableColumn.Guank2 + " TEXT,"
                + DangerSgTableColumn.Guank3 + " TEXT,"
                + DangerSgTableColumn.Guank4 + " TEXT,"
                + DangerSgTableColumn.Guank5 + " TEXT,"
                + DangerSgTableColumn.Keeper + " TEXT,"
                + DangerSgTableColumn.Keeperphone + " TEXT,"
                + DangerSgTableColumn.Sgcontact + " TEXT,"
                + DangerSgTableColumn.Sgdepartment + " TEXT,"
                + DangerSgTableColumn.Sgphone + " TEXT,"
                + DangerSgTableColumn.Ywcontact + " TEXT,"
                + DangerSgTableColumn.PlineName + " TEXT,"
                + DangerSgTableColumn.ChnName + " TEXT,"
                + DangerSgTableColumn.ChnObjectId + " INTEGER,"
                + DangerSgTableColumn.Ywphone + " TEXT)";

        //检查点表是否存在，不存在则创建
        boolean value = this.CreateTable(MyString.danger_sg_table_name, sql1);

        return value;
    }


    /**
     * 创建签到表
     * @return
     */
    private Boolean createPolePlaceTable() {
        // 创建表
        String sql1 = "CREATE TABLE "
                + MyString.pole_place_table_name + " (PK_UID INTEGER NOT NULL PRIMARY KEY,"
                + PolePlaceTableColumn.PoleObjectId + " INTEGER,"
                + PolePlaceTableColumn.UserName + " TEXT,"
                + PolePlaceTableColumn.DateTime + " DATETIME,"
                + PolePlaceTableColumn.PoleName + " TEXT,"
                + PolePlaceTableColumn.PowerName + " TEXT)";

        //检查点表是否存在，不存在则创建
        boolean value = this.CreateTable(MyString.pole_place_table_name, sql1);

        return value;
    }




    /**
     * 创建图形表
     * @param tableName 表名称
     * @param sql1 创建表的sql
     * @param sql2 创建集合表sql
     * @return
     */
    private boolean CreateGeometryTable(String tableName,String sql1,String sql2){
        try {
            SpatialiteDataOpt spatialiteDataOpt= MySingleClass.getInstance().getSpatialiteDataOpt();
            boolean isexsit = spatialiteDataOpt.IsTableExsit(tableName);
            if (!isexsit) {
                spatialiteDataOpt.CreateTable(sql1);
                spatialiteDataOpt.InsertExecute(sql2);
            }
            return true;
        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    /**
     * 创建普通表
     * @param tableName
     * @param sql1
     * @return
     */
    private boolean CreateTable(String tableName,String sql1){
        try {
            SpatialiteDataOpt spatialiteDataOpt=MySingleClass.getInstance().getSpatialiteDataOpt();
            boolean isexsit = spatialiteDataOpt.IsTableExsit(tableName);
            if (!isexsit) {
                spatialiteDataOpt.CreateTable(sql1);
            }
            return true;
        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            return false;
        }
    }



    private int deleteTable(String tablename){
        try {

            String sql="DROP TABLE "+tablename;
            String sql2="DELETE FROM geometry_columns WHERE f_table_name='" + tablename+"'";
            SpatialiteDataOpt spatialiteDataOpt = MySingleClass.getInstance().getSpatialiteDataOpt();

            //如果不存在表
            if(!spatialiteDataOpt.IsTableExsit(tablename)){
                return 1;
            }

            int result1 = spatialiteDataOpt.DeleteExecute(sql);
            if(result1==1) {
                int result2 = spatialiteDataOpt.DeleteExecute(sql2);
                return result2;
            }else{
                return -1;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }





}
