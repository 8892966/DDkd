package com.example.user.ddkd;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.example.user.ddkd.Presenter.ZhuCePresenterImpl;
import com.example.user.ddkd.View.IZhuCeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZhuCeActivity extends BaseActivity implements View.OnClickListener,IZhuCeView {
    private ZhuCe1Fragment zhuCe1Fragment;
    private ZhuCe2Fragment zhuCe2Fragment;
    private ZhuCe3Fragment zhuCe3Fragment;
    private ZhuCe4Fragment zhuCe4Fragment;
    private TextView tv_next;
    private TextView tv_head_fanghui;
    private int count=0;
    private ZhuCePresenterImpl zhuCePresenter;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private AlertDialog show;
    private boolean isUpload;
    private int Upload=0;
    private List<String> names=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zhuCePresenter=ZhuCePresenterImpl.getInstance(this);
        setContentView(R.layout.zhuceactivity);
        count=1;
        names.add("IdCard");
        names.add("IdCardBack");
        names.add("StudentCard");
        tv_next = (TextView) findViewById(R.id.tv_next);//下一步按钮
        tv_head_fanghui = (TextView)findViewById(R.id.tv_head_fanghui);//返回
        tv_next.setOnClickListener(this);
        tv_head_fanghui.setOnClickListener(this);
        showFragment(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                switch (count){
                    case 1:
                        if(zhuCe1Fragment.next()){
                            showFragment(2);
                            count++;
                        }
                        break;
                    case 2:
                        if(zhuCe2Fragment.next()){
                            showFragment(3);
                            count++;
                        }
                        break;
                    case 3:
                        if(zhuCe3Fragment.next()){
                            zhuCePresenter.SubmitPictures(3, "touxiang", zhuCePresenter.getmap("phone"), new File(zhuCe3Fragment.fileName));
                            showFragment(4);
                            count++;
                        }
                        break;
                    case 4:
                        isUpload=true;
                        Submit();
                        break;
                }
                break;
            case R.id.tv_head_fanghui:
                switch (count){
                    case 1:
                        finish();
                        break;
                    case 2:
                        showFragment(1);
                        count--;
                        break;
                    case 3:
                        showFragment(2);
                        count--;
                        break;
                    case 4:
                        showFragment(3);
                        count--;
                        break;
                }
                break;
        }
    }

    public void showFragment(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 1:
                if (zhuCe1Fragment == null) {
                    zhuCe1Fragment = new ZhuCe1Fragment();
                    transaction.add(R.id.FrameLayout,zhuCe1Fragment);
                } else {
                    transaction.show(zhuCe1Fragment);
                }
                break;
            case 2:
                if (zhuCe2Fragment == null) {
                    zhuCe2Fragment = new ZhuCe2Fragment();
                    zhuCe2Fragment.setArguments(zhuCe1Fragment.getArguments());
                    transaction.add(R.id.FrameLayout, zhuCe2Fragment);
                } else {
                    transaction.show(zhuCe2Fragment);
                }
                break;
            case 3:
                if (zhuCe3Fragment == null) {
                    zhuCe3Fragment = new ZhuCe3Fragment();
                    zhuCe3Fragment.setArguments(zhuCe2Fragment.getArguments());
                    transaction.add(R.id.FrameLayout, zhuCe3Fragment);
                } else {
                    transaction.show(zhuCe3Fragment);
                }
                break;
            case 4:
                if (zhuCe4Fragment == null) {
                    zhuCe4Fragment = new ZhuCe4Fragment();
                    transaction.add(R.id.FrameLayout,zhuCe4Fragment);
                } else {
                    transaction.show(zhuCe4Fragment);
                }
                break;
        }
        transaction.commit();
    }

    public void hideFragment(FragmentTransaction transaction) {
        if (zhuCe1Fragment != null) {
            transaction.hide(zhuCe1Fragment);
        }
        if (zhuCe2Fragment != null) {
            transaction.hide(zhuCe2Fragment);
        }
        if (zhuCe3Fragment != null) {
            transaction.hide(zhuCe3Fragment);
        }
        if (zhuCe4Fragment != null) {
            transaction.hide(zhuCe4Fragment);
        }
    }

    /**
     *  * Try to return the absolute file path from the given Uri
     *  *
     *  * @param context
     *  * @param uri
     *  * @return the file path or null
     *  
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public void PhoExisting(){
        zhuCe1Fragment.yanzhengsettext("检查中...");
    }

    @Override
    public void PhoExist() {
        zhuCe1Fragment.yanzhengsettext("验证码");
    }
    @Override
    public void PhoisExist(){
        zhuCe1Fragment.yanzhengsetEnabled(false);
    }
    @Override
    public void PhoisNotExist() {
        zhuCe1Fragment.yanzhengsetEnabled(false);
    }

    @Override
    public void showProgressDialog(int max) {
        if(show==null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.zhuce_dialog_progress, null);
            progressBar1 = (ProgressBar) view.findViewById(R.id.pb_sum);
            progressBar1.setMax(max);
            progressBar2 = (ProgressBar) view.findViewById(R.id.pb_each);
            builder.setTitle("上传信息中...");
            builder.setView(view);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isUpload = false;
                    show.dismiss();
                }
            });
            show = builder.create();
            show.setCanceledOnTouchOutside(false);
            show.show();
        }
    }

    @Override
    public void closeProgressDialog() {
        if (show != null) {
            isUpload=false;
            show.dismiss();
        }
    }

    @Override
    public void onLoading(long total, long current) {
        if (progressBar2 != null) {
            int t;
            int c;
            if (total > 1000) {
                t = (int) (total / 1000);
            } else {
                t = (int) total;
            }
            if (current > 1000) {
                c = (int) (current / 1000);
            } else {
                c = (int) current;
            }
            progressBar2.setMax(t);
            progressBar2.setProgress(c);
        }
    }

    @Override
    public void Submit() {
        if(isUpload) {
            File file = null;
            switch (Upload) {
                case 0:
                    file = new File(getRealFilePath(this, zhuCe4Fragment.uri1));
                    break;
                case 1:
                    file = new File(getRealFilePath(this, zhuCe4Fragment.uri2));
                    break;
                case 2:
                    file = new File(getRealFilePath(this, zhuCe4Fragment.uri3));
                    break;
                case 3:
                    zhuCePresenter.SubmitData();
                    Upload = 0;
                    isUpload = false;
                    return;
            }
            if (file != null) {
                zhuCePresenter.SubmitPictures(4, names.get(Upload), zhuCePresenter.getmap("phone"), file);
            }
            Upload++;
        }else {
            Upload = 0;
        }
    }

    @Override
    public void UploadSUCCESS() {
        Toast.makeText(getApplication(), "提交成功，请等待审核通过", Toast.LENGTH_SHORT).show();
        closeProgressDialog();
        finish();
    }

    @Override
    public void showToast(String content) {
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }

    public void Agreed() {
        tv_next.setEnabled(true);
    }

    public void Against() {
        tv_next.setEnabled(false);
    }

}
