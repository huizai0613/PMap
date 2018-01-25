package xxzx.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

import xxzx.publicClass.ToastUtil;


public class ImageViewActivity extends AppCompatActivity {

    private static final String LOG_TAG = ImageViewActivity.class.getSimpleName();
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Intent intent = getIntent();
        String imgPath = intent.getStringExtra("imgpath");

        this.initView();
        this.initData(imgPath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_my_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        imageview = (ImageView)findViewById(R.id.imageview);
    }

    private void initData(String imgPath ) {
        Bitmap bitmap=getDiskBitmap(imgPath);
        if(bitmap!=null) {
            imageview.setImageBitmap(bitmap);
        }else{
            ToastUtil.show(this,"读取图片失败");
        }
    }


    private Bitmap getDiskBitmap(String pathString)
    {
        Bitmap bitmap = null;
        try
        {
            File file = new File(pathString);
            if(file.exists())
            {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }

        return bitmap;
    }
}




