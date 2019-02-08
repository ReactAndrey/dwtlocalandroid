package uk.co.libertyapps.dwtlocal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WeeksActivity extends AppCompatActivity {

    ArrayList<String> firebase, fire, subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Tests");

//        AzureServiceAdapter.Initialize(this);

        setContentView(R.layout.weeks);

        firebase = new ArrayList<String>() {{
            add("800g White");
            add("12 Sliced White Rolls");
            add("400g Wholemeal");
            add("800g White Farmhouse");
            add("800g Seeded Batch 5 Seeds");
            add("6 Pack Crumpets");
            add("2 Giant crumpets");
        }};

        fire = new ArrayList<String>() {{
            add("85");
            add("6");
            add("5");
            add("5");
            add("4");
            add("8");
            add("6");
        }};

        subtitle = new ArrayList<String>() {{
            add("Today");
            add("Yesterday");
            add("8th Nov 18");
            add("8th Nov 18");
            add("8th Nov 18");
            add("8th Nov 18");
            add("8th Nov 18");
        }};

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, firebase) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(firebase.get(position));
                text2.setText(subtitle.get(position));
                return view;
            }
        };



/*        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(WeeksActivity.this, android.R.layout.simple_list_item_1, firebase); */
        final ListView lv = (ListView) findViewById(R.id.listview);



        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                          Intent intent = new Intent(view.getContext(), TestingActivity.class);
                                          startActivity(intent);
                                      }
                                  });


                FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchActivity.class);
                startActivity(intent);

                // finish();
            }
        });
    }

}


