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

import java.util.ArrayList;
import java.util.List;

import com.espringtran.compressor4j.bean.CompressionType;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public class Constants {
    
    // Delete Source
    public static class SOURCE {
        public static final boolean DELETE = true;
        public static final boolean KEEP = false;
    }
    
    // Allow file types
    public static class FILE_TYPES_ALLOW {
        public static final List<CompressionType> COMPRESSING = new ArrayList<CompressionType>();
        public static final List<CompressionType> DECOMPRESSING = new ArrayList<CompressionType>();
        static {
            // Compressing
            COMPRESSING.add(CompressionType.ZIP);
            COMPRESSING.add(CompressionType.GZ);
            COMPRESSING.add(CompressionType.TAR);
            COMPRESSING.add(CompressionType.TAR_GZ);
            COMPRESSING.add(CompressionType.JAR);
            COMPRESSING.add(CompressionType.BZ2);
            COMPRESSING.add(CompressionType.TAR_BZ2);
            COMPRESSING.add(CompressionType.AR);
            COMPRESSING.add(CompressionType.CPIO);
            COMPRESSING.add(CompressionType.XZ);
            // Deompressing
            DECOMPRESSING.add(CompressionType.ZIP);
            DECOMPRESSING.add(CompressionType.GZ);
            DECOMPRESSING.add(CompressionType.TAR);
            DECOMPRESSING.add(CompressionType.TAR_GZ);
            DECOMPRESSING.add(CompressionType.JAR);
            DECOMPRESSING.add(CompressionType.BZ2);
            DECOMPRESSING.add(CompressionType.TAR_BZ2);
            DECOMPRESSING.add(CompressionType.AR);
            DECOMPRESSING.add(CompressionType.CPIO);
//            DECOMPRESSING.add(CompressionType.LZMA);
            DECOMPRESSING.add(CompressionType.SEVENZIP);
            DECOMPRESSING.add(CompressionType.XZ);
            DECOMPRESSING.add(CompressionType.RAR);
        }
    }
    
}
