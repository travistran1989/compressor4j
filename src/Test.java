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
import com.espringtran.compressor4j.bean.CompressionLevel;
import com.espringtran.compressor4j.bean.CompressionType;
import com.espringtran.compressor4j.bean.ZipLevel;
import com.espringtran.compressor4j.compressor.FileCompressor;
import com.espringtran.compressor4j.util.LogUtil;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public class Test {

    private static final String SRC = "test/src/";
    private static final String DES = "test/des/";
    private static final String EXTRACT = "test/extract/";
    private static final CompressionType TYPE = CompressionType.JAR;
    private static final CompressionLevel LEVEL = ZipLevel.NORMAL;
    private static final String PATH_SRC = DES + "test." + TYPE.getExtension();
    private static final String PATH_EXTRACT = EXTRACT + TYPE.getExtension()
            + "/";

    public static void main(String[] args) throws Exception {
        LogUtil.createMemoryLog();
        write();
        read();
        LogUtil.createMemoryLog();
    }

    public static void write() throws Exception {
        try {
            FileCompressor fileCompressor = new FileCompressor();
            String[] files = new String[] { "IMG_0332.JPG" };
            for (String file : files) {
                fileCompressor.add(SRC + file, file);
            }
            fileCompressor.setType(TYPE);
            fileCompressor.setLevel(LEVEL);
            fileCompressor.setCompressedPath(PATH_SRC);
            fileCompressor.compress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read() throws Exception {
        try {
            FileCompressor fileCompressor = FileCompressor.read(PATH_SRC);
            fileCompressor.decompress(PATH_EXTRACT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
