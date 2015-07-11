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
import com.espringtran.compressor4j.util.FileUtil;
import com.espringtran.compressor4j.util.LogUtil;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public class BenchmarkTest {

    private static final String SRC = "test/src/";
    private static final String DES = "test/des/";
    private static final String EXTRACT = "test/extract/";
    private static final String[] FILES = new String[] { "log.log",
            "IMG_0332.JPG", "grand-tour.mp4", "ChromeStandaloneSetup.exe",
            "CheckListThucHien.docx", "Bao cao danh sach chuc nang.xls",
            "20130504_DaoTao_VPM_KhongKhoan.ppt", "IMG_0035.psd",
            "Mua Yeu Dau - Dinh Manh Ninh.mp3" };
    private static final CompressionLevel LEVEL = ZipLevel.NORMAL;

    public static void main(String[] args) throws Exception {
        for (String file : FILES) {
            FileCompressor.LOGGER
                    .info("-------------------------------" + file);
            CompressionType[] compressionTypes = CompressionType.values();
            for (CompressionType compressionType : compressionTypes) {
                FileCompressor.LOGGER.info("++++++++++" + compressionType);
                LogUtil.createMemoryLog();
                write(file, compressionType, LEVEL);
                read(file, compressionType);
                LogUtil.createMemoryLog();
            }
        }
        FileCompressor.LOGGER.info("Done!");
    }

    public static void write(String file, CompressionType type,
            CompressionLevel level) throws Exception {
        try {
            FileCompressor fileCompressor = new FileCompressor();
            fileCompressor.add(SRC + file, file);
            fileCompressor.setType(type);
            fileCompressor.setLevel(level);
            fileCompressor.setCompressedPath(DES
                    + FileUtil.getFileNameWithouExtension(file) + "."
                    + type.getExtension());
            fileCompressor.compress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read(String file, CompressionType type) throws Exception {
        try {
            FileCompressor fileCompressor = FileCompressor.read(DES
                    + FileUtil.getFileNameWithouExtension(file) + "."
                    + type.getExtension());
            fileCompressor.decompress(EXTRACT
                    + FileUtil.getFileNameWithouExtension(file) + "."
                    + type.getExtension() + "/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
