package com.yifan.admin.api.utils;

import com.yifan.admin.api.exception.ApiException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * <p>
 * 描述: 有关文件信息和操作的工具类
 * 1. 通过文件头判断文件类型
 * 2. 获取格式化的文件大小
 * 3. 高效的将文件转换成字节数组
 * </p>
 *
 */
public class FileUtil {

    private static final HashMap<String, String> fileTypes = new HashMap<String, String>();

    static { // BOM（Byte Order Mark）文件头字节
        fileTypes.put("494433" , "mp3");
        fileTypes.put("524946" , "wav");
        fileTypes.put("ffd8ff" , "jpg");
        fileTypes.put("FFD8FF" , "jpg");
        fileTypes.put("89504E" , "png");
        fileTypes.put("89504e" , "png");
        fileTypes.put("474946" , "gif");
    }

    private static final String B_UNIT = "B";
    private static final String KB_UNIT = "KB";
    private static final String MB_UNIT = "MB";
    private static final String GB_UNIT = "GB";
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.0");


    /**
     * <p>
     * 描述：通过含BOM（Byte Order Mark）的文件头的
     * 前 3个字节判断文件类型
     * </p>
     * 获取文件类型
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getFileType(String filePath) {
        return fileTypes.get(getFileHeader3(filePath));
    }

    /**
     * <p>
     * 描述：获取文件头前3个字节
     * </p>
     *
     * @param filePath 文件路径
     * @return
     */
    private static String getFileHeader3(String filePath) {

        File file = new File(filePath);
        if (!file.exists() || file.length() < 4) {
            return "null";
        }
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(file);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * <p>
     * 描述： 获取格式化的文件大小
     * 格式为带单位保留一位小数
     * </p>
     *
     * @param size
     * @return
     */
    private static String getFileFormatSize(double size) {
        String fileSizeString = "";
        if (size < 1024) {
            fileSizeString = decimalFormat.format(size) + B_UNIT;
        } else if (size < 1048576) {
            fileSizeString = decimalFormat.format(size / 1024) + KB_UNIT;
        } else if (size < 1073741824) {
            fileSizeString = decimalFormat.format(size / 1048576) + MB_UNIT;
        } else {
            fileSizeString = decimalFormat.format(size / 1073741824) + GB_UNIT;
        }
        return fileSizeString;
    }

    public static String getFileFormatSize(long size) {
        return getFileFormatSize((double) size);
    }

    /**
     * <p>
     * 描述：高效率的将文件转换成字节数组
     * </p>
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static byte[] toByteArray(String filePath) throws IOException {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filePath, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
                    fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取文件扩展名-不含.
     *
     * @param filename
     * @return
     */
    public static String getFileExtensionName(String filename) {
        if (StringUtils.isBlank(filename)) {
            throw new ApiException("filename can not be null.");
        }
        String extensionName = "";
        filename = filename.replaceAll(" " , "");
        if (filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0) {
            extensionName = filename.substring(filename.lastIndexOf(".") + 1);
        }
        return extensionName;
    }

    /**
     * 获取文件名
     *
     * @param filePath 文件路径
     * @return 文件名
     * @throws IOException
     */
    public static String getFileNameByLocal(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("file not exist!");
        }
        return file.getName();
    }


    /**
     * 获取文件名-url地址
     * @param fileUrl
     * @return
     */
    public static String getFileNameByRemote(String fileUrl) {
        String fileName = "";
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String newUrl = conn.getURL().getFile();
            if (StringUtils.isNotBlank(newUrl)) {
                newUrl = java.net.URLDecoder.decode(newUrl, "UTF-8");
                int pos = newUrl.indexOf('?');
                if (pos > 0) {
                    newUrl = newUrl.substring(0, pos);
                }
                pos = newUrl.lastIndexOf('/');
                fileName = newUrl.substring(pos + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;

    }

    /**
     * 获取某个文件的前缀路径
     * <p>
     * 不包含文件名的路径
     *
     * @param file 当前文件对象
     * @return
     * @throws IOException
     */
    public static String getFilePrefixPath(File file) throws IOException {
        String path;
        if (!file.exists()) {
            throw new IOException("file not exist!");
        }
        String fileName = file.getName();
        path = file.getPath().replace(fileName, "");
        return path;
    }

    public static String getFileHash(InputStream is) throws NoSuchAlgorithmException, IOException {
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        try (FileInputStream fis = (FileInputStream) is) {
            byte[] buffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md5Digest.update(buffer, 0, bytesRead);
            }
        }
        StringBuilder sb = new StringBuilder();
        byte[] digestBytes = md5Digest.digest();
        for (byte b : digestBytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

}
