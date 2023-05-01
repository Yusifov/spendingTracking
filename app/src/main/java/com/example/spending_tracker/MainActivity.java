package com.example.spending_tracker;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.ListActivity;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spending_tracker.customspinner.CustomAdapter;
import com.example.spending_tracker.customspinner.CustomItem;
import com.example.spending_tracker.sqlite.object;
import com.example.spending_tracker.sqlite.sqliteOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ListActivity implements AdapterView.OnItemSelectedListener {
    sqliteOperations sqlOperations;
    List<object> listAlldata;
    ArrayAdapter<object> adapter1;

    Spinner customSpinner;
    ArrayList<CustomItem> customList;
    int width = 150;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlOperations = new sqliteOperations(this);
        sqlOperations.openConnecting();

        customSpinner = findViewById(R.id.customIconSpinner);
        customList = getCustomList();
        CustomAdapter adapter = new CustomAdapter(this, customList);
        if (customSpinner != null) {
            customSpinner.setAdapter(adapter);
            customSpinner.setOnItemSelectedListener(this);
        }
        EditText price = findViewById(R.id.price);
        Button send = findViewById(R.id.send);

        /* String[] items = new String[]{"Market", "Dışardan Yemek", "Kahve/Çay", "Alışveriş", "Ek Harçamalar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        customSpinner.setAdapter(adapter); */

        listAlldata = sqlOperations.showAllData(null);
        adapter1 = new ArrayAdapter<object>(this,
                android.R.layout.simple_list_item_1, listAlldata);
        setListAdapter(adapter1);

        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    LinearLayout linearLayout = findViewById(R.id.customSpinnerItemLayout);
                    width = linearLayout.getWidth();
                } catch (Exception e) {}
                customSpinner.setDropDownWidth(width);
                CustomItem item = (CustomItem) customSpinner.getSelectedItem();
                listAlldata = sqlOperations.showAllData("place = '"+
                        item.getSpinnerItemName()+"'");
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
                    try {
                        LinearLayout linearLayout = findViewById(R.id.customSpinnerItemLayout);
                        width = linearLayout.getWidth();
                    } catch (Exception e) {}
                    customSpinner.setDropDownWidth(width);
                    CustomItem item = (CustomItem) customSpinner.getSelectedItem();
                    String time = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    object o = new object(item.getSpinnerItemName(), Float.valueOf(String.valueOf(price.getText())), time);
                    price.setText(insert(sqlOperations, item.getSpinnerItemName(),
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

    private ArrayList<CustomItem> getCustomList() {
        customList = new ArrayList<>();
        customList.add(new CustomItem("Market", R.drawable.ic_shopping_cart_black_24dp));
        customList.add(new CustomItem("Dışardan Yemek", R.drawable.baseline_restaurant_menu_24));
        customList.add(new CustomItem("Kahve/Çay", R.drawable.baseline_local_cafe_24));
        customList.add(new CustomItem("Alışveriş", R.drawable.baseline_shopping_bag_24));
        customList.add(new CustomItem("Ek Harçamalar", R.drawable.ic_whatshot_black_24dp));
        return customList;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        try {
            LinearLayout linearLayout = findViewById(R.id.customSpinnerItemLayout);
            width = linearLayout.getWidth();
        } catch (Exception e) {
        }
        customSpinner.setDropDownWidth(width);
        CustomItem item = (CustomItem) adapterView.getSelectedItem();
        Toast.makeText(this, item.getSpinnerItemName(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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