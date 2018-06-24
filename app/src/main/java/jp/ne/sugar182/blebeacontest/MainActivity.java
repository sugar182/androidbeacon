package jp.ne.sugar182.blebeacontest;

import android.Manifest;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private int cnt;
    BluetoothManager mbluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cnt = 0;

        // BLE Scanner の取得
        mbluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mbluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        // 6.0以降はコメントアウトした処理をしないと初回はパーミッションがOFFになっています。
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.text_beacon_info)).setText("スキャン開始");
                // (何もフィルタリングしない) スキャンフィルタの作成
                //List<ScanFilter> mScanFilters = new ArrayList<ScanFilter>();
                // スキャンモードの作成
                //ScanSettings.Builder mScanSettingBuiler = new ScanSettings.Builder();
                //mScanSettingBuiler.setScanMode(android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY);
                //ScanSettings mScanSettings = mScanSettingBuiler.build();
                // 作成したスキャンフィルタとモードでスキャン開始
                //mBluetoothLeScanner.startScan(mScanFilters, mScanSettings, mScanCallback);
                mBluetoothLeScanner.startScan(mScanCallback);
            }
        });

        findViewById(R.id.stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.text_beacon_info)).setText("スキャン終了");
                mBluetoothLeScanner.stopScan(mScanCallback);
            }
        });

    }

    private ScanCallback mScanCallback = new ScanCallback() {

        public void onBatchScanResults(List<ScanResult> results) {
        };

        public void onScanFailed(int errorCode) {
        };

        // 通常このメソッドがコールバックされます
        public void onScanResult(int callbackType, ScanResult result) {
            cnt++;

            ((TextView) findViewById(R.id.textView2)).setText("スキャンカウント：" + cnt);
            byte[] msg_bytes = result.getScanRecord().getManufacturerSpecificData(76);
            if (msg_bytes == null) {
                ((TextView) findViewById(R.id.text_beacon_info)).setText("未検出");
                return;
            }
            String msg = "";
            for (int i = 9; i < msg_bytes.length; i++) {
                msg += Byte.toString(msg_bytes[i]) + " ";
            }
            Log.d("ibeacon msg", msg);
            ((TextView) findViewById(R.id.text_beacon_info)).setText("ibeacon msg：" + new String(msg));

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
