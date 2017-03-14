package es.alvaroweb.ircamerareader.wscameraview;

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
    private static final int TELEMETRY_ROW_NUMBER = 240;
    private static final int BYTES_IN_ROW_NUMBER = 80;
//    def process_row(self, row):
//            if(len(row) < EIGHT_BYTES_ROW):
//            return
//    n_row = row[0]
//
//            if n_row < Y_LENGTH:  # normal row
//            try:
//                    for indx, val in enumerate(row[1:]):
//    f_row = (n_row)/2
//    f_col = (n_row) % 2 * 80 + indx
//    self.frame_arr[f_row][f_col] = val
//
//    except ValueError and IndexError as e:
//            logging.warn("row size is not suitable: %s", e.message)
//            else:  # telemetry row
//            self.process_telemetry(row)
//            self.process_frame()

    private byte[][] frame;
    FrameCallback frameCallback;

    public HighCamera(int xsize, int ysize, FrameCallback callback) {
        frame = new byte[xsize][ysize];
        frameCallback = callback;
    }

    public void processRow(byte[] row){
        if(row.length < BYTES_IN_ROW_NUMBER){
            return;
        }
        int rowNumber = byteToInt(row[0]);
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
        for(int ind = 1; ind < BYTES_IN_ROW_NUMBER; ind++){
            int frameRow = rowNumber / 2;
            int frameCol = rowNumber % 2 * BYTES_IN_ROW_NUMBER + ind;
            frame[frameRow][frameCol] = row[ind];
        }
    }

    private int byteToInt(byte b){
        return b & 0xff;
    }

    interface FrameCallback{
        void frameReady(byte[][] frame);
    }
}
