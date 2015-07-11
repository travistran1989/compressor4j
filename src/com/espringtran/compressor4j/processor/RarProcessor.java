/*
 * Copyright (C) 2013-2015 E-Spring Tran
 * 
 *             https://espringtran.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.espringtran.compressor4j.processor;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.espringtran.compressor4j.bean.BinaryFile;
import com.espringtran.compressor4j.compressor.FileCompressor;
import com.espringtran.compressor4j.util.FileUtil;
import com.espringtran.compressor4j.util.LogUtil;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public class RarProcessor implements CompressProcessor {
    
    /**
     * Compress data
     * 
     * @param fileCompressor
     *            FileCompressor object
     * @return
     * @throws Exception
     */
    @Override
    public byte[] compressData(FileCompressor fileCompressor) throws Exception {
        return null;
    }
    
    /**
     * Read from compressed file
     * 
     * @param srcPath
     *            path of compressed file
     * @param fileCompressor
     *            FileCompressor object
     * @throws Exception
     */
    @Override
    public void read(String srcPath, FileCompressor fileCompressor)
            throws Exception {
        long t1 = System.currentTimeMillis();
        byte[] data = FileUtil.convertFileToByte(srcPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Archive arch = new Archive(new File(srcPath));
        try {
            if (arch.isEncrypted()) {
                FileCompressor.LOGGER.error(fileCompressor.hashCode()
                        + " Archive is encrypted. Cannot read <" + srcPath
                        + ">");
                return;
            }
            FileHeader entry = null;
            while (true) {
                entry = arch.nextFileHeader();
                if (entry == null)
                    break;
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.isUnicode() ? entry.getFileNameW() : entry
                        .getFileNameString();
                if (entry.isEncrypted()) {
                    FileCompressor.LOGGER.error(fileCompressor.hashCode()
                            + " File is encrypted. Cannot read <" + name + ">");
                    continue;
                }
                long t2 = System.currentTimeMillis();
                baos = new ByteArrayOutputStream();
                arch.extractFile(entry, baos);
                BinaryFile binaryFile = new BinaryFile(name, baos.toByteArray());
                fileCompressor.addBinaryFile(binaryFile);
                LogUtil.createAddFileLog(fileCompressor, binaryFile, t2,
                        System.currentTimeMillis());
            }
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on get compressor file", e);
        } finally {
            arch.close();
            baos.close();
        }
        LogUtil.createReadLog(fileCompressor, srcPath, data.length, t1,
                System.currentTimeMillis());
    }
    
}
