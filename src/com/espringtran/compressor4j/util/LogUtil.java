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

import com.espringtran.compressor4j.bean.BinaryFile;
import com.espringtran.compressor4j.compressor.FileCompressor;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public class LogUtil {

    public static void createFileLog(FileCompressor fileCompressor) {
        String compressedPath = fileCompressor.getCompressedPath();
        String s = fileCompressor.hashCode()
                + " Created file"
                + ((compressedPath == null || compressedPath.isEmpty()) ? ""
                        : (" <" + compressedPath.trim() + ">"));
        FileCompressor.LOGGER.info(s);
    }

    public static void createStartCompressingLog(FileCompressor fileCompressor) {
        FileCompressor.LOGGER.info(fileCompressor.hashCode()
                + " Start compressing... <"
                + fileCompressor.getCompressedPath() + ">");
    }

    public static void createFinishCompressingLog(
            FileCompressor fileCompressor, long startTime, long endTime) {
        long actualSize = fileCompressor.getActualSize();
        long compressedSize = fileCompressor.getCompressedSize();
        double rate = (actualSize - compressedSize) * 1.0 / actualSize;
        StringBuilder s = new StringBuilder(fileCompressor.hashCode()
                + " Finish compressing <" + fileCompressor.getCompressedPath()
                + ">");
        s.append("\n    Type: ").append(fileCompressor.getCompressedTypeName());
        s.append("\n    Level: ").append(fileCompressor.getLevel());
        s.append("\n    Actual size: ").append(
                FormatterUtil.formatSizeInfo(actualSize));
        s.append("\n    Compressed size: ").append(
                FormatterUtil.formatSizeInfo(compressedSize));
        s.append("\n    Rate: ").append(FormatterUtil.formatPercent(rate));
        s.append("\n    Time: ").append(endTime - startTime).append(" (")
                .append(FormatterUtil.formatTimeInfo(endTime - startTime))
                .append(")");
        FileCompressor.LOGGER.info(s.toString());
    }

    public static void createStartDecompressingLog(FileCompressor fileCompressor) {
        FileCompressor.LOGGER.info(fileCompressor.hashCode()
                + " Start decompressing... <"
                + fileCompressor.getCompressedPath() + ">");
    }

    public static void createFinishDecompressingLog(
            FileCompressor fileCompressor, long startTime, long endTime) {
        FileCompressor.LOGGER.info(fileCompressor.hashCode()
                + " Finish decompressing <"
                + fileCompressor.getCompressedPath() + "> in "
                + (endTime - startTime) + " ("
                + FormatterUtil.formatTimeInfo(endTime - startTime)
                + ") Size: "
                + FormatterUtil.formatSizeInfo(fileCompressor.getActualSize()));
    }

    public static void createDeleteAllSourcesLog(FileCompressor fileCompressor,
            long startTime, long endTime) {
        FileCompressor.LOGGER.info(fileCompressor.hashCode()
                + " Delete all sources in " + (endTime - startTime) + " ("
                + FormatterUtil.formatTimeInfo(endTime - startTime) + ")");
    }

    public static void createAddFileLog(FileCompressor fileCompressor,
            BinaryFile binaryFile, long startTime, long endTime) {
        FileCompressor.LOGGER.info(fileCompressor.hashCode() + " Added file <"
                + binaryFile.getSrcPath() + "> to <" + binaryFile.getDesPath()
                + "> in " + (endTime - startTime) + " ("
                + FormatterUtil.formatTimeInfo(endTime - startTime)
                + ") Size: "
                + FormatterUtil.formatSizeInfo(binaryFile.getActualSize()));
    }

    public static void createRemoveFileLog(FileCompressor fileCompressor,
            BinaryFile binaryFile, long startTime, long endTime) {
        FileCompressor.LOGGER.info(fileCompressor.hashCode()
                + " Removed file <" + binaryFile.getDesPath() + "> in "
                + (endTime - startTime) + " ("
                + FormatterUtil.formatTimeInfo(endTime - startTime)
                + ") Size: "
                + FormatterUtil.formatSizeInfo(binaryFile.getActualSize()));
    }

    public static void createExtractLog(FileCompressor fileCompressor,
            BinaryFile binaryFile, long startTime, long endTime) {
        FileCompressor.LOGGER.info(fileCompressor.hashCode()
                + " Extracted file <" + binaryFile.getDesPath() + "> in "
                + (endTime - startTime) + " ("
                + FormatterUtil.formatTimeInfo(endTime - startTime)
                + ") Size: "
                + FormatterUtil.formatSizeInfo(binaryFile.getActualSize()));
    }

    public static void createReadLog(FileCompressor fileCompressor,
            String srcPath, long size, long startTime, long endTime) {
        FileCompressor.LOGGER.info(fileCompressor.hashCode()
                + " Read from file <" + srcPath + "> in "
                + (endTime - startTime) + " ("
                + FormatterUtil.formatTimeInfo(endTime - startTime)
                + ") Size: " + FormatterUtil.formatSizeInfo(size));
    }

    public static void createFileNotFoundLog(String prefix,
            FileCompressor fileCompressor, String srcPath) {
        FileCompressor.LOGGER.error(fileCompressor.hashCode()
                + (prefix == null ? "" : (" " + prefix)) + " File not found <"
                + srcPath + ">");
    }

    public static void createMemoryLog() {
        StringBuilder s = new StringBuilder();
        s.append("JVM Memory Info");
        s.append("\n    Heap size: ").append(
                FormatterUtil.formatSizeInfo(MemoryUtil.getTotalMemory()));
        s.append("\n    Max heap size: ").append(
                FormatterUtil.formatSizeInfo(MemoryUtil.getMaxMemory()));
        s.append("\n    Free: ").append(
                FormatterUtil.formatSizeInfo(MemoryUtil.getFreeMemory()));
        s.append("\n    Used: ").append(
                FormatterUtil.formatSizeInfo(MemoryUtil.getUsedMemory()));
        FileCompressor.LOGGER.info(s.toString());
    }

    public static void createFileTypeNotSupportLog(String fileName) {
        FileCompressor.LOGGER.error("File type not supported. <" + fileName
                + ">");
    }

}
