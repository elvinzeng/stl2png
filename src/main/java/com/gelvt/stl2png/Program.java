package com.gelvt.stl2png;

import org.apache.commons.cli.*;
import java.io.File;
import java.io.IOException;

/**
 * STL转png转换器.
 * Created by elvin on 16-12-6.
 */
public class Program {
    private static Options options = new Options();
    private static CommandLineParser parser = new DefaultParser();
    private static CommandLine cmd = null;

    public static CommandLine getCommand() {
        return cmd;
    }

    static{
        options.addOption("s", true, "STL file path");
        options.addOption("t", true, "target png file path");
        options.addOption("h", false, "help");
        options.addOption("v", false, "show details of transform");
        options.addOption("V", false, "calculate volume and output it");
        options.addOption("S", false, "calculate surface area and output it");
    }

    private static String mkTempFile() throws IOException{
        File temp = File.createTempFile("stl2png_", ".pov");
        temp.deleteOnExit();
        return temp.getAbsolutePath();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            //e.printStackTrace();
            printHelpInfo();
            return;
        }

        if (!cmd.hasOption("s")){
            printHelpInfo();
            return;
        }
        if (!cmd.hasOption("t")){
            printHelpInfo();
            return;
        }
        if(cmd.hasOption("h")){
            printHelpInfo();
            return;
        }
        String stlFilePath = cmd.getOptionValue("s");
        String pngFilePath = cmd.getOptionValue("t");
        Converter converter = new Converter();
        String povFilePath = mkTempFile();
        long t1 = System.currentTimeMillis();
        System.out.println("converter started...");
        System.out.println("STL File path:" + stlFilePath);
        System.out.println("PNG File path:" + pngFilePath);
        System.out.println("########[transforming STL to pov...]#########################");
        converter.stl2pov(stlFilePath, povFilePath);
        System.out.println("########[transform STL to pov finished.]#####################");
        long t2 = System.currentTimeMillis();
        System.out.println("transform STL to pov total takes " + (t2 - t1) + " millisecond");
        System.out.println("########[transforming pov to png...]#########################");
        converter.pov2png(povFilePath, pngFilePath);
        System.out.println("########[transforming pov to png finished.]##################");
        long t3 = System.currentTimeMillis();
        System.out.println("transform pov to png total takes " + (t3 - t2) + " millisecond");
        System.out.println("success");
        System.out.println("transform STL to png total takes " + (t3 - t1) + " millisecond");
    }

    private static void printHelpInfo(){
        System.out.println("usage:");
        System.out.println("\t-s STL file path");
        System.out.println("\t-t target png file path");
        System.out.println("\t-h help");
        System.out.println("\t-v show details of transform");
        System.out.println("\t-V calculate volume and output it");
        System.out.println("\t-S calculate surface area and output it");
        System.out.println("Author: Elvin Zeng");
        System.out.println("project: https://github.com/elvinzeng/stl2png");
    }
}
