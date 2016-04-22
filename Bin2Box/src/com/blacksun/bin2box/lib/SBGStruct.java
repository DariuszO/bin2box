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

package com.blacksun.bin2box.lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Get elements of the sbg file
 * @author ruan
 */
public class SBGStruct {
    
    private String namedef;
    private double background;
    private double carrier;
    private char signal;
    private double beat;
    private double amplitude;
    
    private static Pattern pattern;
    private static Matcher matcher;
    
    /* elements */
    private final String digits;
    private final String spin;
    private final String label;
    private final String sequencie;
    private final String backgroundsequencie;
    
    public SBGStruct() {
        digits = "(\\d+|\\d+\\.\\d+|\\.\\d+)";
        spin = "spin:\\d+\\.\\d+[\\+|\\-]\\d+\\.\\d+|spin:\\d+[\\+|\\-]\\d+|spin:\\d+\\.\\d+[\\+|\\-]\\d+|spin:\\d+[\\+|\\-]\\d+\\.\\d+";
        label = "^([a-zA-Z][\\w-\\S[^:]]+):\\s.*";
        sequencie = "^" + digits + "(\\+|\\-)" + digits + "/" + digits + "$";
        backgroundsequencie = "^(pink|"+spin+"|mix)/" + digits + "$";
    }

    public String getNamedef() {
        return namedef;
    }

    public void setNamedef(String namedef) {
        this.namedef = namedef;
    }

    public double getCarrier() {
        return carrier;
    }

    public void setCarrier(double carrier) {
        this.carrier = carrier;
    }

    public char getSignal() {
        return signal;
    }

    public void setSignal(char signal) {
        this.signal = signal;
    }

    public double getBeat() {
        return beat;
    }

    public void setBeat(double beat) {
        this.beat = beat;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getBackground() {
        return background;
    }

    public void setBackground(double background) {
        this.background = background;
    }
    
    public boolean isNameDef(String element) {
        pattern = Pattern.compile(label);
        matcher = pattern.matcher(element);
        return matcher.matches();
    }
    
    public boolean isBackground(String element) {
        pattern = Pattern.compile(backgroundsequencie);
        matcher = pattern.matcher(element);
        return matcher.matches();
    }
    
    public boolean isSequencie(String element) {
        pattern = Pattern.compile(sequencie);
        matcher = pattern.matcher(element);
        return matcher.matches();
    }
    
    public String extractNameDef(String element) {
        pattern = Pattern.compile(namedef);
        matcher = pattern.matcher(element);
        
        if(matcher.matches())
            return matcher.group(1);
        
        return null;
    }
    
    public double extractCarrier(String element) {
        pattern = Pattern.compile(sequencie);
        matcher = pattern.matcher(element);
        
        if(matcher.matches())
            return Double.parseDouble(matcher.group(1));
        
        return 0;
    }
    
    public char extractSignal(String element) {
        pattern = Pattern.compile(sequencie);
        matcher = pattern.matcher(element);
        
        if(matcher.matches()) {
            String c = matcher.group(2);
            return c.charAt(0);
        }
        
        return 0;
    }
    
    public double extractBeat(String element) {
        pattern = Pattern.compile(sequencie);
        matcher = pattern.matcher(element);
        
        if(matcher.matches())
            return Double.parseDouble(matcher.group(3));
        
        return 0;
    }
    
    public double extractAmplitude(String element) {
        pattern = Pattern.compile(sequencie);
        matcher = pattern.matcher(element);
        
        if(matcher.matches())
            return Double.parseDouble(matcher.group(4));
        
        return 0;
    }
    
    public double extractBackground(String element) {
        pattern = Pattern.compile(backgroundsequencie);
        matcher = pattern.matcher(element);
        
        if(matcher.matches())
            return Double.parseDouble(matcher.group(2));
        
        return 0;
    }
    
    public double getHarmonicBoxValue() {
        return getCarrier() - (getBeat() * 2);
    }
    
}
