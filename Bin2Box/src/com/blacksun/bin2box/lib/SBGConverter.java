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
 *   along with Bin2Box.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.blacksun.bin2box.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * Convert sbg files to Monaural or Harmonic Box
 * @author ruan
 */
public class SBGConverter {

    private final File file;
    private final FileReader fileReader;
    private final FileWriter fileWriter;
    private final BufferedReader bufferedReader;

    private final SBGStruct sbgStruct;

    private boolean monauralBeat;

    public SBGConverter(String in, String out) throws FileNotFoundException, IOException {
        fileReader = new FileReader(in);
        bufferedReader = new BufferedReader(fileReader);
        file = new File(out);
        fileWriter = new FileWriter(file.getAbsolutePath());
        sbgStruct = new SBGStruct();
        // force decimal point
        Locale.setDefault(new Locale("en", "US", System.getProperty("os.name")));
    }

    public boolean isMonauralBeat() {
        return monauralBeat;
    }

    public void setMonauralBeat(boolean monauralBeat) {
        this.monauralBeat = monauralBeat;
    }

    public void convertSbagenFile() throws IOException {

        String lineIn, lineOut;
        String[] token;

        int element, noiseCount = 0;
        //int lineCount = 0;

        while((lineIn = bufferedReader.readLine()) != null) {

            //lineCount++;

            if(sbgStruct.isNameDef(lineIn)) {
                token = lineIn.split("\\s+");
               // sbgStruct.setNamedef(token[0].replace(':', ' '));
                lineOut = token[0] + " ";
                fileWriter.write(lineOut);
                for (element = 1; element <= token.length -1; element++) {
                    if(sbgStruct.isBackground(token[element])) {
                        noiseCount++;
                        sbgStruct.setBackground(sbgStruct.extractBackground(token[element]));
                        lineOut = token[element] + " ";
                        fileWriter.write(lineOut);

                        if(element == (token.length-1)) {
                            if(OperatingSystem.isWindows())
                                fileWriter.write("\r\n");
                            else
                                fileWriter.write("\n");
                        }

                        continue;
                    }
                    else if(sbgStruct.isSequencie(token[element])) {
                        sbgStruct.setCarrier(sbgStruct.extractCarrier(token[element]));
                        sbgStruct.setSignal(sbgStruct.extractSignal(token[element]));
                        sbgStruct.setBeat(sbgStruct.extractBeat(token[element]));
                        sbgStruct.setAmplitude(sbgStruct.extractAmplitude(token[element]));
                        // Fix audio clipping
                        fixAmplitude(sbgStruct.getAmplitude(), sbgStruct.getBackground(), noiseCount, token.length-2);

                        lineOut = String.format("%.2f%c%.2f/%.2f ", sbgStruct.getCarrier(), sbgStruct.getSignal(),
                                sbgStruct.getBeat(), sbgStruct.getAmplitude());

                        fileWriter.write(lineOut);

                        switch (sbgStruct.getSignal()) {
                            case '+':
                                sbgStruct.setSignal('-');
                                break;
                            case '-':
                                sbgStruct.setSignal('+');
                                break;
                        }

                        if(monauralBeat)
                            lineOut = String.format("%.2f%c%.2f/%.2f ", sbgStruct.getCarrier(), sbgStruct.getSignal(),
                                sbgStruct.getBeat(), sbgStruct.getAmplitude());
                        else
                            lineOut = String.format("%.2f%c%.2f/%.2f ", sbgStruct.getHarmonicBoxValue(), sbgStruct.getSignal(),
                                sbgStruct.getBeat(), sbgStruct.getAmplitude());

                        fileWriter.write(lineOut);

                        if(element == (token.length-1)) {
                            if(OperatingSystem.isWindows())
                                fileWriter.write("\r\n");
                            else
                                fileWriter.write("\n");
                        }

                        continue;
                    }
                    else {
                        lineOut = token[element] + " ";
                        fileWriter.write(lineOut);
                    }

                    if(element == (token.length-1)) {
                        if(OperatingSystem.isWindows())
                            fileWriter.write("\r\n");
                        else
                            fileWriter.write("\n");
                    }

                }


            }
            else {
                if(OperatingSystem.isWindows())
                    lineOut = lineIn + "\r\n";
                else
                    lineOut = lineIn + "\n";

                fileWriter.write(lineOut);
            }
        }

        closeFile();
    }

    private void fixAmplitude(double x, double y, int a, int b) {
        if(a>0) {
            int z = b-a <= 1 ? 2 : b-a;
            if((y+(x*(z*2)))>100)
                sbgStruct.setAmplitude(x/2);
            a = 0;
        }
        else {
            if((x*(b*2))>100)
                sbgStruct.setAmplitude(x/2);
        }
    }

    private void closeFile() throws IOException {
        if(fileReader != null)
            fileReader.close();
        fileWriter.close();
    }

}
