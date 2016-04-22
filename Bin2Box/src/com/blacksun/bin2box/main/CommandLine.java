/*
 *   (c) 2010-2016 Ruan K. F <ruan.klein@gmail.com>
 *   All Rights Reserved.
 *   This file is part of Bin2Box.
 *
 *   Bin2Box is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Bin2Box is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.blacksun.bin2box.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.blacksun.bin2box.lib.SBGConverter;
import com.blacksun.bin2box.lib.SoftwareInfo;
import com.blacksun.bin2box.lib.OperatingSystem;

/**
 *
 * @author ruan
 */
public class CommandLine {
    
    private final String[] args;
    private SBGConverter sbgConverter;
    
    public CommandLine(String[] args) {
        this.args = args;
    }
    
    private void showHelp() {
        System.out.println("Usage: "+SoftwareInfo.PROG_NAME+" [Options] <inputfile.sbg> ...");
        System.out.println("Available options:\n");
	System.out.println("\t--monaural\tset on monaural beats");
	System.out.println("\t--output=DIR\tspecify directory to output");
	System.out.println("\t--help\tprint this help-text");
	System.out.println("\t--version\tprint program version");
    }
    
    public void processCommandLine() throws IOException {
      
      List<String> queue = new ArrayList<>();
      String outputDir, outputFile;
      boolean optM, optH, optV, optO;
      
      outputDir = outputFile = null;
      optM = optH = optV = optO = false;
      
      for(String arg : args) {
          
          if(arg.charAt(0) == '-' && arg.charAt(1) == '-') {
              if(arg.equals("--help")) {
                  optH = true;
                  break;
              }
              else if (arg.equals("--monaural")) {
                  optM = true;
              }
              else if (arg.matches("^--output=[^\\s+].*$")) {
                  optO = true;
                  String[] parts = arg.split("=");
                  outputDir = parts[1];
                  
                  if((OperatingSystem.isLinux() || OperatingSystem.isOSX()) && outputDir.charAt(0) == '~')
                      outputDir = System.getProperty("user.home") + outputDir.replace("~", "") + "/";
              }
              else if (arg.equals("--version")) {
                  optV = true;
                  break;
              }
              else {
                  System.err.println("Error :: invalid option: "+arg);
                  return;
              }
          }
          else if (arg.endsWith(".sbg") || arg.endsWith(".SBG")) {
              queue.add(arg);
          }
          else {
              showHelp();
              return;
          }
      }
      
      
      if(optH) { showHelp(); return; }
      if(optV) { showVersion(); return; }
      
      if(optO) {
          
          File dir = new File(outputDir);
              
          if(!dir.isDirectory()) {
            System.err.println("Error :: invalid directory: "+outputDir);
            return;
          }
              
          if(!dir.canWrite()) {
            System.err.println("Error :: permission denied: "+outputDir);
            return;
          }
          
          
      }
      
      for(String sbgFile : queue) {
          
          if(!new File(sbgFile).canRead()) {
              System.err.println("Error :: permission denied for '"+sbgFile+"'");
              continue;
          }
          
          if(OperatingSystem.isWindows())
            sbgFile = sbgFile.replace("\\", "/");
          
          String[] parts = sbgFile.split("/");
          
          if(optO) {
              if(sbgFile.endsWith(".sbg")) {
                  
                  if(OperatingSystem.isWindows())
                      outputDir = outputDir.replace("\\", "/");
                  
                  outputFile = outputDir + "/" + parts[parts.length-1].replaceAll(".sbg$", optM ? "_mon.sbg" : "_box.sbg");
              }
              else if(sbgFile.endsWith(".SBG")) {
                  
                  if(OperatingSystem.isWindows())
                      outputDir = outputDir.replace("\\", "/");
                  
                  outputFile = outputDir + "/" + parts[parts.length-1].replaceAll(".SBG$", optM ? "_mon.SBG" : "_box.SBG");
              }
          }
          else {
              if(sbgFile.endsWith(".sbg")) {
                  
                  if(OperatingSystem.isWindows())
                      outputFile = new File(sbgFile).getParent().replace("\\", "/");
                  else
                      outputFile = new File(sbgFile).getParent();
                  
                  outputFile +=  "/" + parts[parts.length-1].replaceAll(".sbg$", optM ? "_mon.sbg" : "_box.sbg");
              }
              else if(sbgFile.endsWith(".SBG")) {
                  
                  if(OperatingSystem.isWindows())
                      outputFile = new File(sbgFile).getParent().replace("\\", "/");
                  else
                      outputFile = new File(sbgFile).getParent();
                  
                  outputFile += "/" + parts[parts.length-1].replaceAll(".SBG$", optM ? "_mon.SBG" : "_box.SBG");
              }
          }
          
          sbgConverter = new SBGConverter(sbgFile, outputFile);
          sbgConverter.setMonauralBeat(optM);
          System.out.println("Converting :: '"+sbgFile+"' to "+ (optM ? "Monaural Beat" : "Harmonic Box"));
          sbgConverter.convertSbagenFile();
      }
      
    }

    private void showVersion() {
        System.out.println(
                        "\n\t"+SoftwareInfo.PROG_NAME+", Version " + SoftwareInfo.VERSION + "\n" +
                        "\tCopyright (C) 2010-2016 "+SoftwareInfo.AUTHOR+" <"+SoftwareInfo.EMAIL + ">\n\n" +
                        "\tThis program is free software: you can redistribute it and/or modify\n" +
                        "\tit under the terms of the GNU General Public License as published by\n" +
                        "\tthe Free Software Foundation, either version 3 of the License, or\n" +
                        "\t(at your option) any later version.\n" +
                        "\n" +
                        "\tThis program is distributed in the hope that it will be useful,\n" +
                        "\tbut WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                        "\tMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                        "\tGNU General Public License for more details.\n" +
                        "\n" +
                        "\tYou should have received a copy of the GNU General Public License\n" +
                        "\talong with this program.  If not, see <http://www.gnu.org/licenses/>."
        );
    }
    
}
