package com.example.spending_tracker;

import static android.widget.Toast.LENGTH_SHORT;
import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.spending_tracker.customspinner.CustomAdapter;
import com.example.spending_tracker.customspinner.CustomItem;
import com.example.spending_tracker.sqlite.object;
import com.example.spending_tracker.sqlite.sqliteOperations;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    sqliteOperations sqlOperations;
    ArrayList<object> listAlldata;
    // ArrayAdapter<object> adapter1;
    CustomListViewAdapter listViewAdapter;
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
        TextInputEditText price = findViewById(R.id.price);
        FloatingActionButton send = findViewById(R.id.send);
        FloatingActionButton listbtn = findViewById(R.id.listbtn);
        ListView listView = findViewById(R.id.listView);



        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent graphicDisplay = new Intent(MainActivity.this, graphic_display.class);
                startActivity(graphicDisplay);
            }
        });


        listAlldata = sqlOperations.showAllData(null);
        listViewAdapter = new CustomListViewAdapter(MainActivity.this,listAlldata);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String message = sqlOperations.deleteData(listViewAdapter.getItem(i));
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                CustomItem item = (CustomItem) customSpinner.getSelectedItem();
                listAlldata = sqlOperations.showAllData("place = '"+
                        item.getSpinnerItemName()+"'");
                listViewAdapter = new CustomListViewAdapter(MainActivity.this,listAlldata);
                listView.setAdapter(listViewAdapter);
                return false;
            }
        });



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
                listViewAdapter = new CustomListViewAdapter(MainActivity.this,listAlldata);
                listView.setAdapter(listViewAdapter);
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
                    listViewAdapter.add(o);
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
    protected void onResume(){
        sqlOperations.openConnecting();
        super.onResume();
    }
    protected void onPause() {
        sqlOperations.closeConnecting();
        super.onPause();
    }
}