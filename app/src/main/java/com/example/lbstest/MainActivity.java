package com.example.lbstest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public LocationClient mylocationClient;
    public TextView positiontext;
    private MapView mapView;
    private BaiduMap baiduMap;
    private  boolean isFirstLocate=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mylocationClient =new LocationClient(getApplicationContext());
        mylocationClient.registerLocationListener(new MYLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapView=(MapView)findViewById(R.id.bmapView);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        positiontext=(TextView)findViewById(R.id.position_text_view);
        List<String>permissionList=new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.
                ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.
                READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission. READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.
                WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission. WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String[]permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else {
            requestLocastion();
        }
    }
    private void navigateto(BDLocation location){
        if (isFirstLocate){
            LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update=MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate=false;
        }
        MyLocationData.Builder locationbuilder=new MyLocationData.Builder();
        locationbuilder.latitude(location.getLatitude());
        locationbuilder.longitude(location.getLongitude());
        MyLocationData locationData=locationbuilder.build();
        baiduMap.setMyLocationData(locationData);
    }
    private void requestLocastion(){
        initLocation();
        mylocationClient.start();
    }
    private  void initLocation(){
        LocationClientOption locationClientOption=new LocationClientOption();
        locationClientOption.setScanSpan(3000);
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClientOption.setIsNeedAddress(true);
        mylocationClient.setLocOption(locationClientOption);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mylocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result : grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须通过所有同意才可以使用本软件", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocastion();
                }else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public  class  MYLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

                        navigateto(bdLocation);
//                    StringBuilder currentPpsition =new StringBuilder();
//                    currentPpsition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
//                    currentPpsition.append("经度：").append(bdLocation.getLongitude()).append("\n");
//                    currentPpsition.append("国家：").append(bdLocation.getCountry()).append("\n");
//                    currentPpsition.append("省：").append(bdLocation.getProvince()).append("\n");
//                    currentPpsition.append("市区：").append(bdLocation.getCity()).append("\n");
//                    currentPpsition.append("区：").append(bdLocation.getDistrict()).append("\n");
//                    currentPpsition.append("街道：").append(bdLocation.getStreet()).append("\n");
//                    currentPpsition.append("定位方式：");
//                    if (bdLocation.getLocType()==BDLocation.TypeGpsLocation){
//                        currentPpsition.append("GPS");
//                    }else  if (bdLocation.getLocType()==BDLocation.TypeNetWorkException){
//                        currentPpsition.append("网络");
//                    }else {
//                        currentPpsition.append("读不出来");
//                    }
//                    positiontext.setText(currentPpsition);
                }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
   }




    }



