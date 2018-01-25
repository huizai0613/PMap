package xxzx.myView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;


/**
 * Created by mulin on 2015/11/12.
 */
public class DialogEditLocalData extends Dialog  {

    private Context mContext;

    private ImgButtonHorizontal imgbtn_photo = null;
    private ImgButtonHorizontal imgbtn_map = null;
    private ImgButtonHorizontal imgbtn_delete = null;
    // ----------------定义点击事件-----------------------------
    /**
     * 一定一个接口
     */
    public interface ICoallBack {
        public void onClick(View view);
    }

    /**
     * 初始化接口变量
     */
    ICoallBack icallBack = null;

    /**
     * 自定义控件的自定义事件
     *
     * @param iBack
     *            接口类型
     **/
    public void setOnClick(ICoallBack iBack) {
        icallBack = iBack;
    }

    public DialogEditLocalData(Context context) {
        super(context, R.style.MyAlertDialogStyle);
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_local_data);
        InitView();
        InitData();
    }

    private void InitView() {

        imgbtn_photo = (ImgButtonHorizontal) findViewById(R.id.imgbtn_photo);
        imgbtn_map = (ImgButtonHorizontal) findViewById(R.id.imgbtn_map);
        imgbtn_delete = (ImgButtonHorizontal) findViewById(R.id.imgbtn_delete);

        imgbtn_photo.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_edit_local_picture));
        imgbtn_map.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_edit_local_location));
        imgbtn_delete.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_edit_local_delete));

        imgbtn_photo.setOnClick(new BtnOnClickListener());
        imgbtn_map.setOnClick(new BtnOnClickListener());
        imgbtn_delete.setOnClick(new BtnOnClickListener());
    }

    private void InitData() {

    }


    /**
     * 按钮点击监听器
     */
    private class BtnOnClickListener implements ImgButtonHorizontal.ICoallBack {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()){
                case R.id.imgbtn_photo:
                    if(icallBack!=null){
                        icallBack.onClick(v);
                    }
                    break;
                case R.id.imgbtn_map:
                    if(icallBack!=null){
                        icallBack.onClick(v);
                    }
                    break;
                case R.id.imgbtn_delete:
                    if(icallBack!=null){
                        icallBack.onClick(v);
                    }
                    break;
            }
        }
    }



    private void photoBrower(){

    }

    private void mapLocation(){

    }

    private void delete(){

    }






}