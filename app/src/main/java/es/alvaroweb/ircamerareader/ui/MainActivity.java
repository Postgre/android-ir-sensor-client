package es.alvaroweb.ircamerareader.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import es.alvaroweb.ircamerareader.R;
import es.alvaroweb.ircamerareader.wscameraview.CameraView;

public class MainActivity extends AppCompatActivity {
    public static final String URI_STRING = "URI_STRING";
    private CameraView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (CameraView) findViewById(R.id.image);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String uri = getIntent().getStringExtra(URI_STRING);
        image.connectTo(uri);
    }


    @Override
    protected void onStop() {
        super.onStop();
        image.stopWebsocket();
    }

    public void ButtonClick(View view){
        image.setRandomImage(view);
    }


}
