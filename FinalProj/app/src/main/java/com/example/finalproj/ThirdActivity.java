package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class ThirdActivity extends AppCompatActivity {
    private final String ServiceKey = "VL7IbtJG4D9TFZK%2BMY2yQ9Cx8AU8ITjvyTLUHdC9LGuqwG%2B35enJ1LPZ%2FrI1SU%2Fc69Fr5YJLKmBPl5h3WibKKw%3D%3D\n";
    private final String ServiceURL = "http://ws.bus.go.kr/api/rest/busRouteInfo/";

    private EditText busNum;
    private Button busSearchBtn;
    private TextView showInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_main);

        busNum = (EditText)findViewById(R.id.busNum);
        busSearchBtn = (Button)findViewById(R.id.busSearchbtn);
        showInfo = (TextView)findViewById(R.id.busNumInfo);

        busSearchBtn.setOnClickListener(new View.OnClickListener() {

            String printStrData;
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        printStrData = printBusNumInfo();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showInfo.setText(printStrData);
                            }
                        });
                    }
                }).start();
            }

        });
    }

    public String printBusNumInfo() {
        String busNumber = busNum.getText().toString();
        String RequestURL = ServiceURL + "getBusRouteList" + "?strSrch=" + busNumber + "&ServiceKey=" + ServiceKey;

        String busInfo = "";
        try {
            URL url = new URL(RequestURL);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tags;

            xpp.next();
            int xmlEventType = xpp.getEventType();
            String busRouteNm = "", firstBusTm = "", lastBusTm = "", stStationNm = "", edStationNm = "";
            while (xmlEventType != XmlPullParser.END_DOCUMENT) {
                switch (xmlEventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tags = xpp.getName();
                        if (tags.equals("itemList")) ;
                        else if (tags.equals("busRouteNm")) { // 버스 번호
                            xpp.next();
                            busRouteNm = xpp.getText();
                        }
                        else if (tags.equals("firstBusTm")) { // 첫차 시간
                            xpp.next();
                            firstBusTm = xpp.getText().substring(8, 12);
                        }
                        else if (tags.equals("lastBusTm")) { // 막차 시간
                            xpp.next();
                            lastBusTm = xpp.getText().substring(8, 12);
                        }
                        else if (tags.equals("stStationNm")) { // 출발지
                            xpp.next();
                            stStationNm = xpp.getText();
                        }
                        else if (tags.equals("edStationNm")) { // 도착지
                            xpp.next();
                            edStationNm = xpp.getText();
                        }
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tags = xpp.getName();
                        if(tags.equals("itemList")) {
                            busInfo += "["+ busRouteNm +"]\n";
                            busInfo += "첫차 : " + firstBusTm.substring(0,2)+":"+firstBusTm.substring(2,4) + ", 막차 : " + lastBusTm.substring(0,2)+":"+lastBusTm.substring(2,4) + "\n";
                            busInfo += "출발지 : " + stStationNm + ", 도착지 : " + edStationNm + "\n";
                        }
                        break;
                }
                xmlEventType = xpp.next();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //toast(busInfo, 10);
        return busInfo;
    }
    public void toast(String texts, int duration) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, texts, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
}