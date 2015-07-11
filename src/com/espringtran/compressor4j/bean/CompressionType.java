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
package com.espringtran.compressor4j.bean;

import com.espringtran.compressor4j.processor.ArProcessor;
import com.espringtran.compressor4j.processor.Bzip2Processor;
import com.espringtran.compressor4j.processor.CompressProcessor;
import com.espringtran.compressor4j.processor.CpioProcessor;
import com.espringtran.compressor4j.processor.GzipProcessor;
import com.espringtran.compressor4j.processor.JarProcessor;
import com.espringtran.compressor4j.processor.LzmaProcessor;
import com.espringtran.compressor4j.processor.RarProcessor;
import com.espringtran.compressor4j.processor.SevenZipProcessor;
import com.espringtran.compressor4j.processor.TarBz2Processor;
import com.espringtran.compressor4j.processor.TarGzProcessor;
import com.espringtran.compressor4j.processor.TarProcessor;
import com.espringtran.compressor4j.processor.XzProcessor;
import com.espringtran.compressor4j.processor.ZipProcessor;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public enum CompressionType {

    TAR_GZ("Tape archive with Gzip", "tar.gz", "application/x-gtar", new TarGzProcessor()),
    TAR_BZ2("Tape archive with Bzip2", "tar.bz2", "application/x-gtar", new TarBz2Processor()),
    TAR("Tape archive", "tar", "application/x-tar", new TarProcessor()),
    ZIP("ZIP", "zip", "application/zip", new ZipProcessor()),
    GZ("GZIP", "gz", "application/x-gzip", new GzipProcessor()),
    BZ2("BZIP2", "bz2", "application/x-bzip2", new Bzip2Processor()),
    AR("Unix Archiver", "ar", null, new ArProcessor()),
    CPIO("CPIO", "cpio", "application/x-cpio", new CpioProcessor()),
//    LZMA("LZMA", "lzma", "application/x-lzma", new LzmaProcessor()),
    SEVENZIP("7Z", "7z", "application/x-7z-compressed", new SevenZipProcessor()),
    XZ("XZ", "xz", "application/x-xz", new XzProcessor()),
    RAR("RAR", "rar", "application/x-rar-compressed", new RarProcessor()),
    JAR("JAR", "jar", null, new JarProcessor());

    private String name;
    private String extension;
    private String mimeType;
    private CompressProcessor compressProcessor;

    CompressionType(String name, String extension, String mimeType,
            CompressProcessor compressProcessor) {
        this.name = name;
        this.extension = extension;
        this.mimeType = mimeType;
        this.compressProcessor = compressProcessor;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public CompressProcessor getCompressProcessor() {
        return compressProcessor;
    }
    
}
