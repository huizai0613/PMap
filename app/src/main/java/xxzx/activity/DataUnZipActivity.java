package xxzx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by yexiangyu on 2018/1/23.
 */

public class DataUnZipActivity extends AppCompatActivity
{

    private TextView progressTV;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataunzip_layout);
        progressTV = (TextView) findViewById(R.id.tv_un_status);

        final File mapFile = new File(Environment.getExternalStorageDirectory(), "amap");
        if (!(mapFile.exists() && getTotalSizeOfFilesInDir(mapFile) > 80 * 1024 * 1024)) {
            mapFile.mkdir();
            new AsyncTask<String, Integer, Boolean>()
            {

                @Override
                protected Boolean doInBackground(String... strings)
                {

                    try {
                        InputStream open = getAssets().open("data.zip");
                        String Parent = mapFile.getAbsolutePath();
                        File pathFile = new File(Parent);
                        if (!pathFile.exists()) {
                            pathFile.mkdirs();
                        }
                        ZipInputStream zip = new ZipInputStream(open);
                        ZipEntry nextEntry = null;
                        int index = 0;
                        while ((nextEntry = zip.getNextEntry()) != null) {
                            ZipEntry entry = nextEntry;
                            String zipEntryName = entry.getName();
//            InputStream in = new ZipFile().getInputStream();
                            String outPath = (Parent + "/" + zipEntryName).replaceAll("\\*", "/");
                            ;
                            //判断路径是否存在,不存在则创建文件路径
                            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                            if (new File(outPath).isDirectory()) {
                                continue;
                            }
                            //输出文件路径信息
                            System.out.println(outPath);

                            OutputStream out = new FileOutputStream(outPath);
                            byte[] buf1 = new byte[1024];
                            int len;
                            while ((len = zip.read(buf1)) > 0) {
                                out.write(buf1, 0, len);
                            }
                            publishProgress(index);
                            index++;
                            out.close();
                        }
                        zip.close();

                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;

                }

                @Override
                protected void onProgressUpdate(Integer... values)
                {
                    super.onProgressUpdate(values);

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("正在准备数据中");
                    for (int i = 0; i < values[0] % 4; i++) {
                        stringBuilder.append(".");
                    }
                    progressTV.setText(stringBuilder.toString());
                }

                @Override
                protected void onPostExecute(Boolean s)
                {

                    if (s) {
                        //解压成功跳转
                        startActivity(new Intent(DataUnZipActivity.this, MainDrawerActivity.class));
                    } else {
                        //解压失败关闭
                        finish();
                    }

                    super.onPostExecute(s);

                }
            }.execute();
        } else {
            startActivity(new Intent(DataUnZipActivity.this, MainDrawerActivity.class));
        }


    }

    private long getTotalSizeOfFilesInDir(final File file)
    {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }


}
