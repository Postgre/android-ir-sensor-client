package es.alvaroweb.ircamerareader.wscameraview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.AppCompatImageView;

import java.util.Random;

import okio.ByteString;

/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */

public class CameraView extends AppCompatImageView implements WebsocketConnection.OnReceiveRow, HighCamera.FrameCallback {

    private static final String DEBUG_TAG = CameraView.class.getSimpleName();
    private Bitmap bitmap;
    private int sizex = 160;
    private int sizey = 120;
    private static Random random = new Random();
    private WebsocketConnection websocketConnection;
    private HighCamera highCamera;
    private Runnable t;


    public CameraView(Context context) {
        super(context);
        initBitmap();
    }

    public CameraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBitmap();
    }

    private void initBitmap(){
        bitmap = Bitmap.createBitmap(sizex, sizey, Bitmap.Config.ARGB_8888);
        highCamera = new HighCamera(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Bitmap has to be set here. and this callback is called from UI
        this.setImageBitmap(bitmap);
    }

    public void setImage(byte[][] array){
//        cleanImage();
        boolean dimensionMaches = array[0].length == bitmap.getWidth() &&
                array.length == bitmap.getHeight();

        if(!dimensionMaches){
            Log.d("image","doesn't match the dimension");
            return;
        }

        for (int i = 0; i < bitmap.getHeight(); i++) {
            for(int j =0; j < bitmap.getWidth(); j++) {
                int pixel = convertByteToInt(array[i][j]);
                bitmap.setPixel(j,i, Color.rgb(pixel, pixel, pixel));
            }
        }
    }
    public void setImage(View view){
        byte[][] arr = new byte[sizey][sizex];
        for(int i = 0; i < sizey; i++){
            for(int j = 0; j < sizex; j++){
                arr[i][j] = ((byte)randint(Byte.MIN_VALUE, Byte.MAX_VALUE));
            }
        }
        setImage(arr);
    }

    public void cleanImage(){
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for(int j =0; j < bitmap.getHeight(); j++) {
                bitmap.setPixel(i, j, Color.rgb(255, 255, 255));
            }
        }
    }

    private int convertByteToInt(byte b){
        return b & 0xff;
    }

    private int randint(int min, int max){
        return random.nextInt(max + 1 - min) + min;
    }

    public void connectTo(String uri) {
            Log.d(CameraView.class.getSimpleName(), "uri received: " + uri);
            websocketConnection = new WebsocketConnection(uri, this);
    }

    @Override
    public void receiveRows(ByteString data) {
        if(data.size() < 1){
            return;
        }
        highCamera.consumeData(data);
    }

    @Override
    public void frameReady(byte[][] frame) {
        this.setImage(frame);
    }

    public void stopWebsocket() {
        websocketConnection.close();
    }

    public interface UpdateArray{
        void updateArray();
    }
}
