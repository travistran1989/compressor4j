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

import java.io.Serializable;

import com.espringtran.compressor4j.util.FileUtil;

/**
 * 
 * @author E-Spring Tran
 *
 */
public class BinaryFile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String desPath;
    private String srcPath;
    private byte[] data;
    private long actualSize = 0;
    private long compressedSize = 0;

    /**
     * Initialize BinaryFile object
     * 
     * @param desPath
     *            destination path includes file name
     * @param data
     *            byte data
     */
    public BinaryFile(String desPath, byte[] data) {
        desPath = FileUtil.getSafePath(desPath);
        this.desPath = FileUtil.getSafePath(desPath);
        this.data = data;
        this.actualSize = data.length;
    }

    /**
     * Initialize BinaryFile object
     * 
     * @param srcPath
     *            source path includes file name
     * @param desPath
     *            destination path includes file name
     * @param deleteSrc
     *            delete source after convert to binary file or not
     * @throws Exception
     */
    public BinaryFile(String srcPath, String desPath, boolean deleteSrc)
            throws Exception {
        srcPath = FileUtil.getSafePath(srcPath);
        desPath = FileUtil.getSafePath(desPath);
        this.srcPath = srcPath;
        this.desPath = desPath;
        this.data = FileUtil.convertFileToByte(srcPath);
        this.actualSize = data.length;
        if (deleteSrc) {
            FileUtil.delete(srcPath);
        }
    }

    /**
     * Create new instance of BinaryFile from file
     * 
     * @param srcPath
     *            source path includes file name
     * @param desPath
     *            destination path includes file name
     * @param deleteSrc
     *            delete source after convert to binary file or not
     * @throws Exception
     */
    public static BinaryFile newInstance(String srcPath, String desPath,
            boolean deleteSrc) throws Exception {
        return new BinaryFile(srcPath, desPath, deleteSrc);
    }

    /*
     * Getter-Setter
     */
    public String getDesPath() {
        return desPath;
    }

    public void setDesPath(String desPath) {
        this.desPath = desPath;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getActualSize() {
        return actualSize;
    }

    public void setActualSize(long actualSize) {
        this.actualSize = actualSize;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }
}
