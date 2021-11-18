package com.example.monitorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity  {
    /*
    private CombinedChart mChart;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        tv = (TextView) findViewById(R.id.textView);

        mChart = (CombinedChart) findViewById(R.id.chart);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setOnChartValueSelectedListener(this);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        YAxis left = mChart.getAxisLeft();
        left.setDrawGridLines(false);
        left.setAxisMinimum(0f);

        final List<String> xLabel = new ArrayList<>();
        xLabel.add("Jan");
        xLabel.add("Feb");
        xLabel.add("Mar");
        xLabel.add("Apr");
        xLabel.add("May");
        xLabel.add("Jun");
        xLabel.add("Jul");
        xLabel.add("Aug");
        xLabel.add("Sep");
        xLabel.add("Oct");
        xLabel.add("Nov");
        xLabel.add("Dec");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value % xLabel.size());
            }
        });

        CombinedData data = new CombinedData();
        LineData lineDatas = new LineData();
        lineDatas.addDataSet((ILineDataSet) dataChart());

        data.setData(lineDatas);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        mChart.invalidate();

        float[] arr = left.mEntries;
        tv.setText(String.valueOf(arr[0]) + String.valueOf(arr[1]));
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(this, "Value: "
                + e.getY()
                + ", index: "
                + h.getX()
                + ", DataSet index: "
                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    private static DataSet dataChart() {

        LineData d = new LineData();
        int[] data = new int[] { 1, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 9 };

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 12; index++) {
            entries.add(new Entry(index, data[index]));
        }

        LineDataSet set = new LineDataSet(entries, "Request Ots approved");
        set.setColor(Color.GREEN);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.GREEN);
        set.setCircleRadius(5f);
        set.setFillColor(Color.GREEN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.GREEN);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }*/
    private TextView tvHumid;
    private  TextView tvTemp;
    private TextView tvGas;
    private ListView listView;
    private TextView icPrint;
    private TextView icSettings;
    private DatabaseReference reference;
    private Button btnClearWarning;
    private ArrayList<UserThreshold> userThresholdArrayList;
    private ArrayList<Warning> warningArrayList;
    WarningAdapter warningAdapter;
    Warning warning;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        tvHumid = (TextView) findViewById(R.id.idtvHumidValue);
        tvTemp = (TextView) findViewById(R.id.idtvTempValue);
        tvGas = (TextView) findViewById(R.id.idtvGasValue);
        icPrint = (TextView) findViewById(R.id.idicPrint);
        icSettings = (TextView) findViewById(R.id.idicSettings);
        listView = (ListView) findViewById(R.id.idlvWarning);
        btnClearWarning = (Button) findViewById(R.id.clearLV);

        userThresholdArrayList = new ArrayList<>();
        warningArrayList = new ArrayList<>();
        warningAdapter = new WarningAdapter(warningArrayList);

        reference = FirebaseDatabase.getInstance().getReference().child("DHTSensor");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Query lastQuery = reference.orderByKey().limitToLast(1);
                lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            DHTSensor dhtSensor = snapshot.getValue(DHTSensor.class);
                            //getData from Firebase
                            double humid = dhtSensor.getHumidity();
                            double temp = dhtSensor.getTemperature();
                            long gas = dhtSensor.getGas();
                            String timestamp = dhtSensor.getTime();
                            //setext To Screen
                            tvHumid.setText((String.valueOf(humid)));
                            tvGas.setText(String.valueOf(gas));
                            tvTemp.setText(String.valueOf(temp));

                            if(temp > 40 ){
                                Warning warning = new Warning(timestamp, "High Temperature");
                                warningArrayList.add(warning);
                                listView.setAdapter(warningAdapter);
                                warningAdapter.notifyDataSetChanged();
                            }
                            if(humid < 20){
                                Warning warning = new Warning(timestamp, "Low Humid");
                                warningArrayList.add(warning);
                                listView.setAdapter(warningAdapter);
                                warningAdapter.notifyDataSetChanged();
                            }

                            if(gas > 40000){
                                Warning warning = new Warning(timestamp, "High Gas");
                                warningArrayList.add(warning);
                                listView.setAdapter(warningAdapter);
                                warningAdapter.notifyDataSetChanged();
                            }
                            //Receive Data From Activity2


                            Intent intent = getIntent();
                            Bundle bundle = intent.getBundleExtra("sendUserThreshold");
                            if(bundle != null){
                                userThresholdArrayList = (ArrayList<UserThreshold>) bundle.getSerializable("lstuserThreshhold");
                                for(int i=0; i < userThresholdArrayList.size(); i++){
                                    if((temp >  userThresholdArrayList.get(i).listThreshold[0].getValue()  && userThresholdArrayList.get(i).listThreshold[0].isGreater() && userThresholdArrayList.get(i).listThreshold[0].isUse()) ||
                                            ((humid >  userThresholdArrayList.get(i).listThreshold[1].getValue()  && userThresholdArrayList.get(i).listThreshold[1].isGreater() && userThresholdArrayList.get(i).listThreshold[1].isUse())) ||
                                            ((gas >  userThresholdArrayList.get(i).listThreshold[2].getValue()  && userThresholdArrayList.get(i).listThreshold[2].isGreater() && userThresholdArrayList.get(i).listThreshold[2].isUse()))
                                    ){
                                        Warning warning = new Warning(timestamp, userThresholdArrayList.get(i).getMessage());
                                        warningArrayList.add(warning);
                                        listView.setAdapter(warningAdapter);
                                        warningAdapter.notifyDataSetChanged();
                                    }
                                    if((temp <  userThresholdArrayList.get(i).listThreshold[0].getValue()  && (!userThresholdArrayList.get(i).listThreshold[0].isGreater()) && userThresholdArrayList.get(i).listThreshold[0].isUse()) ||
                                            ((humid <  userThresholdArrayList.get(i).listThreshold[1].getValue()  && (!userThresholdArrayList.get(i).listThreshold[1].isGreater()) && userThresholdArrayList.get(i).listThreshold[1].isUse())) ||
                                            ((gas <  userThresholdArrayList.get(i).listThreshold[2].getValue()  && (!userThresholdArrayList.get(i).listThreshold[2].isGreater()) && userThresholdArrayList.get(i).listThreshold[2].isUse()))
                                    ){
                                        Warning warning = new Warning(timestamp, userThresholdArrayList.get(i).getMessage());
                                        warningArrayList.add(warning);
                                        listView.setAdapter(warningAdapter);
                                        warningAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        icSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        icPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });
        btnClearWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningArrayList.clear();
                listView.setAdapter(warningAdapter);
                warningAdapter.notifyDataSetChanged();
            }
        });
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

}