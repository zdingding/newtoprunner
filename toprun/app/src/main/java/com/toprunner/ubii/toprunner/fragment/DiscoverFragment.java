package com.toprunner.ubii.toprunner.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.activivty.ClipActivity;
import com.toprunner.ubii.toprunner.activivty.ScanActivity;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.utils.CacheUtils;
import com.toprunner.ubii.toprunner.utils.SpUtils;
import com.toprunner.ubii.toprunner.utils.UIUtils;
import com.toprunner.ubii.toprunner.view.CircleImageView;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static com.toprunner.ubii.toprunner.R.layout.fragment_discover;


public class DiscoverFragment extends BaseFragment implements View.OnClickListener {
    private PopupWindow popWindow;
    private CircleImageView headimage;
    private LayoutInflater layoutInflater;
    private TextView photograph,albums;
    private LinearLayout cancel;
    public static final int PHOTOZOOM = 0; // 相册/拍照
    public static final int PHOTOTAKE = 1; // 相册/拍照
    public static final int IMAGE_COMPLETE = 2; // 结果
    public static final int CROPREQCODE = 3; // 截取
    private String photoSavePath;//保存路径
    private String photoSaveName;//图pian名
    private String path;//图片全路径
    private TextView nickname;
    private TextView sex_text;
    private TextView sextext;
    private String sex;
    private TextView mHeight;
    private NumberPicker numberPicker;
    private float height;//身高
    private float weight;//体重
    private int age;//年龄
    private TextView tv_mHeight;
    private TextView tv_weight;
    private TextView num_weight;
    private TextView tv_age;
    private TextView text_age;
    private TextView test_scan_qrcode;


    @Override
    protected void initData() {
          sex = CacheUtils.getString(getActivity(), "sex");
          sex = CacheUtils.getString(getActivity(), "sex");
        if(null!=sex){
            sextext.setText(sex);
        }
        headimage.setOnClickListener(this);
        nickname.setOnClickListener(this);
        sex_text.setOnClickListener(this);
        tv_mHeight.setOnClickListener(this);
        tv_weight.setOnClickListener(this);
        tv_age.setOnClickListener(this);
        test_scan_qrcode.setOnClickListener(this);

    }
    @Override
    public void setListener() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        headimage = findViewById(R.id.headimage);
        nickname = findViewById(R.id.nickname);
        sex_text = findViewById(R.id.sex_text);
        sextext = findViewById(R.id.sextext);
        mHeight = findViewById(R.id.mHeight);
        tv_mHeight = findViewById(R.id.tv_mHeight);
        tv_weight = findViewById(R.id.tv_weight);
        num_weight = findViewById(R.id.num_weight);
        tv_age = findViewById(R.id.tv_age);
        text_age = findViewById(R.id.text_age);
        test_scan_qrcode = findViewById(R.id.test_scan_qrcode);

    }

    @Override
    public int getLayoutId() {
        return fragment_discover;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.test_scan_qrcode:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
            case  R.id.tv_age:
                // 设置年龄
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(150);
                numberPicker.setValue(Integer.parseInt(text_age.getText().toString()
                        .trim()));
                numberPicker.setMinValue(1);
                new AlertDialog.Builder(getActivity())
                        .setView(numberPicker)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        text_age.setText(numberPicker.getValue() + "");
                                        age = numberPicker.getValue();
                                        savePersonalData();
                                    }
                                }).show();


                break;
            case  R.id.tv_weight:
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(200);
                numberPicker.setValue((int) Float.parseFloat(num_weight.getText()
                        .toString().trim()));
                numberPicker.setMinValue(20);
                new AlertDialog.Builder(getActivity())
                        .setView(numberPicker)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        num_weight.setText(numberPicker.getValue() + "");
                                        weight = numberPicker.getValue();
                                        savePersonalData();
                                    }
                                }).show();

                break;
            case  R.id.tv_mHeight:
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(200);
                numberPicker.setValue((int) Float.parseFloat(mHeight.getText()
                        .toString().trim()));
                numberPicker.setMinValue(20);
                new AlertDialog
                        .Builder(getActivity())
                        .setView(numberPicker)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        mHeight.setText(numberPicker.getValue() + "");
                                        height = numberPicker.getValue();
                                        savePersonalData();
                                    }
                                }).show();


                break;
            case  R.id.sex_text:
                // 设置性别
                final String[] sexlist = { "男", "女" };
                new AlertDialog.Builder(getActivity()).setItems(sexlist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sextext.setText(sexlist[which]);
                        sex = sexlist[which];
                        savePersonalData();
                    }
                }).show();
            break;
            case  R.id.headimage:
                showPopupWindow(headimage);
            break;
            case  R.id.nickname:
                final EditText editText = new EditText(getActivity());
                editText.setHint(nickname.getText().toString());
                new AlertDialog.Builder(getActivity())
                        .setTitle("修改昵称")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //得到新名称
                                String newName = editText.getText().toString();
                                //显示输入的名称
                                nickname.setText(newName);
                                //保存新名称(sp中)
                                //sp.edit().putString("lost_name", newName).commit();
                                SpUtils.getInstance(UIUtils.getContext()).save(SpUtils.LOST_NAME, newName);
                            }})
                        .setNegativeButton("取消", null)
                        .show();
            break;

        }
    }

    private void savePersonalData() {
        CacheUtils.putString(getActivity(),"sex",sex);//保存性别
    }

    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            View view = UIUtils.inflate(R.layout.popupwindow);
            popWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            initPop(view);
        }
        popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

    }
    public void initPop(View view){
        photograph = (TextView) view.findViewById(R.id.photograph);//拍照
        albums = (TextView) view.findViewById(R.id.albums);//相册
        cancel= (LinearLayout) view.findViewById(R.id.cancel);//取消
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();
                photoSaveName =String.valueOf(System.currentTimeMillis()) + ".png";
                Uri imageUri = null;
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = Uri.fromFile(new File(photoSavePath,photoSaveName));
                openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, PHOTOTAKE);
            }
        });
        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openAlbumIntent, PHOTOZOOM);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();

            }
        });
    }
        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != RESULT_OK) {
                return;
            }
            Uri uri = null;
            switch (requestCode) {
                case PHOTOZOOM://相册
                    if (data==null) {
                        return;
                    }
                    uri = data.getData();
                    String[] proj = { MediaStore.Images.Media.DATA };
                    Cursor cursor = UIUtils.getContext().getContentResolver().query(uri, proj, null, null,null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);// 图片在的路径
                    Intent intent3=new Intent(UIUtils.getContext(), ClipActivity.class);
                    intent3.putExtra("path", path);
                    startActivityForResult(intent3, IMAGE_COMPLETE);
                    break;
                case PHOTOTAKE://拍照
                    path=photoSavePath+photoSaveName;
                    uri = Uri.fromFile(new File(path));
                    Intent intent2=new Intent(UIUtils.getContext(), ClipActivity.class);
                    intent2.putExtra("path", path);
                    startActivityForResult(intent2, IMAGE_COMPLETE);
                    break;
                case IMAGE_COMPLETE:
                    final String temppath = data.getStringExtra("path");
                    headimage.setImageBitmap(UIUtils.getLoacalBitmap(temppath));
                    break;
                default:
                    break;
            }

    }


}
