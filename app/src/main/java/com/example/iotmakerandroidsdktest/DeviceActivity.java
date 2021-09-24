package com.example.iotmakerandroidsdktest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.DeviceNewApiNew;
import com.kt.gigaiot_sdk.TagStrmApiNew;
import com.kt.gigaiot_sdk.data.DeviceApiResponseNew;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.TagStrm;
import com.kt.gigaiot_sdk.data.TagStrmApiResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;

import java.util.ArrayList;

public class DeviceActivity extends AppCompatActivity{
    String TAG = "DeviceActivity";
    public static final String EXTRA_DEVICE = "device";

    private ListView mListView;
    private DeviceNew mDevice;
    private ArrayList<TagStrm> mTagStrms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        String strDevice = getIntent().getStringExtra(EXTRA_DEVICE);

        Gson gson = new Gson();
        mDevice = gson.fromJson(strDevice, DeviceNew.class);
        TextView tw_device = findViewById(R.id.tv_device_info);
        tw_device.setText(
                "디바이스 생성일시: " + mDevice.getCreatedOn() + "\n" +
                        "디바이스 모델 명: " + mDevice.getModel().getName() + "\n" +
                        "디바이스 모델 일련번호: " + mDevice.getModel().getSequence() + "\n" +
                        "디바이스 명: " + mDevice.getName() + "\n" +
                        "디바이스 아이디: " + mDevice.getId() + "\n" +
                        "게이트웨이 연결 아이디: " + mDevice.getConnectionId() + "\n" +
                        "서비스 대상 일련번호: " + mDevice.getTarget().getSequence() + "\n"
                //+ strDevice
        );

        new GetTagStrmListTask().execute();
    }

    public class GetTagStrmListTask {
        APICallback apiCallback = new APICallback<TagStrmApiResponse>() {
            @Override
            public void onStart() { }

            @Override
            public void onDoing(TagStrmApiResponse response) {
                mTagStrms = response.getTagStrms();

                ArrayList<String> tagStrm_names = new ArrayList<String>();
                for (TagStrm tag : mTagStrms) {
                    String type = "UnKnown";
                    if (tag.getTagStrmPrpsTypeCd().equals("0000010"))
                        type = "수집";
                    else if (tag.getTagStrmPrpsTypeCd().equals("0000020"))
                        type = "제어";
                    tagStrm_names.add("[" + type + "] " + tag.getTagStrmId());
                }

                // ArrayAdpater 객체 생성 및 설정
                ArrayAdapter dAdapter = new ArrayAdapter(DeviceActivity.this,
                        android.R.layout.simple_list_item_1, tagStrm_names.toArray());

                // 리스트뷰에 어뎁터 객체 연결
                mListView.setAdapter(dAdapter);
                mListView = findViewById(R.id.lv_tagstrm_list);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        if (mTagStrms != null) {

                            TagStrm tagStrm = mTagStrms.get(position);
                            Gson gson = new Gson();
                            String strTag = gson.toJson(tagStrm);
                            String strDevice = gson.toJson(mDevice);

                            if (tagStrm.getTagStrmPrpsTypeCd().equals(TagStrmApiNew.TAGSTRM_DATA)) {
                                Intent intent = new Intent(DeviceActivity.this, TagStrmLogActivity.class);
                                intent.putExtra(DeviceActivity.EXTRA_DEVICE, strDevice);
                                intent.putExtra(TagStrmLogActivity.EXTRA_TAGSTRM, strTag);
                                startActivity(intent);
                            } else if (tagStrm.getTagStrmPrpsTypeCd().equals(TagStrmApiNew.TAGSTRM_CTRL)) {
                                createSendCtrlMsgDialog(position);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFail() { }
        };

        public void execute() {
            TagStrmApiNew tagStrmApi = new TagStrmApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    apiCallback
            );
            //개인당 최대 5개의 디바이스만 등록 가능하므로, offset=1만 설정해줘도 됨
            tagStrmApi.getTagStrmList(mDevice.getId());
        }
    }

    private void createSendCtrlMsgDialog(final int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dilogView = inflater.inflate(R.layout.dialog_device_ctrl, null);
        final EditText etCtrlMsg = (EditText) dilogView.findViewById(R.id.et_device_ctrl);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("제어요청 보내기")
                .setView(dilogView)
                .setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String ctrlMsg = etCtrlMsg.getText().toString();
                        new SendCtrlMsgTask().execute(position,ctrlMsg);
                    }
                })
                .setNegativeButton("취소", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class SendCtrlMsgTask {
        APICallback apiCallback = new APICallback<TagStrmApiResponse>() {
            @Override
            public void onStart() { }

            @Override
            public void onDoing(TagStrmApiResponse response) {
                if (response.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    Toast.makeText(DeviceActivity.this, "제어 요청이 성공하였습니다.", Toast.LENGTH_SHORT).show();


                } else if (response.getResponseCode().equals(ApiConstants.CODE_NG)) {
                    Toast.makeText(DeviceActivity.this, "제어 요청이 실패하였습니다.\n[" + response.getMessage() + "]", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFail() { }
        };

        public void execute(int position, String ctrlMsg) {
            TagStrmApiNew tagStrmApi = new TagStrmApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    apiCallback);
            tagStrmApi.sendCtrlMsg(
                    mDevice.getTarget().getSequence(),
                    mDevice.getSequence(),
                    mDevice.getId(),
                    mDevice.getConnectionId(),
                    mTagStrms.get(position).getTagStrmId(),
                    mTagStrms.get(position).getTagStrmValTypeCd(),
                    ApplicationPreference.getInstance().getPrefAccountId(), ctrlMsg);
        }
    }
}