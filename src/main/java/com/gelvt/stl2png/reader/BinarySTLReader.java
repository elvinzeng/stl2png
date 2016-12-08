package com.gelvt.stl2png.reader;

import com.gelvt.stl2png.model.Vertex;
import com.gelvt.stl2png.model.Polygon;

import java.io.*;

import static com.gelvt.stl2png.StreamUtil.readFloat;

/**
 * 专门负责读取二进制格式的STL文件的STLReader
 * Created by elvin on 16-12-6.
 */
public class BinarySTLReader extends STLReader {
    private DataInputStream inputStream;
    private boolean isInitialized = false;
    /**
     * @param stlFilePath STL文件的路径
     */
    BinarySTLReader(String stlFilePath) {
        super(stlFilePath);
    }

    public Polygon nextPolygon() throws IOException {
        Polygon polygon = null;
        Vertex normal = null;
        Vertex[] vertices = new Vertex[3];
        try{
            normal = new Vertex(
                    readFloat(inputStream),
                    readFloat(inputStream),
                    readFloat(inputStream)
            );
            for(int i = 0; i < 3; i++){
                Vertex vertex = new Vertex(
                        readFloat(inputStream),
                        readFloat(inputStream),
                        readFloat(inputStream)
                );
                vertices[i] = vertex;
            }
            inputStream.skipBytes(2);
        } catch (EOFException e){
            return null;
        }

        if (null != normal){
            polygon = new Polygon();
            polygon.setNormal(normal);
            polygon.setVertices(vertices);
        }

        return polygon;
    }

    public void open() throws IOException {
        if (isInitialized){
            return;
        }

        inputStream = new DataInputStream(new BufferedInputStream(
                new FileInputStream(this.getStlFilePath())));
        inputStream.skipBytes(84);

        isInitialized = true;
    }

    public void close() throws IOException {
        if (inputStream != null){
            inputStream.close();
            inputStream = null;
        }
    }

}
