package xxzx.spatialite;

/**
 * Created by mulin on 2015/11/30.
 */

import java.io.File;


import android.content.Context;
import android.widget.Toast;

import jsqlite.Database;
import jsqlite.Stmt;

public class SpatialiteDataOpt {

    private jsqlite.Database SpatialiteDB = null;
    private Context mContext = null;

    /**
     * 构造函数
     *
     * @param path
     */
    public SpatialiteDataOpt(Context mContext, String path) {
        try {
            if (path != null) {
                this.mContext = mContext;
                File spatialDbFile = new File(path);
                SpatialiteDB = new jsqlite.Database();
                SpatialiteDB.open(spatialDbFile.getAbsolutePath(), jsqlite.Constants.SQLITE_OPEN_READWRITE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造函数
     *
     * @param db
     */
    public SpatialiteDataOpt(Context mContext, jsqlite.Database db) {
        try {
            this.SpatialiteDB = db;
            this.mContext = mContext;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public jsqlite.Database getSpatialiteDB() {
        return this.SpatialiteDB;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        if (this.SpatialiteDB != null) {
            try {
                this.SpatialiteDB.close();
            } catch (jsqlite.Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断表是否存在
     *
     */
    @SuppressWarnings("finally")
    public boolean IsTableExsit(String TableName) {
        int count = -1;
        try {

            Stmt stmt = null;

            String sql = "SELECT COUNT(*) FROM sqlite_master where type='table' and name='" + TableName + "'";

            stmt = SpatialiteDB.prepare(sql);

            while (stmt.step()) {
                count = stmt.column_int(0);
            }

            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Toast.makeText(mContext, "执行sql失败！" + e.toString(), Toast.LENGTH_LONG).show();

        } finally {
            if (count == 0)
                return false;
            else
                return true;
        }
    }


    /**
     * 执行sql语句
     * @param
     * @return
     */
    @SuppressWarnings("finally")
    public int Execute(String sql) {
        int result = -1;
        try {
            SpatialiteDB.exec(sql,null);
            result = 1;
        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(mContext, "执行sql失败！sql="+sql +",错误："+ e.toString(), Toast.LENGTH_LONG).show();
            result = -1;
        } finally {
            return result;
        }
    }



    /**
     * 创建表
     *
     */

    public void CreateTable(String sql) {
        try {
            Stmt stmt = null;
            SpatialiteDB.exec(sql,null);
//            stmt.step();
//            stmt.close();

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Toast.makeText(mContext, "创建表失败！" + e.toString(), Toast.LENGTH_LONG).show();

        }
    }




    /**
     * 执行插入sql语句
     *
     */
    @SuppressWarnings("finally")
    public Long InsertExecute(String sql) {

        long rowid = -1;
        try {
            Stmt stmt = null;
            stmt = SpatialiteDB.prepare(sql);
            stmt.step();
            rowid = SpatialiteDB.last_insert_rowid();

            stmt.close();
            if (rowid == -1) {
                Toast.makeText(mContext, "执行插入数据失败！", Toast.LENGTH_LONG).show();
            }

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(mContext, "执行插入数据失败！" + e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            return rowid;
        }
    }

    /**
     * 执行插入sql语句
     *
     * @param sql
     */
    public long UpdateExecute(String sql) {

        long rowid = -1;

        try {

            Stmt stmt = null;
            stmt = SpatialiteDB.prepare(sql);
            stmt.step();
            rowid = SpatialiteDB.last_insert_rowid();
            stmt.close();

            if (rowid == -1) {
                Toast.makeText(mContext, "执行更新数据失败！", Toast.LENGTH_LONG).show();
            }

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Toast.makeText(mContext, "执行更新数据失败！" + e.toString(), Toast.LENGTH_LONG).show();

        } finally {

            return rowid;
        }
    }

    /**
     * 执行删除sql语句
     *
     * @param sql
     */
    public int DeleteExecute(String sql) {
        int result=-1;
        try {
            Stmt stmt = null;
            stmt = SpatialiteDB.prepare(sql);
            stmt.step();
            stmt.close();

            result=1;

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(mContext, "执行删除数据失败！" + "sql语句：" + sql + "错误信息："+e.toString(), Toast.LENGTH_LONG).show();
            result=-1;
        }finally {
            return result;
        }
    }

    /**
     * 执行sql语句
     *
     * @param sql
     */
    @SuppressWarnings("finally")
    public Stmt QueryExecute(String sql) {

        Stmt stmt = null;

        try {
            stmt = SpatialiteDB.prepare(sql);

        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Toast.makeText(mContext, "执行查询语句失败！" + e.toString(), Toast.LENGTH_LONG).show();

        } finally {
            return stmt;
        }
    }

}
