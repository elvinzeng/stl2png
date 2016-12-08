package com.gelvt.stl2png.reader;

import com.gelvt.stl2png.model.Coordinate;
import com.gelvt.stl2png.model.Polygon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 专门负责读取Ascii格式的STL文件的STLReader
 * Created by elvin on 16-12-6.
 */
class AsciiSTLReader extends STLReader {

    private BufferedReader streamReader;
    private boolean isInitialized = false;
    private Pattern facetLinePattern;
    private Pattern vertexLinePattern;
    /**
     * @param stlFilePath STL文件的路径
     */
    AsciiSTLReader(String stlFilePath) {
        super(stlFilePath);
    }

    public Polygon nextPolygon() throws IOException {
        Polygon polygon = null;
        String line;
        Coordinate normal = null;
        Coordinate[] vertices = new Coordinate[3];
        int index = 0;
        while(null != (line = streamReader.readLine())){
            Matcher facetMatcher = this.facetLinePattern.matcher(line);
            if (facetMatcher.find()){
                normal = new Coordinate(Float.parseFloat(facetMatcher.group(1))
                        , Float.parseFloat(facetMatcher.group(2))
                        , Float.parseFloat(facetMatcher.group(3)));
            }
            if (line.indexOf("loop") > 0){
                continue;
            }else if (line.indexOf("endfacet") > 0){
                break;
            }
            Matcher vertexMatcher = this.vertexLinePattern.matcher(line);
            if (vertexMatcher.find()){
                Coordinate vertex = new Coordinate(
                        Float.parseFloat(vertexMatcher.group(1))
                        , Float.parseFloat(vertexMatcher.group(2))
                        , Float.parseFloat(vertexMatcher.group(3)));
                vertices[index++] = vertex;
            }
        }

        if (normal != null){
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
        this.facetLinePattern = Pattern.compile(
                "\\s*facet\\s+normal"
                + "\\s+([-]?\\d+\\.\\d{6}e[+-]\\d{3})"
                + "\\s+([-]?\\d+\\.\\d{6}e[+-]\\d{3})"
                + "\\s+([-]?\\d+\\.\\d{6}e[+-]\\d{3})"
                + "\\s*");
        this.vertexLinePattern = Pattern.compile(
                "\\s*vertex"
                + "\\s+([-]?\\d+\\.\\d{6}e[+-]\\d{3})"
                + "\\s+([-]?\\d+\\.\\d{6}e[+-]\\d{3})"
                + "\\s+([-]?\\d+\\.\\d{6}e[+-]\\d{3})"
                + "\\s*");
        streamReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(
                        this.getStlFilePath())));
        isInitialized = true;
    }

    public void close() throws IOException {
        if (streamReader != null){
            streamReader.close();
            streamReader = null;
        }
    }
}
