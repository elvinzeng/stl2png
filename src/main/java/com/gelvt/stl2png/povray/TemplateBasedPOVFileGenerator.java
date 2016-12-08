package com.gelvt.stl2png.povray;

import com.gelvt.stl2png.Converter;
import com.gelvt.stl2png.model.Coordinate;
import com.gelvt.stl2png.model.Polygon;

import java.io.*;

/**
 * Created by elvin on 16-12-6.
 */
public class TemplateBasedPOVFileGenerator extends POVFileGenerator {
    private String templateFilePath;
    private boolean isInitialized = false;
    private BufferedWriter streamWriter;
    private BufferedReader streamReader;

    public TemplateBasedPOVFileGenerator(String povFilePath, String templateFilePath) {
        super(povFilePath);
        this.templateFilePath = templateFilePath;
    }

    public void addPolygon(Polygon polygon) throws IOException {
        streamWriter.write("triangle {");
        streamWriter.newLine();
        for(int i = 0; i < 3; i++){
            Coordinate coordinate = polygon.getVertices()[i];
            streamWriter.write("<" + String.format("%d", (int)coordinate.getX()) +
                    ", " + String.format("%d", (int)coordinate.getY()) +
                    ", " + String.format("%d", (int)coordinate.getZ()) +
                    '>');
            if (i != 2){
                streamWriter.write(",");
                streamWriter.newLine();
            }else{
                streamWriter.write(" }");
                streamWriter.newLine();
            }
        }
    }

    public void init() throws IOException{
        if (isInitialized){
            return;
        }
        streamWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(this.getPovFilePath())));

        File tpFile = new File(this.templateFilePath);
        if(tpFile.exists()){
            streamReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(
                            this.templateFilePath)));
        }else{
            InputStream is = TemplateBasedPOVFileGenerator.class.getClassLoader()
                    .getResourceAsStream(Converter.DEFAULT_TEMPLATE_FILE_PATH);
            if (is == null){
                throw new FileNotFoundException("pov template file not found in jar.");
            }
            streamReader = new BufferedReader(
                    new InputStreamReader(is));
        }

        if (streamReader == null){
            throw new FileNotFoundException("pov template file not found.");
        }

        String tmplLine;
        while (null != (tmplLine = streamReader.readLine())){
            if (!"{{PLACEHOLDER}}".equals(tmplLine)){
                streamWriter.write(tmplLine);
                streamWriter.newLine();
            }else{
                break;
            }
        }
        streamWriter.flush();
        isInitialized = true;
    }

    public void close() throws IOException{
        if (streamReader != null && streamWriter != null){
            String tmplLine;
            while (null != (tmplLine = streamReader.readLine())){
                streamWriter.write(tmplLine);
                streamWriter.newLine();
            }
            streamWriter.flush();
        }

        if (streamReader != null){
            streamReader.close();
            streamReader = null;
        }
        if (streamWriter != null){
            streamWriter.close();
            streamWriter = null;
        }
    }
}
