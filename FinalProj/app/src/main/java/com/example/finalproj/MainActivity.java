package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "myTag";
    private final String key = "WyP9pjBjktC%2Bea2fFObCERMWEwTJGwwsRMr1YeyFGuqKZSnQwmKcnk1n3ZKf6L8UMGjCSZ4HZDCC%2BSQol89tjQ%3D%3D";
    private final String endPoint = "http://61.43.246.153/openapi-data/service/busanBIMS2";

    //xml 변수
    private EditText xmlBusNum;
    private EditText xmlStationArsno;
    private TextView xmlShowInfo;

    // 파싱을 위한 필드 선언
    private URL url;
    private InputStream is;
    private XmlPullParserFactory factory;
    private XmlPullParser xpp;
    private String tag;
    private int eventType;

    // xml의 값 입력 변수
    private String busNum; // 버스 번호
    private String stationArsno = ""; //출발 정류장 arsNo
    private StringBuffer buffer;
    // 데이터 검색
    private String busNumId; // 버스 번호 Id
    private String stationId;// 출발 정류소명 Id
    private String sStationArriveTime; // 버스의 정류장 도착정보

    private String car1;
    private String min1;
    private String station1;
    private String car2;
    private String min2;
    private String station2;

    private Button lgBtn;
    private Button l2Btn;
    private Button rgBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lgBtn = (Button) findViewById(R.id.btn_login);

        rgBtn = (Button) findViewById(R.id.btn_register);
        //Button imageButton2 = (Button) findViewById(R
        // .id.btn_register);
        lgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Button Clicked", 10);
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent);
            }
        });

        rgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                startActivity(intent);
            }
        });
    }
    public void toast(String texts, int duration) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, texts, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
}