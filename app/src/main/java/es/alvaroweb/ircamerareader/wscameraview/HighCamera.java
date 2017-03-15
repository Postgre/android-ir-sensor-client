package es.alvaroweb.ircamerareader.wscameraview;
import android.util.Log;

import com.google.common.primitives.Bytes;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import okio.ByteString;

/**
 * 00 01 02 04 ------
 Data is arranged on 240(0xF0) rows of 84 bytes(FF FF FF n_row and 80 of data):
 FF FF FF 01 <DATA>
 FF FF FF 02 <DATA>
 FF FF FF 03 <DATA>
 ...
 FF FF FF F0 <DATA>
 FF FF FF <TELEMETRY> (38 Bytes)
 Where the 4th byte is the number of the row
 Every row of the actual picture has 2 rows of the raw data
 so the image is 160 x 120 (20198 Bytes)
 */

public class HighCamera {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final int TELEMETRY_ROW_NUMBER = 240;
    private static final int BYTES_IN_ROW_NUMBER = 81;
    private static final String DEBUG_TAG = HighCamera.class.getSimpleName();

    private byte[][] frame;
    FrameCallback frameCallback;
    private byte[] remains;
    private byte[] delimiter = new byte[]{-1,-1,-1};

    public HighCamera(FrameCallback callback) {
        frame = new byte[120][160];
        frameCallback = callback;
        remains = new byte[]{};
    }

    public void consumeData(ByteString data){

        List<byte[]> pieces = delimiterData(Bytes.concat(remains, data.toByteArray()), delimiter);

        int lastIndex = pieces.size() - 1;
        for(int i = 0; i < pieces.size(); i++){
            if(i == lastIndex) continue;
            processRow(pieces.get(i));
        }
        remains = pieces.get(lastIndex);
    }


    public void processRow(byte[] row){
        if(row.length < BYTES_IN_ROW_NUMBER){
            return;
        }
        int rowNumber = byteToInt(row[0]);
        Log.d(DEBUG_TAG, "rownumber:"+rowNumber);
        if (rowNumber < TELEMETRY_ROW_NUMBER){
           getFrameData(rowNumber, row);
        }else{
            getTelemetryData(row);
        }
    }

    private void getTelemetryData(byte[] row) {
        // todo telemetry
        frameCallback.frameReady(frame);
    }

    private void getFrameData(int rowNumber, byte[] row){
        for(int i = 0; i < BYTES_IN_ROW_NUMBER - 1; i++){
            int ind = i + 1;
            int frameRow = rowNumber / 2;
            int frameCol = rowNumber % 2 * (BYTES_IN_ROW_NUMBER -1)+ ind;
            try{
            frame[frameRow][frameCol] = row[ind];
            }catch (ArrayIndexOutOfBoundsException e){
                Log.e(DEBUG_TAG, e.getLocalizedMessage());
            }
        }
    }

    private int byteToInt(byte b){
        return b & 0xff;
    }

    interface FrameCallback{
        void frameReady(byte[][] frame);
    }

//    private byte[] hexStringToByteArray(String s) {
//        int len = s.length();
//        if(!(len % 2 == 0)){
//            Log.e(DEBUG_TAG, "string is not even");
//        }
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
//                    + Character.digit(s.charAt(i+1), 16));
//        }
//        return data;
//    }
//
//    public static String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for ( int j = 0; j < bytes.length; j++ ) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }

    private List<byte[]> delimiterData(byte[] array, byte[] delimiter) {
        List<byte[]> byteArrays = new LinkedList<>();
        if (delimiter.length == 0) {
            return byteArrays;
        }
        int begin = 0;

        outer:
        for (int i = 0; i < array.length - delimiter.length + 1; i++) {
            for (int j = 0; j < delimiter.length; j++) {
                if (array[i + j] != delimiter[j]) {
                    continue outer;
                }
            }
            byteArrays.add(Arrays.copyOfRange(array, begin, i));
            begin = i + delimiter.length;
        }
        byteArrays.add(Arrays.copyOfRange(array, begin, array.length));
        return byteArrays;
    }
}
