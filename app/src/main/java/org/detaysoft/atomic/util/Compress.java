package org.detaysoft.atomic.util;

import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
/**
 * @author Detaysoft Java Team
 * 
 */
public class Compress {

    private int expectedUncompressedDataSize = 60536;
    private ByteArrayOutputStream bos;
    private Deflater deflater;
//    private Deflater deflaterWeb;
    private Inflater inflater;

    public Compress() {
        construct();
    }

    public Compress(int _expectedUncompressedDataSize) {
        this.expectedUncompressedDataSize = _expectedUncompressedDataSize;
        construct();
    }

    private void construct() {
        deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true);
//    	deflater = new Deflater();
        inflater = new Inflater(true);
        bos = new ByteArrayOutputStream(expectedUncompressedDataSize);
    }

    public byte[] compress(byte[] _uncompressedData) {
        try {
            deflater.setInput(_uncompressedData);
            deflater.finish();
            byte[] compressBuf = new byte[expectedUncompressedDataSize];
            while (!deflater.finished()) {
                int count = deflater.deflate(compressBuf);
                bos.write(compressBuf, 0, count);
            }

            byte[] compressedData = bos.toByteArray();
            reset();
            return compressedData;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

//    public byte[] compressWeb(byte[] _uncompressedData) {
//        try {
//        	deflaterWeb.setInput(_uncompressedData);
//            deflaterWeb.finish();
//            byte[] compressBuf = new byte[expectedUncompressedDataSize];
//            while (!deflaterWeb.finished()) {
//                int count = deflaterWeb.deflate(compressBuf);
//                bos.write(compressBuf, 0, count);
//            }
//
//            byte[] compressedData = bos.toByteArray();
//            reset();
//            return compressedData;
//        } catch (Exception e) {
//            System.err.println(e);
//            return null;
//        }
//    }
    public byte[] deCompress(byte[] _compressedData) {
        try {
            inflater.setInput(_compressedData);
            byte[] decompressBuf = new byte[expectedUncompressedDataSize];
            long bytesWritten = 0;
            int sameByteCount = 0;
            while (!inflater.finished()) {
                int count = inflater.inflate(decompressBuf);
                bos.write(decompressBuf, 0, count);

                if (bytesWritten == inflater.getBytesWritten()) {
                    sameByteCount++;
                    if (sameByteCount == 1024) {
                        break;
                    }
                } else {
                    bytesWritten = inflater.getBytesWritten();
                }
            }
            byte[] decompressedData = bos.toByteArray();
            reset();
            return decompressedData;
        } catch (Exception e) {
            reset();

            System.err.println(e);
            return null;
        }
    }

    public static byte[] getBytes(Object _obj) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(_obj);
            oos.flush();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            System.err.println(ex);
            return new byte[]{'0'};
        } finally {
            try {
                return bos.toByteArray();
            } catch (Exception ex) {
                System.err.println(ex);
                return new byte[]{'0'};
            }
        }
    }

    public static Object toObject(byte[] _bytes) {
        try {
            Object object = null;
            object = new ObjectInputStream(new ByteArrayInputStream(_bytes)).readObject();
            return object;
        } catch (Exception ex) {
            System.err.println(ex);
            return null;
        }
    }

    private void reset() {
        bos = new ByteArrayOutputStream(expectedUncompressedDataSize);
        deflater.reset();
        inflater.reset();
    }
}
