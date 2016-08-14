package com.example.botalovns.weathergdg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.botalovns.weathergdg.common.CityListAdapter;
import com.example.botalovns.weathergdg.helpers.DatabaseHelper;
import com.example.botalovns.weathergdg.models.CityModel;

import java.util.ArrayList;

public class ListCitiesActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ListView listView = null;
    TextView messageNotCities = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cities);

        messageNotCities = (TextView)findViewById(R.id.notCitiesMessage);
        listView = (ListView)findViewById(R.id.listCities);
        registerForContextMenu(listView);

        //updateWeather();
    }

    @Override
    public void onResume(){
        super.onResume();

        databaseHelper = new DatabaseHelper(this);
        ArrayList<CityModel> cities = databaseHelper.getCities();
        updateListView(cities);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //Выхов активити для поиска нового города
    public void onAddNewCity(View view){
        Intent intent = new Intent(this, SearchCityActivity.class);
        startActivity(intent);
    }


    private void updateListView(ArrayList<CityModel> cities){

        CityListAdapter cityListAdapter = new CityListAdapter(this, cities);

        if(listView!=null && cityListAdapter!=null && cities.size()>0){
            listView.setAdapter(cityListAdapter);
            listView.setVisibility(View.VISIBLE);
            messageNotCities.setVisibility(View.GONE);
        }
        else{
            listView.setVisibility(View.GONE);
            messageNotCities.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listCities) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(((CityModel)listView.getAdapter().getItem(info.position)).getName());
            String[] menuItems = {"Удалить"};//getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = {"Удалить"};// getResources().getStringArray(R.array.menu);
        String menuItemName = menuItems[menuItemIndex];
        //String listItemName = Countries[info.position];
        CityModel city = (CityModel)listView.getAdapter().getItem(info.position);
        switch (menuItemIndex){
            case 0:{
                if(databaseHelper==null){
                    databaseHelper=new DatabaseHelper(this);
                }
                databaseHelper.deleteCity(city);
                updateListView(databaseHelper.getCities());
                break;
            }
        }

        /*TextView text = (TextView)findViewById(R.id.footer);
        text.setText(String.format("Selected %s for item %s", menuItemName, listItemName));*/
        return true;
    }
}
