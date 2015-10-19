package com.example.thegumza.oilprice;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView result,date;
    List<Oil> oil = new ArrayList<>();
    Typeface font;
    ListView listView;
    OilListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);
        date = (TextView) findViewById(R.id.date);
        listView = (ListView) findViewById(R.id.listView);
        font = Typeface.createFromAsset(getAssets(), "ZoodHardSell2.ttf");


        result.setTypeface(font);
        date.setTypeface(font);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("9286E9FCF841B2046FCC548E6087F770").build();
        mAdView.loadAd(adRequest);

        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void run() throws Exception {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("text/xml");
        RequestBody body = RequestBody.create(mediaType, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n  <soap:Body>\r\n    <CurrentOilPrice xmlns=\"http://www.pttplc.com/ptt_webservice/\">\r\n      <Language>thai</Language>\r\n    </CurrentOilPrice>\r\n  </soap:Body>\r\n</soap:Envelope>");
        Request request = new Request.Builder()
                .url("http://www.pttplc.com/webservice/pttinfo.asmx?op=CurrentOilPrice")
                .post(body)
                .addHeader("content-type", "text/xml")
                .addHeader("content-length", "length")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    String result = body.string().replace("&lt;", "<").replace("&gt;", ">");
                    parseXML(result);
                } else {

                }
            }

        });


    }

    private void parseXML(String result){

        try {
            Document doc = Jsoup.parse(result, "", Parser.xmlParser());
            for (Element news : doc.getElementsByTag("DataAccess")) {

                Elements date = news.getElementsByTag("PRICE_DATE");
                Elements product = news.getElementsByTag("PRODUCT");
                Elements price = news.getElementsByTag("PRICE");
                Oil object = new Oil();
                object.setDate(date.text());
                object.setName(product.text());
                object.setPrice(price.text());

                if(!price.text().equals("")){
                    oil.add(object);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        date.setText("ณ วันที่ "+oil.get(0).getDate().substring(0,10));
                        adapter = new OilListAdapter(oil,MainActivity.this);
                        listView.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
