package main.file.reader;

import main.file.SignalFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * Created by chrno on 10/18/14.
 */
public class BufferFileReader {

    public static SignalFile readFile(File file) throws IOException {

        SignalFile signalFile = new SignalFile();

        byte[] buffer = new byte[4];

        InputStream is = new FileInputStream(file.getPath());
        BufferedInputStream bufferedInput = new BufferedInputStream(is);

        if (bufferedInput.read(buffer) != buffer.length) {
            // do something
            throw new IOException("Wrong fielsize");
        }

        signalFile.setSignature(Charset.defaultCharset().decode(ByteBuffer.wrap(buffer)).toString());

        bufferedInput.read(buffer);

        signalFile.setChannelsCount(Integer.reverseBytes(ByteBuffer.wrap(buffer).getInt()));

        bufferedInput.read(buffer);

        signalFile.setPointsCountAtTheSameTime(Integer.reverseBytes(ByteBuffer.wrap(buffer).getInt()));

        bufferedInput.read(buffer);

        signalFile.setSpectralLinesCount(Integer.reverseBytes(ByteBuffer.wrap(buffer).getInt()));

        bufferedInput.read(buffer);

        signalFile.setCutOffFrequency(Integer.reverseBytes(ByteBuffer.wrap(buffer).getInt()));

        bufferedInput.read(buffer);

        signalFile.setFrequencyResolution(ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat());

        bufferedInput.read(buffer);

        signalFile.setTimeToReceive(ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat());

        bufferedInput.read(buffer);

        signalFile.setTotalTimeToReceive(Integer.reverseBytes(ByteBuffer.wrap(buffer).getInt()));

        bufferedInput.read(buffer);

        signalFile.setUserBlocksCount(Integer.reverseBytes(ByteBuffer.wrap(buffer).getInt()));

        bufferedInput.read(buffer);

        signalFile.setDataSize(Integer.reverseBytes(ByteBuffer.wrap(buffer).getInt()));

        bufferedInput.read(buffer);

        signalFile.setSystemBlocksCount(Integer.reverseBytes(ByteBuffer.wrap(buffer).getInt()));

        bufferedInput.read(buffer);

        signalFile.setMaxValue(ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat());

        bufferedInput.read(buffer);

        signalFile.setMinValue(ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat());


        while (bufferedInput.available() != 0) {
            bufferedInput.read(buffer);

            signalFile.addSignal(ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat());
        }


//        DataInputStream input = new DataInputStream(new FileInputStream(file.getPath()));
//
//        if (input.read(buffer) != buffer.length) {
//            // do something
//            throw new IOException("Wrong fielsize");
//        }
//
//        signalFile.setSignature(Charset.defaultCharset().decode(ByteBuffer.wrap(buffer)).toString());
//
//        signalFile.setChannelsCount(input.readInt());
//
//        signalFile.setPointsCountAtTheSameTime(input.readInt());
//
//        signalFile.setSpectralLinesCount(input.readInt());


        bufferedInput.close();

        return signalFile;

//        try (BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
//
//            byte[] firstBytes = new byte[4];
//            int read = br.read(firstBytes);
//
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
