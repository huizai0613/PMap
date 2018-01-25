package xxzx.myBdLocation.AroudFriends;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xxzx.activity.R;
import xxzx.login.HttpRequestResult;
import xxzx.login.LoginFileUtils;
import xxzx.login.PswModifyTask;
import xxzx.login.User;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.ToastUtil;

/**
 * Created by Lenovo on 2017/6/13.
 */

public class NbUserDlgFragment extends DialogFragment {

    private TextView tv_name;
    private TextView tv_telephone;

    private static NbUserDlgFragment frag;


    public static NbUserDlgFragment newInstance(String name, String telephone) {
        if (frag == null) {
            frag = new NbUserDlgFragment();
        }

        Bundle b = new Bundle();
        b.putString("name", name);
        b.putString("telephone", telephone);
        frag.setArguments(b);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_nearby_user, null);

        String name = getArguments().getString("name");
        String telephone = getArguments().getString("telephone");

        this.tv_name = (TextView) view.findViewById(R.id.tv_name);
        this.tv_telephone = (TextView) view.findViewById(R.id.tv_telephone);
        this.tv_name.setText(name);
        this.tv_telephone.setText(telephone);

        Button btn_call =  (Button) view.findViewById(R.id.btn_call);

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(tv_telephone.getText().toString().trim())){
                    ToastUtil.show(NbUserDlgFragment.this.getActivity(),"电话号码为空,请检查号码");
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tv_telephone.getText().toString().trim());
                intent.setData(data);
                startActivity(intent);

            }
        });
        return view;
    }


}
