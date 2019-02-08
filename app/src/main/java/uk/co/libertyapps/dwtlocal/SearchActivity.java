package uk.co.libertyapps.dwtlocal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Select product");
        setContentView(R.layout.products);

        SharedPreferences settings = getSharedPreferences("USER", Context.MODE_PRIVATE);
        session = settings.getString("WEEK", "");

        updateAdapter();
    }

    private void updateAdapter() {

        ArrayList<String> firebase = new ArrayList<String>() {{
            add("800g White");
            add("12 Sliced White Rolls");
            add("400g Wholemeal");
            add("800g White Farmhouse");
            add("800g Seeded Batch 5 Seeds");
            add("6 Pack Crumpets");
            add("2 Giant Crumpets");
            add("6 White Thins");
            add("4 Fruity Teacakes");
        }};

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, firebase);
        final ListView lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(itemsAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // save to local pref
                SharedPreferences settings = getSharedPreferences("USER", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.commit();

                Intent intent = new Intent(SearchActivity.this, ChoiceActivity.class);
                startActivity(intent);
            }
        });
    }
}

