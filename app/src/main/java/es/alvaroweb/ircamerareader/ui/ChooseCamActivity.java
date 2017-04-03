package es.alvaroweb.ircamerareader.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import es.alvaroweb.ircamerareader.CamerasInfo;
import es.alvaroweb.ircamerareader.Constants;
import es.alvaroweb.ircamerareader.HttpConnection;
import es.alvaroweb.ircamerareader.R;

import static android.R.id.list;
import static es.alvaroweb.ircamerareader.Constants.DEVEL_URL;
import static es.alvaroweb.ircamerareader.Constants.PARAMETERS;
import static es.alvaroweb.ircamerareader.Constants.PRODUCT_URL;


public class ChooseCamActivity extends AppCompatActivity implements HttpConnection.AResponse{

    private ListView listView;
    private String[] listArr;
    private Intent intent;
    private ArrayAdapter<String> adapter;
    private String produc = "ws://" + PRODUCT_URL + "/client" + PARAMETERS;
    private String devel = "ws://" + DEVEL_URL + "/client" + PARAMETERS;
    private String uri = devel;
    private HttpConnection httpConnection;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cam_activithy);

        listView = (ListView) findViewById(R.id.cameras_list_view);
        button = (Button) findViewById(R.id.environment_button);

        intent = new Intent(this, MainActivity.class);
        httpConnection = new HttpConnection(this);

        initClientArray(button.getText().toString());
    }

    private void initList(){
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

    private void initClientArray(String textButton) {
        listArr = new String[0];
        try {
            if (textButton == "devel") {
                httpConnection.getCamsInfo(DEVEL_URL);
            } else {
                httpConnection.getCamsInfo(PRODUCT_URL);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void switchUrl(View v){
        Button button = (Button)v;
        initClientArray(button.getText().toString());
        if(uri.equals(produc)){
            uri = devel;
            button.setText("devel");
        }else{
            uri = produc;
            button.setText("produc");

        }
    }

    @Override
    public void getResponse(CamerasInfo camerasInfo) {
        int count = camerasInfo.getCount();
        listArr = new String[count];
        for (CamerasInfo.Camera c : camerasInfo.getCams()) {
            listArr[count - 1] = c.getName();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initList();
            }
        });

    }
}
