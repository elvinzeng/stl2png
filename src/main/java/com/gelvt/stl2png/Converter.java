package com.gelvt.stl2png;

import com.gelvt.stl2png.model.Vertex;
import com.gelvt.stl2png.model.Polygon;
import com.gelvt.stl2png.povray.POVFileGenerator;
import com.gelvt.stl2png.povray.TemplateBasedPOVFileGenerator;
import com.gelvt.stl2png.reader.STLReader;
import com.gelvt.stl2png.reader.STLReaderFactory;

import java.io.*;

/**
 * POV file converter
 * Created by elvin on 16-12-6.
 */
public class Converter {
    public static final String DEFAULT_TEMPLATE_FILE_PATH
            = "povray/default.template";

    /**
     * convert pov file to png file
     * @param povFilePath pov file path
     * @param pngFilePath png file path
     */
    void pov2png(String povFilePath, String pngFilePath)
            throws IOException, InterruptedException {
        String params = " -I\"" + povFilePath
                + "\" +FN -o\"" + pngFilePath + "\" +Q9 +AM1 +A +UA";
        BufferedReader br = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("povray", params);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            if (Program.getCommand().hasOption("v")){
                br = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String lineStr;
                while (null != (lineStr = br.readLine())){
                    System.out.println(lineStr);
                }
            }

            if (process.waitFor() != 0) {
                if (process.exitValue() == 1){
                    System.out.println("com command execute failed");
                    System.out.println("cmd:povray" + params);
                }
            }
        } finally {
            if (br != null){
                br.close();
            }
        }
    }


    /**
     * convert STL file to pov file
     * @param stlFilePath STL file path
     * @param povFilePath pov file path
     * @throws IOException when file access error
     */
    void stl2pov(String stlFilePath, String povFilePath
            ) throws IOException {
        File stlFile = new File(stlFilePath);
        if (!stlFile.exists()){
            throw new IllegalArgumentException("specified STL file not found!");
        }
        POVFileGenerator povFileGenerator = new TemplateBasedPOVFileGenerator(povFilePath
                , DEFAULT_TEMPLATE_FILE_PATH);

        STLReader stlReader = STLReaderFactory.newInstance(stlFilePath);
        double v = 0;
        double s = 0;
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
        float minZ = Float.MAX_VALUE;
        float maxZ = Float.MIN_VALUE;
        try {
            stlReader.open();
            povFileGenerator.init();

            //int lineNo = 0;
            Polygon polygon;
            while(null != (polygon = stlReader.nextPolygon())){
                povFileGenerator.addPolygon(polygon);
                if (Program.getCommand().hasOption("S")){
                    double ps = getPartialSurfaceArea(polygon);
                    s += ps;
                    //lineNo++;
                    //System.out.println("lineNo:" + lineNo + " ps:" + ps + " s:" + s + " polygon:" + polygon);
                }
                if (Program.getCommand().hasOption("V")){
                    v += getPartialVolume(polygon);
                }
                if (Program.getCommand().hasOption("d")){
                    Vertex[] vertices = polygon.getVertices();
                    for(Vertex vertex : vertices){
                        if (vertex.getX() < minX){
                            minX = vertex.getX();
                        }
                        if (vertex.getX() > maxX){
                            maxX = vertex.getX();
                        }
                        if (vertex.getY() < minY){
                            minY = vertex.getY();
                        }
                        if (vertex.getY() > maxY){
                            maxY = vertex.getY();
                        }
                        if (vertex.getZ() < minZ){
                            minZ = vertex.getZ();
                        }
                        if (vertex.getZ() > maxZ){
                            maxZ = vertex.getZ();
                        }
                    }
                }
            }

            if (Program.getCommand().hasOption("S")){
                System.out.println("surface area: " + s);
            }
            if (Program.getCommand().hasOption("V")){
                System.out.println("volume: " + v);
            }
            if (Program.getCommand().hasOption("d")){
                System.out.println("size: " + (maxX - minX) + ","
                        + (maxY - minY) + "," + (maxZ - minZ));
            }
        } finally {
            if (stlReader != null){
                stlReader.close();
            }
            povFileGenerator.close();
        }
    }

    private double getPartialVolume(Polygon polygon){
        Vertex[] vertices = polygon.getVertices();
        return (vertices[0].getX() * vertices[1].getY() * vertices[2].getZ()
                + vertices[0].getY() * vertices[1].getZ() * vertices[2].getX()
                + vertices[0].getZ() * vertices[1].getX() * vertices[2].getY()
                - vertices[0].getX() * vertices[1].getZ() * vertices[2].getY()
                - vertices[0].getZ() * vertices[1].getY() * vertices[2].getX()
                - vertices[0].getY() * vertices[1].getX() * vertices[2].getZ()) / 6;

    }

    private double getPartialSurfaceArea(Polygon polygon){
        Vertex[] vertices = polygon.getVertices();
        double a, b, c;
        a = Math.sqrt((Math.pow((vertices[0].getX() - vertices[1].getX()), 2)
                + Math.pow((vertices[0].getY() - vertices[1].getY()), 2)
                + Math.pow((vertices[0].getZ() - vertices[1].getZ()), 2)));
        b = Math.sqrt((Math.pow((vertices[0].getX() - vertices[2].getX()), 2)
                + Math.pow((vertices[0].getY() - vertices[2].getY()), 2)
                + Math.pow((vertices[0].getZ() - vertices[2].getZ()), 2)));
        c = Math.sqrt((Math.pow((vertices[2].getX() - vertices[1].getX()), 2)
                + Math.pow((vertices[2].getY() - vertices[1].getY()), 2)
                + Math.pow((vertices[2].getZ() - vertices[1].getZ()), 2)));
        double s = (a + b + c) / 2;
        double ret = Math.sqrt(s*(s - a)*(s - b)*(s - c));
        return Double.isNaN(ret) ? 0 : ret;
    }
}
