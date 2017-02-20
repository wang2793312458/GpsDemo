package com.velue.gps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;



public class MainActivity extends AppCompatActivity {
    private TextView jul = null;
    private TextView shuju = null;
    private Button startButton = null;
    private LocationClient locationClient = null;
    private static final int UPDATE_TIME = 5000;
    private static int LOCATION_COUTNS = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jul = (TextView) this.findViewById(R.id.jul);
        shuju = (TextView) this.findViewById(R.id.shuju);

        startButton = (Button) this.findViewById(R.id.btn_start);

        locationClient = new LocationClient(this);
        // 设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 是否打开GPS
        option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setProdName("LocationDemo"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME); // 设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);
        // 注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null) {
                    return;
                }
                StringBuffer sb = new StringBuffer(256);

                sb.append("\nLatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nLontitude : ");
                sb.append(location.getLongitude());
                LOCATION_COUTNS++;
                sb.append("\n检查位置更新次数：");
                sb.append(String.valueOf(LOCATION_COUTNS));

//                shuju.setText(sb.toString());
                Toast.makeText(MainActivity.this, "Latitude"+sb.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (locationClient == null) {
                    return;
                }
                if (locationClient.isStarted()) {
                    startButton.setText("Start");
                    locationClient.stop();

                } else {
                    startButton.setText("Stop");
                    locationClient.start();
                    /*
					 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
					 * )后，每隔设定的时间，定位SDK就会进行一次定位。
					 * 如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
					 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
					 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
					 */
                    locationClient.requestLocation();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
    }
}
