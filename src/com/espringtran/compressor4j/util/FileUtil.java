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
package com.espringtran.compressor4j.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.espringtran.compressor4j.compressor.FileCompressor;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public class FileUtil {
    
    /**
     * Get name of file
     * 
     * @param filePath
     *            file path includes file name
     * @return
     */
    public static String getFileName(String filePath) {
        if (filePath == null) {
            return new String();
        }
        File file = new File(filePath);
        return file.getName();
    }
    
    /**
     * Get path of file's folder
     * 
     * @param filePath
     *            file path includes file name
     * @return
     */
    public static String getFolderPath(String filePath) {
        if (filePath == null) {
            return new String();
        }
        File file = new File(filePath);
        return file.getParentFile().getPath();
    }
    
    /**
     * Get extension of file
     * 
     * @param fileName
     *            file name
     * @return
     */
    public static String getExtension(String fileName) {
        if (fileName == null) {
            return new String();
        }
        int dotIdx = fileName.lastIndexOf(".");
        if (dotIdx <= 0) {
            return new String();
        }
        return fileName.substring(dotIdx + 1, fileName.length()).toLowerCase();
    }
    
    /**
     * Check file has extension
     * 
     * @param fileName
     *            file name
     * @param extension
     *            file extension
     * @return
     */
    public static boolean isExtension(String fileName, String extension) {
        if (fileName == null || extension == null) {
            return false;
        }
        fileName = fileName.trim().toLowerCase();
        extension = "." + extension.trim().toLowerCase();
        int idx = fileName.lastIndexOf(extension);
        if (idx <= 0) {
            return false;
        }
        return fileName.substring(idx, fileName.length()).equalsIgnoreCase(
                extension);
    }
    
    /**
     * Get file name without extension
     * 
     * @param fileName
     *            file name
     * @return
     */
    public static String getFileNameWithouExtension(String fileName) {
        if (fileName == null) {
            return new String();
        }
        int dotIdx = fileName.lastIndexOf(".");
        if (dotIdx <= 0) {
            return fileName;
        }
        return fileName.substring(0, dotIdx);
    }
    
    /**
     * Get safe file path
     * 
     * @param path
     *            file path
     * @return
     */
    public static String getSafePath(String path) {
        if (path == null)
            return null;
        return path.replace("\\", "/").replace("//", "/");
    }
    
    /**
     * Convert from file to byte array data
     * 
     * @param filePath
     *            input file path
     * @return
     * @throws Exception
     */
    public static byte[] convertFileToByte(String filePath) throws Exception {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] result = null;
        try {
            byte[] fetchByte = new byte[1024];
            int readByte = fis.read(fetchByte);
            while (readByte != -1) {
                baos.write(fetchByte, 0, readByte);
                readByte = fis.read(fetchByte);
            }
            result = baos.toByteArray();
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on convert file to byte", e);
        } finally {
            baos.close();
            fis.close();
        }
        return result;
    }
    
    /**
     * Convert from byte array data to file
     * 
     * @param filePath
     *            path of created file after convert
     * @param data
     *            byte array content converted to file
     * @throws Exception
     */
    public static void convertByteToFile(String filePath, byte[] data)
            throws Exception {
        String folderPath = getFolderPath(filePath);
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(filePath);
        FileOutputStream fo = new FileOutputStream(file);
        try {
            fo.write(data);
            fo.flush();
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on convert byte to file", e);
        } finally {
            fo.close();
        }
    }
    
    /**
     * Delete source file
     * 
     * @param filePath
     *            file path includes file name
     * @throws Exception
     */
    public static void delete(String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
    
    /**
     * Delete source file and parent folder if empty
     * 
     * @param filePath
     *            file path includes file name
     * @throws Exception
     */
    public static void deleteParent(String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        File parent = file.getParentFile();
        while (parent.exists() && parent.isDirectory()
                && parent.listFiles().length == 0) {
            parent.delete();
            parent = parent.getParentFile();
        }
    }
    
}
