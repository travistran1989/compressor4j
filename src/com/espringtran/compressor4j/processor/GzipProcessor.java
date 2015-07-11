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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import com.espringtran.compressor4j.bean.BinaryFile;
import com.espringtran.compressor4j.compressor.FileCompressor;
import com.espringtran.compressor4j.util.FileUtil;
import com.espringtran.compressor4j.util.LogUtil;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public class GzipProcessor implements CompressProcessor {

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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream cos = new GzipCompressorOutputStream(baos);
        ZipOutputStream zos = new ZipOutputStream(cos);
        try {
            zos.setLevel(fileCompressor.getLevel().getValue());
            zos.setMethod(ZipOutputStream.DEFLATED);
            zos.setComment(fileCompressor.getComment());
            for (BinaryFile binaryFile : fileCompressor.getMapBinaryFile()
                    .values()) {
                zos.putNextEntry(new ZipEntry(binaryFile.getDesPath()));
                zos.write(binaryFile.getData());
                zos.closeEntry();
            }
            zos.flush();
            zos.finish();
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on compress data", e);
        } finally {
            zos.close();
            cos.close();
            baos.close();
        }
        return baos.toByteArray();
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
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        GzipCompressorInputStream cis = new GzipCompressorInputStream(bais);
        ZipInputStream zis = new ZipInputStream(cis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int readByte;
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                long t2 = System.currentTimeMillis();
                baos = new ByteArrayOutputStream();
                readByte = zis.read(buffer);
                while (readByte != -1) {
                    baos.write(buffer, 0, readByte);
                    readByte = zis.read(buffer);
                }
                zis.closeEntry();
                BinaryFile binaryFile = new BinaryFile(entry.getName(),
                        baos.toByteArray());
                fileCompressor.addBinaryFile(binaryFile);
                LogUtil.createAddFileLog(fileCompressor, binaryFile, t2,
                        System.currentTimeMillis());
                entry = zis.getNextEntry();
            }
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on get compressor file", e);
        } finally {
            baos.close();
            zis.close();
            cis.close();
            bais.close();
        }
        LogUtil.createReadLog(fileCompressor, srcPath, data.length, t1,
                System.currentTimeMillis());
    }

}
