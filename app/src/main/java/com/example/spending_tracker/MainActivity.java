package com.example.spending_tracker;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ListActivity {
    sqliteOperations sqlOperations;
    List<object> listAlldata;
    ArrayAdapter<object> adapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlOperations = new sqliteOperations(this);
        sqlOperations.openConnecting();

        Spinner selected = findViewById(R.id.list);
        EditText price = findViewById(R.id.price);
        Button send = findViewById(R.id.send);

        String[] items = new String[]{"Market", "Dışardan Yemek", "Kahve/Çay", "Alışveriş", "Ek Harçamalar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        selected.setAdapter(adapter);

        listAlldata = sqlOperations.showAllData(null);
        adapter1 = new ArrayAdapter<object>(this,
                android.R.layout.simple_list_item_1, listAlldata);
        setListAdapter(adapter1);

        selected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listAlldata = sqlOperations.showAllData("place = '"+
                        selected.getSelectedItem()+"'");
                send.setText(String.valueOf(calculate()));
                adapter1 = new ArrayAdapter<object>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, listAlldata);
                setListAdapter(adapter1);
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price.setText("");
                send.setEnabled(true);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!price.getText().toString().matches("")) {
                    String time = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    object o = new object(selected.getSelectedItem().toString(), Float.valueOf(String.valueOf(price.getText())), time);
                    price.setText(insert(sqlOperations, selected.getSelectedItem().toString(),
                            Float.valueOf(String.valueOf(price.getText())), time));
                    send.setEnabled(false);
                    adapter1.add(o);
                    send.setText(String.valueOf(Float.valueOf(String.valueOf(send.getText())) +
                            Float.valueOf(String.valueOf(o.getPrice()))));
                } else {
                    Toast.makeText(getApplicationContext(), "You did not enter a price", LENGTH_SHORT).show();
                }
            }
        });
    }

    public String insert(sqliteOperations sqlOperations, String place, Float price, String time) {
        String response = sqlOperations.insert(place, price, time);
        return response;
    }

    public float calculate() {
        float totalPrice = 0;
        for(int i=0;i<listAlldata.size();i++) {
            totalPrice += listAlldata.get(i).getPrice();
        }
        return totalPrice;
    }

    protected void onResume(){
        sqlOperations.openConnecting();
        super.onResume();
    }
    protected void onPause() {
        sqlOperations.closeConnecting();
        super.onPause();
    }
}