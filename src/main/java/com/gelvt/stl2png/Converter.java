package com.gelvt.stl2png;

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
            br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String lineStr;
            while (null != (lineStr = br.readLine())){
                System.out.println(lineStr);
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
        try {
            stlReader.open();
            povFileGenerator.init();

            Polygon polygon;
            while(null != (polygon = stlReader.nextPolygon())){
                povFileGenerator.addPolygon(polygon);
            }
        } finally {
            if (stlReader != null){
                stlReader.close();
            }
            povFileGenerator.close();
        }
    }
}
