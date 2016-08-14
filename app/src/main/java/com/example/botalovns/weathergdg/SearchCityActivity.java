package com.example.botalovns.weathergdg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.example.botalovns.weathergdg.common.AsyncResponse;
import com.example.botalovns.weathergdg.common.SearchCityListAdapter;
import com.example.botalovns.weathergdg.helpers.SearchHelper;
import com.example.botalovns.weathergdg.models.CityModel;

import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchCityActivity extends AppCompatActivity implements AsyncResponse {

    EditText searchEditText;
    ListView searchListView;
    SearchCityListAdapter cityListAdapter;
    boolean isEmptyList = false;

    private static SearchCityActivity Instance;

    public static SearchCityActivity getInstance() {
        if (Instance == null) {
            Instance = new SearchCityActivity();
        }
        return Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        Instance = this;

        searchEditText = (EditText) findViewById(R.id.searchCityEditText);
        searchListView = (ListView) findViewById(R.id.searchCityListView);


        searchEditText.addTextChangedListener(new TextWatcher() {
            String outURL = "";
            String[] urlString = {getString(R.string.city_address), "&username=" + getString(R.string.city_username), "&style=full"};

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    try {
                        outURL = urlString[0] + URLEncoder.encode(charSequence.toString(), "UTF-8") + urlString[1] + urlString[2];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SearchHelper searchHelper = new SearchHelper();
                    searchHelper.setContext(getInstance());
                    searchHelper.setDelegate(getInstance());
                    searchHelper.setUrl(outURL);
                    searchHelper.execute();
                    isEmptyList = false;
                } else {
                    isEmptyList = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void processFinish(ArrayList<CityModel> output) {

        if (!isEmptyList) {
            cityListAdapter = new SearchCityListAdapter(this, output);
        } else {
            cityListAdapter = new SearchCityListAdapter(this, new ArrayList<CityModel>());
        }
        if (searchListView != null && output != null) {
            searchListView.setAdapter(cityListAdapter);
        }
    }
}
