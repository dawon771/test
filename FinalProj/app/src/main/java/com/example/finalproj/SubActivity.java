package com.example.finalproj;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class SubActivity extends AppCompatActivity {

    private final String ServiceKey = "VL7IbtJG4D9TFZK%2BMY2yQ9Cx8AU8ITjvyTLUHdC9LGuqwG%2B35enJ1LPZ%2FrI1SU%2Fc69Fr5YJLKmBPl5h3WibKKw%3D%3D\n";
    private final String ServiceURL = "http://ws.bus.go.kr/api/rest/stationinfo/";

    private EditText curbusStopId;
    private EditText curbusNum;
    private Button searchBtn;
    private TextView showInfo;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main);
        curbusNum = (EditText)findViewById(R.id.busNum);
        curbusStopId = (EditText)findViewById(R.id.busStopId);
        searchBtn = (Button)findViewById(R.id.button);
        showInfo = findViewById(R.id.showInfo);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            String aa;
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        aa = printBusStopInfo();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showInfo.setText(aa);
                            }
                        });
                    }
                }).start();
            }
        });
    }



    // busStopId : 정류장 번호
    // busNum : 버스 번호

    /* 데이터 갱신 */
    public String printBusStopInfo() {
        String busStopId = curbusStopId.getText().toString();
        String RequestURL = ServiceURL + "getStationByUid" + "?arsId=" + busStopId + "&ServiceKey=" + ServiceKey;

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
                String arrmsg1 = "", arrmsg2 = "", adirection = "", stationNm1 = "", stationNm2 = "", rtNm = "";
                while (xmlEventType != XmlPullParser.END_DOCUMENT) {
                    switch (xmlEventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tags = xpp.getName();
                            if (tags.equals("itemList")) ;
                            else if (tags.equals("rtNm")) { // 버스 번호
                                xpp.next();
                                rtNm = xpp.getText();
                            }
                            else if (tags.equals("arrmsg1")){
                                xpp.next();
                                arrmsg1 = xpp.getText();
                            }
                            else if (tags.equals("arrmsg2")) {
                                xpp.next();
                                arrmsg2 = xpp.getText();
                            }
                            else if (tags.equals("adirection")) {
                                xpp.next();
                                adirection = xpp.getText();
                            }
                            else if (tags.equals("stationNm1")) {
                                xpp.next();
                                stationNm1 = xpp.getText();
                            }
                            else if (tags.equals("stationNm2")) {
                                xpp.next();
                                stationNm2 = xpp.getText();
                            }
                            case XmlPullParser.TEXT:
                                break;
                        case XmlPullParser.END_TAG:
                            tags = xpp.getName();
                                if(tags.equals("itemList")) {
                                    busInfo += "[" + rtNm + "/" + adirection + " 방면]\n";
                                    busInfo += "[첫번째 버스 : " + stationNm1 + "] : " + arrmsg1 + "\n";
                                    busInfo += "[두번째 버스 : " + stationNm2 + "] : " + arrmsg2 + "\n";
                                }
                            break;
                    }
                    xmlEventType = xpp.next();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
           return busInfo;
    }
    public void toast(String texts, int duration) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, texts, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
}