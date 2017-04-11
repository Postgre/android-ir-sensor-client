package es.alvaroweb.ircamerareader.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import es.alvaroweb.ircamerareader.CamerasInfo;
import es.alvaroweb.ircamerareader.HttpConnection;
import es.alvaroweb.ircamerareader.R;

import static es.alvaroweb.ircamerareader.Constants.DEVEL_URL;
import static es.alvaroweb.ircamerareader.Constants.PARAMETERS;
import static es.alvaroweb.ircamerareader.Constants.PRODUCT_URL;


public class ChooseCamActivity extends AppCompatActivity implements HttpConnection.AResponse{

    private ListView listView;
    private List<CamerasInfo.Camera> cameraList;
    private Intent intent;
    private ArrayAdapter<CamerasInfo.Camera> adapter;
    private String produc = "ws://" + PRODUCT_URL + "/client" + PARAMETERS;
    private String devel = "ws://" + DEVEL_URL + "/client" + PARAMETERS;
    private String uri = devel;
    private HttpConnection httpConnection;
    private Button button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cam_activithy);

        listView = (ListView) findViewById(R.id.cameras_list_view);
        button = (Button) findViewById(R.id.environment_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        intent = new Intent(this, MainActivity.class);
        httpConnection = new HttpConnection(this);

//        initClientArray(button.getText().toString());
    }

    private void initList(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cameraList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = cameraList.get(i).getName();
                intent.putExtra(MainActivity.URI_STRING, uri+name);
                startActivity(intent);
            }
        });
    }

    private void initClientArray(String textButton) {
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
        if(uri.equals(produc)){
            uri = devel;
            button.setText("devel");
        }else{
            uri = produc;
            button.setText("produc");
        }
        initClientArray(button.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        button.setEnabled(false);
    }

    @Override
    public void getResponse(CamerasInfo camerasInfo) {
        cameraList = camerasInfo.getCams();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initList();
                button.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
