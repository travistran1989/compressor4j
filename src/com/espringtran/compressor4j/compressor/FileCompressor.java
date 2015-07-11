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
package com.espringtran.compressor4j.compressor;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.espringtran.compressor4j.bean.BinaryFile;
import com.espringtran.compressor4j.bean.CompressionLevel;
import com.espringtran.compressor4j.bean.CompressionType;
import com.espringtran.compressor4j.processor.CompressProcessor;
import com.espringtran.compressor4j.util.FileUtil;
import com.espringtran.compressor4j.util.LogUtil;
import com.espringtran.compressor4j.util.Constants.FILE_TYPES_ALLOW;
import com.espringtran.compressor4j.util.Constants.SOURCE;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public class FileCompressor implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Logger LOGGER = Logger.getLogger("log.compressor4j");

    private Map<String, BinaryFile> mapBinaryFile = new HashMap<String, BinaryFile>();
    private CompressionType type = null;
    private CompressionLevel level = null;
    private long actualSize = 0;
    private String compressedPath;
    private long compressedSize = -1;
    private CompressionType compressedType = null;
    private String comment;

    /**
     * Create new FileCompressor
     */
    public FileCompressor() {
        LogUtil.createFileLog(this);
    }

    /*
     * TODO --------------------APIs--------------------
     */
    /**
     * Validate before compressing
     * 
     * @throws Exception
     */
    public void validateCompress() throws Exception {
        validateCompressedPath(compressedPath);
        validateType(type);
        validateAllowCompressing(type);
        validateLevel(type, level);
        validateMapFile(mapBinaryFile);
    }

    /**
     * Compress file
     * 
     * @throws Exception
     */
    public void compress() throws Exception {
        validateCompress();
        long t1 = System.currentTimeMillis();
        LogUtil.createStartCompressingLog(this);
        CompressProcessor compressProcessor = getCompressProcessor(type);
        byte[] compressedData = compressProcessor.compressData(this);
        FileUtil.convertByteToFile(compressedPath, compressedData);
        compressedSize = compressedData.length;
        compressedType = type;
        LogUtil.createFinishCompressingLog(this, t1, System.currentTimeMillis());
    }

    /**
     * Compress file and delete all sources
     * 
     * @throws Exception
     */
    public void compressAndDelete() throws Exception {
        compress();
        deleteAllSources();
    }

    /**
     * Create new FileCompressor from compressed file
     * 
     * @param compressedPath
     *            path of compressed file including name
     * @return
     */
    public static FileCompressor read(String compressedPath) throws Exception {
        return read(compressedPath, getFileType(compressedPath));
    }

    /**
     * Create new FileCompressor from compressed file
     * 
     * @param compressedPath
     *            path of compressed file including name
     * @param type
     *            compressed type
     * @return
     */
    public static FileCompressor read(String compressedPath,
            CompressionType type) throws Exception {
        compressedPath = FileUtil.getSafePath(compressedPath);
        validateCompressedPath(compressedPath);
        validateType(type);
        FileCompressor fileCompressor = new FileCompressor();
        fileCompressor.setCompressedPath(compressedPath);
        fileCompressor.setType(type);
        try {
            getCompressProcessor(type).read(compressedPath, fileCompressor);
            LogUtil.createFileLog(fileCompressor);
            return fileCompressor;
        } catch (Exception e) {
            LOGGER.error("Error on reading compressed file", e);
        }
        return null;
    }

    /**
     * Create new FileCompressor from compressed file and delete it
     * 
     * @param compressedPath
     *            path of compressed file including name
     * @return
     */
    public static FileCompressor readAndDelete(String compressedPath)
            throws Exception {
        return readAndDelete(compressedPath, getFileType(compressedPath));
    }

    /**
     * Create new FileCompressor from compressed file and delete it
     * 
     * @param compressedPath
     *            path of compressed file including name
     * @param type
     *            compressed type
     * @return
     */
    public static FileCompressor readAndDelete(String compressedPath,
            CompressionType type) throws Exception {
        compressedPath = FileUtil.getSafePath(compressedPath);
        FileCompressor fileCompressor = read(compressedPath, type);
        try {
            FileUtil.delete(compressedPath);
        } catch (Exception e) {
            LogUtil.createFileNotFoundLog(
                    "Delete after reading compressed file...", fileCompressor,
                    compressedPath);
        }
        return fileCompressor;
    }

    /**
     * Decompress
     * 
     * @param decompressedPath
     *            path of decompressed folder
     * @throws Exception
     */
    public void decompress(String decompressedPath) throws Exception {
        long t1 = System.currentTimeMillis();
        LogUtil.createStartDecompressingLog(this);
        decompressedPath = FileUtil.getSafePath(decompressedPath);
        File file = new File(decompressedPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (BinaryFile binFile : this.getMapBinaryFile().values()) {
            FileUtil.convertByteToFile(
                    FileUtil.getSafePath(decompressedPath + "/"
                            + binFile.getDesPath()), binFile.getData());
        }
        LogUtil.createFinishDecompressingLog(this, t1,
                System.currentTimeMillis());
    }

    /*
     * TODO --------------------Base API--------------------
     */
    /**
     * Add file or folder
     * 
     * @param srcPath
     *            source path or file or folder
     * @param desPath
     *            destination path of file or folder
     * @throws Exception
     */
    public void add(String srcPath, String desPath) throws Exception {
        File srcFile = new File(srcPath);
        if (!srcFile.exists())
            return;
        File[] files;
        if (srcFile.isFile()) {
            addFile(srcPath, desPath);
        } else {
            files = srcFile.listFiles();
            for (File file : files) {
                add(file.getPath(), desPath + "/" + file.getName());
            }
        }
    }

    /**
     * Add and delete file or folder
     * 
     * @param srcPath
     *            source path or file or folder
     * @param desPath
     *            destination path of file or folder
     * @throws Exception
     */
    public void addAndDelete(String srcPath, String desPath) throws Exception {
        File srcFile = new File(srcPath);
        File[] listFiles;
        if (srcFile.exists() && srcFile.isFile()) {
            addFileAndDelete(srcPath, desPath);
        } else if (srcFile.exists() && srcFile.isDirectory()) {
            listFiles = srcFile.listFiles();
            for (File file : listFiles) {
                addAndDelete(file.getPath(), desPath + "/" + file.getName());
            }
        }
        if (srcFile.exists()) {
            srcFile.delete();
        }
    }

    /**
     * Delete all sources
     * 
     * @throws Exception
     */
    public void deleteAllSources() throws Exception {
        long t1 = System.currentTimeMillis();
        for (BinaryFile binFile : mapBinaryFile.values()) {
            try {
                FileUtil.deleteParent(binFile.getSrcPath());
            } catch (Exception e) {
                LogUtil.createFileNotFoundLog("Delete all sources...", this,
                        binFile.getSrcPath());
            }
        }
        LogUtil.createDeleteAllSourcesLog(this, t1, System.currentTimeMillis());
    }

    /**
     * Remove file
     * 
     * @param srcPath
     *            path of binary file
     * @throws Exception
     */
    public void remove(String srcPath) throws Exception {
        removeFile(srcPath);
    }

    /**
     * Extract file
     * 
     * @param srcPath
     *            path of file in compressed file
     * @param desPath
     *            path to extract folder
     * @throws Exception
     */
    public void extract(String srcPath, String desPath) throws Exception {
        long t1 = System.currentTimeMillis();
        srcPath = FileUtil.getSafePath(srcPath);
        desPath = FileUtil.getSafePath(desPath);
        File file = new File(desPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        BinaryFile binaryFile = getBinaryFile(srcPath);
        if (binaryFile == null) {
            LogUtil.createFileNotFoundLog("Extract file...", this, srcPath);
            return;
        }
        FileUtil.convertByteToFile(
                FileUtil.getSafePath(desPath + "/" + binaryFile.getDesPath()),
                binaryFile.getData());
        LogUtil.createExtractLog(this, binaryFile, t1,
                System.currentTimeMillis());
    }

    /*
     * TODO --------------------Base functions--------------------
     */
    /**
     * Add BinaryFile
     * 
     * @param binaryFile
     *            BinaryFile object
     */
    public void addBinaryFile(BinaryFile binaryFile) {
        mapBinaryFile.put(binaryFile.getDesPath(), binaryFile);
        this.actualSize += binaryFile.getActualSize();
    }

    /**
     * Remove BinaryFile
     * 
     * @param binaryFile
     *            BinaryFile object
     */
    public void removeBinaryFile(BinaryFile binaryFile) {
        mapBinaryFile.remove(binaryFile.getDesPath());
        this.actualSize -= binaryFile.getActualSize();
    }

    /**
     * Add file
     * 
     * @param srcPath
     *            source file path
     * @param desPath
     *            destination file path
     * @param delSrc
     *            delete source after adding
     * @throws Exception
     */
    public void addFile(String srcPath, String desPath, boolean delSrc)
            throws Exception {
        long t1 = System.currentTimeMillis();
        BinaryFile binaryFile = BinaryFile
                .newInstance(srcPath, desPath, delSrc);
        addBinaryFile(binaryFile);
        LogUtil.createAddFileLog(this, binaryFile, t1,
                System.currentTimeMillis());
    }

    /**
     * Add file
     * 
     * @param srcPath
     *            source file path
     * @param desPath
     *            destination file path
     * @throws Exception
     */
    public void addFile(String srcPath, String desPath) throws Exception {
        addFile(srcPath, desPath, SOURCE.KEEP);
    }

    /**
     * Add file and delete source
     * 
     * @param srcPath
     *            source file path
     * @param desPath
     *            destination file path
     * @throws Exception
     */
    public void addFileAndDelete(String srcPath, String desPath)
            throws Exception {
        addFile(srcPath, desPath, SOURCE.DELETE);
    }

    /**
     * Remove file
     * 
     * @param srcPath
     *            path of binary file
     * @throws Exception
     */
    public void removeFile(String srcPath) throws Exception {
        long t1 = System.currentTimeMillis();
        srcPath = FileUtil.getSafePath(srcPath);
        BinaryFile binaryFile = mapBinaryFile.get(srcPath);
        if (binaryFile == null) {
            LogUtil.createFileNotFoundLog("Remove file...", this, srcPath);
            LogUtil.createRemoveFileLog(this, binaryFile, t1,
                    System.currentTimeMillis());
            return;
        }
        removeBinaryFile(binaryFile);
        LogUtil.createRemoveFileLog(this, binaryFile, t1,
                System.currentTimeMillis());
    }

    /*
     * TODO --------------------Static methods--------------------
     */
    /**
     * Get file type
     * 
     * @param compressedPath
     *            path of compressed file
     * @return
     */
    public static CompressionType getFileType(String compressedPath)
            throws Exception {
        compressedPath = FileUtil.getSafePath(compressedPath);
        CompressionType type = null;
        CompressionType[] compressorTypes = CompressionType.values();
        for (CompressionType compressorType : compressorTypes) {
            if (FileUtil.isExtension(compressedPath,
                    compressorType.getExtension())) {
                type = compressorType;
                break;
            }
        }
        if (type == null) {
            throw new IllegalArgumentException(
                    "File type not supported. File: " + compressedPath);
        }
        return type;
    }

    /**
     * Check file type allowed for compressing
     * 
     * @param type
     *            compressed type
     * @throws Exception
     */
    public static void validateAllowCompressing(CompressionType type)
            throws Exception {
        if (FILE_TYPES_ALLOW.COMPRESSING.contains(type))
            return;
        throw new IllegalArgumentException("Type not support for compressing.");
    }

    /**
     * Check file type allowed for decompressing
     * 
     * @param type
     *            compressed type
     * @throws Exception
     */
    public static void validateAllowDecompressing(CompressionType type)
            throws Exception {
        if (FILE_TYPES_ALLOW.DECOMPRESSING.contains(type))
            return;
        throw new IllegalArgumentException(
                "Type not support for decompressing.");
    }

    /**
     * Get CompressProcessor
     * 
     * @param type
     *            compressed type
     * @return
     * @throws Exception
     */
    public static CompressProcessor getCompressProcessor(CompressionType type)
            throws Exception {
        CompressionType[] compressorTypes = CompressionType.values();
        for (CompressionType compressorType : compressorTypes) {
            if (compressorType.equals(type)) {
                return compressorType.getCompressProcessor();
            }
        }
        throw new IllegalArgumentException("Compress Processor not found.");
    }

    /**
     * Validate type
     * 
     * @param type
     *            compressed type
     * @throws Exception
     */
    public static void validateType(CompressionType type) throws Exception {
        if (type == null) {
            throw new IllegalArgumentException("Type not set.");
        }
        CompressionType[] compressorTypes = CompressionType.values();
        for (CompressionType compressorType : compressorTypes) {
            if (compressorType.equals(type)) {
                return;
            }
        }
        throw new IllegalArgumentException("Invalid type.");
    }

    /**
     * Validate level
     * 
     * @param type
     *            compressed type
     * @param level
     *            compressed level
     * @throws Exception
     */
    public static void validateLevel(CompressionType type,
            CompressionLevel level) throws Exception {
        validateType(type);
        if (level == null) {
            throw new IllegalArgumentException("Level not set.");
        }
        int lv = level.getValue();
        if (CompressionType.ZIP.equals(type)) {
            if ((lv < -1) || (lv > 9)) {
                throw new IllegalArgumentException(
                        "Invalid compression level: " + level);
            }
        } else if (CompressionType.JAR.equals(type)) {
            if ((lv < -1) || (lv > 9)) {
                throw new IllegalArgumentException(
                        "Invalid compression level: " + level);
            }
        } else if (CompressionType.GZ.equals(type)) {
            if ((lv < -1) || (lv > 9)) {
                throw new IllegalArgumentException(
                        "Invalid compression level: " + level);
            }
        } else if (CompressionType.BZ2.equals(type)) {
            if ((lv < -1) || (lv > 9)) {
                throw new IllegalArgumentException(
                        "Invalid compression level: " + level);
            }
        }
    }

    /**
     * Validate compressed path
     * 
     * @param compressedPath
     *            path of compressed file
     * @throws Exception
     */
    public static void validateCompressedPath(String compressedPath)
            throws Exception {
        if (compressedPath == null || compressedPath.isEmpty()) {
            throw new IllegalArgumentException("Compressed path not set.");
        }
    }

    /**
     * Validate map BinaryFile
     * 
     * @param mapBinaryFile
     *            map of BinaryFiles
     * @throws Exception
     */
    public static void validateMapFile(Map<String, BinaryFile> mapBinaryFile)
            throws Exception {
        if (mapBinaryFile == null || mapBinaryFile.isEmpty()) {
            throw new IllegalArgumentException("No file found.");
        }
    }

    /*
     * TODO --------------------Getter Setter--------------------
     */
    public String getCompressedPath() {
        return compressedPath;
    }

    public void setCompressedPath(String compressedPath) {
        this.compressedPath = compressedPath;
    }

    public Map<String, BinaryFile> getMapBinaryFile() {
        return mapBinaryFile;
    }

    public CompressionType getType() {
        return type;
    }

    public void setType(CompressionType type) {
        this.type = type;
    }

    public CompressionLevel getLevel() {
        return level;
    }

    public void setLevel(CompressionLevel level) {
        this.level = level;
    }

    public BinaryFile getBinaryFile(String fileName) {
        return mapBinaryFile.get(fileName);
    }

    public long getActualSize() {
        return actualSize;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public CompressionType getCompressedType() {
        return compressedType;
    }

    public String getCompressedTypeName() {
        if (compressedType == null)
            return "";
        return compressedType.getName();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
