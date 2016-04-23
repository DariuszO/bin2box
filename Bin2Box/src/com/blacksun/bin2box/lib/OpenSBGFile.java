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

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Open sbg file or dir and return the local
 * @author ruan
 */
public class OpenSBGFile {

    private final JFileChooser fc;

    public OpenSBGFile() {
        fc = new JFileChooser();
    }

    public String chooseFile() {

        String sbg = null;

        fc.setFileFilter(new FileNameExtensionFilter("SBaGen sequencie file", "sbg"));
        fc.setDialogTitle("Select sbg file");

        if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            sbg = fc.getSelectedFile().getAbsolutePath();

        if(sbg != null)
            if(!sbg.endsWith(".sbg") && !sbg.endsWith(".SBG"))
                sbg = null;

        return sbg;
    }

    public String[] chooseDir() {

        String[] sbg = null;
        File dir = null;

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Select folder with sbg files");

        if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            dir = new File(fc.getSelectedFile().getPath());

        if(dir != null) {

            int i = 0;

            for (File file : dir.listFiles()){
                if(file.isFile() && file.canRead()) {
                    if(file.getAbsolutePath().endsWith(".sbg") || file.getAbsolutePath().endsWith(".SBG"))
                        i++;
                }
            }

            if(i == 0)
                return null;

            int j = 0;
            sbg = new String[i];

            for (File file : dir.listFiles()){
                if(file.isFile() && file.canRead()) {
                    if(file.getAbsolutePath().endsWith(".sbg") || file.getAbsolutePath().endsWith(".SBG"))
                        if(j < i) {
                            sbg[j] = file.getAbsolutePath();
                            j++;
                        }
                }
            }
        }

        return sbg;

    }

    public String chooseOutputDir() {

       String dir = null;
       File file;

       fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       fc.setDialogTitle("Select output folder");


        if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            dir = fc.getSelectedFile().getPath();


       if(dir != null) {
           file = new File(dir);

           if(!file.canRead() || !file.canWrite())
               dir = null;
       }

       return dir;

    }

}
