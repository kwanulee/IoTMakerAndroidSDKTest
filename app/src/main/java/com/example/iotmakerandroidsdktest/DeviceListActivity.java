package com.example.iotmakerandroidsdktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.DeviceNewApiNew;
import com.kt.gigaiot_sdk.data.DeviceApiResponseNew;
import com.kt.gigaiot_sdk.data.DeviceNew;

import java.util.ArrayList;

public class DeviceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView mListView;
    ArrayList<DeviceNew> mDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        mListView = findViewById(R.id.lv_device_list);
        mListView.setOnItemClickListener(this);

        new GetDevListTask().execute();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mDevices != null) {

            Gson gson = new Gson();
            String strDevice = gson.toJson(mDevices.get(i)); // 리스트뷰에서 선택된 디바이스 객체 정보를 JSON 문자열로 변환

            Intent intent = new Intent(DeviceListActivity.this, DeviceActivity.class);

            intent.putExtra(DeviceActivity.EXTRA_DEVICE, strDevice);    // JSON 문자열로 변환된 디바이스 객체 정보를  DeviceActivity에 Extras를 통해 전달
            startActivity(intent);
        }
    }

    public class GetDevListTask {
        protected void execute() {

            APICallback apiCallback = new APICallback<DeviceApiResponseNew>() {
                @Override
                public void onStart() {}

                @Override
                public void onDoing(DeviceApiResponseNew response) {
                    if (response.getDevices() != null && response.getDevices().size()>0){
                        mDevices = response.getDevices();

                        ArrayList<String> device_names = new ArrayList<String>();
                        for (DeviceNew d: mDevices) {
                            device_names.add(d.getName());
                        }

                        // ArrayAdpater 객체 생성 및 설정
                        ArrayAdapter dAdapter = new ArrayAdapter(DeviceListActivity.this,
                                android.R.layout.simple_list_item_1, device_names.toArray());

                        // 리스트뷰에 어뎁터 객체 연결
                        mListView.setAdapter(dAdapter);

                    }
                }

                @Override
                public void onFail() {
                    Toast.makeText(DeviceListActivity.this, getResources().getString(R.string.devicelist_fail), Toast.LENGTH_SHORT).show();
                }
            };

            DeviceNewApiNew deviceApi = new DeviceNewApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    apiCallback
            );
            //개인당 최대 5개의 디바이스만 등록 가능하므로, offset=1만 설정해줘도 됨
            deviceApi.getNewDeviceList(1);
        }
    }
}