package com.example.monitorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    private DatabaseReference reference;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        tvHumid = (TextView) findViewById(R.id.idtvHumidValue);
        tvTemp = (TextView) findViewById(R.id.idtvTempValue);
        tvGas = (TextView) findViewById(R.id.idtvGasValue);
        reference = FirebaseDatabase.getInstance().getReference().child("DHTSensor");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DHTSensor dhtSensor = snapshot.getValue(DHTSensor.class);
                    double humid = dhtSensor.getHumidity();
                    double temp = dhtSensor.getTemperature();
                    long gas = dhtSensor.getGas();
                    tvHumid.setText((String.valueOf(humid)));
                    tvGas.setText(String.valueOf(gas));
                    tvTemp.setText(String.valueOf(temp));
                    //userSetting

                    //app

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}