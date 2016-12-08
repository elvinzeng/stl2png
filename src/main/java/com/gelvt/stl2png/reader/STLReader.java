package com.gelvt.stl2png.reader;

import com.gelvt.stl2png.model.Polygon;

import java.io.IOException;

/**
 * STL文件读取工具
 * Created by elvin on 16-12-6.
 */
public abstract class STLReader {
    private String stlFilePath;

    /**
     * @param stlFilePath STL文件的路径
     */
    public STLReader(String stlFilePath){
        this.stlFilePath = stlFilePath;
    }

    public String getStlFilePath() {
        return stlFilePath;
    }

    /**
     * 读取下一个平面的数据。
     * 在读取之前，需要先open这个STLReader。
     * @return 如果存在下一个平面则为下一个平面，如果不存在下一个平面则返回null。
     */
    public abstract Polygon nextPolygon() throws IOException;

    /**
     * 打开STLReader。
     */
    public abstract void open() throws IOException;

    /**
     * 关闭STLReader。这个操作将会同时关闭底层的流。
     */
    public abstract void close() throws IOException;
}
