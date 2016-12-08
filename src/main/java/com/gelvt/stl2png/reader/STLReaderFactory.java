package com.gelvt.stl2png.reader;

import java.io.*;
import static com.gelvt.stl2png.StreamUtil.*;

/**
 * Created by elvin on 16-12-6.
 */
public class STLReaderFactory {
    /**
     * 根据STL文件的类型自动创建对应的STLReader
     * @param stlFilePath STL文件的路径
     * @return STLReader实例
     */
    public static STLReader newInstance(String stlFilePath) throws IOException {
        if (isBinaryFile(stlFilePath)){
            return new BinarySTLReader(stlFilePath);
        }else{
            return new AsciiSTLReader(stlFilePath);
        }
    }

    private static boolean isBinaryFile(String path) throws IOException {
        File file = new File(path);
        int size = (int)file.length();
        if (size < 84){
            throw new IOException("File is too short to be an STL file");
        }
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(path)));
            inputStream.skipBytes(80);
            int polygonCount = readInt(inputStream);
            int predictedSize = 80 + 4 + 50 * polygonCount;
            return predictedSize == size;
        } finally {
            if (inputStream != null){
                inputStream.close();
            }
        }
    }
}
