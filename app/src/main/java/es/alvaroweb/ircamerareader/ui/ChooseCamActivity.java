package es.alvaroweb.ircamerareader.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import es.alvaroweb.ircamerareader.R;


public class ChooseCamActivity extends AppCompatActivity {

    private ListView listView;
    private String[] listArr = {"camera0","camera1","camera2","camera3","camera4","camera5"};
    private Intent intent;
    private ArrayAdapter<String> adapter;
    private String produc = "ws://cloudwebsocket2-ir-cloud.espoo-apps.ilab.cloud/client?pass=30022&camera_name=";
    private String devel = "ws://cloudwebsocket-devel-ir-cloud.espoo-apps.ilab.cloud/client?pass=30022&camera_name=";
    private String uri = devel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cam_activithy);

        listView = (ListView) findViewById(R.id.cameras_list_view);
        intent = new Intent(this, MainActivity.class);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listArr);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = listArr[i];
                intent.putExtra(MainActivity.URI_STRING, uri+name);
                startActivity(intent);
            }
        });
    }

    public void switchUrl(View v){
        Button button = (Button)v;
        if(uri.equals(produc)){
            uri = devel;
            button.setText("devel");
        }else{
            uri = produc;
            button.setText("produc");

        }
    }
}
