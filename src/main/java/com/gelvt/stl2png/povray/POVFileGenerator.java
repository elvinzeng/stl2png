package com.gelvt.stl2png.povray;

import com.gelvt.stl2png.model.Polygon;

import java.io.IOException;

/**
 * POV file generator
 * Created by elvin on 16-12-6.
 */
public abstract class POVFileGenerator {
    private String povFilePath;
    public POVFileGenerator(String povFilePath){
        this.povFilePath = povFilePath;
    }

    public String getPovFilePath() {
        return povFilePath;
    }

    /**
     * 增加一个面的数据
     * @param polygon 需要增加的面
     */
    public abstract void addPolygon(Polygon polygon) throws IOException;

    /**
     * 初始化POVFileGenerator实例。
     */
    public abstract void init() throws IOException;

    /**
     * 关闭POVFileGenerator。这个操作将会同时关闭底层的流。
     */
    public abstract void close() throws IOException;
}
