/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.base.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTool {

    public static final String LOG_TAG = "snakeway";
    public static final String DEFAULT_ROOT_PATH = "snakeway";

    public static String getSdcardRootPath(String rootPath) {
        String sDStateString = android.os.Environment.getExternalStorageState();
        if (sDStateString == null || !sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            return null;
        }
        File fileSdcardDir = android.os.Environment.getExternalStorageDirectory();
        if (fileSdcardDir == null) {
            return null;
        }
        String sdcardRootPath = fileSdcardDir.getAbsolutePath() + File.separator + rootPath;
        File fileRootDir = new File(sdcardRootPath);
        if (!fileRootDir.exists()) {
            fileRootDir.mkdir();
        }
        return sdcardRootPath;
    }

    public static String getDefaultRootPath() {
        return getSdcardRootPath(DEFAULT_ROOT_PATH);
    }

    public static void mkdirs(String filePath) {
        if (filePath == null) {
            return;
        }
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    public static void needShowAndWriteLogToSdcard(boolean openDebug, String fileName, String log, Throwable throwable, int type) {
        if (openDebug) {
            showAndWriteLog(true, fileName, log, throwable, type);
        }
    }

    public static void needWriteLogToSdcard(boolean openDebug, String fileName, String log, Throwable throwable, int type) {
        if (openDebug) {
            showAndWriteLog(false, fileName, log, throwable, type);
        }
    }

    public static synchronized void showAndWriteLog(boolean showLog, String fileName, String log, Throwable throwable, int type) {
        if (log == null) {
            return;
        }
        String theFileName = null;
        if (fileName == null) {
            theFileName = getDefaultRootPath() + File.separator + "Log.txt";
        } else {
            theFileName = fileName;
        }
        String throwableContent = getThrowableContent(throwable);
        String theLog = null;
        if (throwableContent != null) {
            theLog = log + "__throwableContent:" + throwableContent;
        } else {
            theLog = log;
        }
        String newLog = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(new Date());
        if (type == 1) {
            newLog = "Error_" + time + ":" + theLog + "\n";
            if (showLog) {
                LogTool.e(LOG_TAG, newLog);
            }
        } else if (type == 2) {
            newLog = "Waring_" + time + ":" + theLog + "\n";
            if (showLog) {
                LogTool.w(LOG_TAG, newLog);
            }
        } else {
            newLog = "Debug_" + time + ":" + theLog + "\n";
            if (showLog) {
                LogTool.d(LOG_TAG, newLog);
            }
        }
        long length = getFileLength(fileName);
        if (length > 104857600) {
            deleteFile(fileName);
        }
        writeContent(theFileName, newLog, true);
    }

    public static String getThrowableContent(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        String bugContent = null;
        StringWriter stringWriter = null;
        PrintWriter printWriter = null;
        try {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            Throwable throwableCause = throwable.getCause();
            while (throwableCause != null) {
                throwableCause.printStackTrace(printWriter);
                throwableCause = throwableCause.getCause();
            }
            bugContent = stringWriter.toString();
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (stringWriter != null) {
                    stringWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bugContent;
    }

    public static long writeContent(String fileName, String content, boolean append) {
        long fileLength = -1;
        if (fileName == null || content == null) {
            return fileLength;
        }
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file, append);
            byte[] bytes = content.getBytes("utf-8");
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileLength = getFileLength(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileLength;
    }

    public static String readContent(String fileName) {
        String result = null;
        if (fileName == null) {
            return result;
        }
        FileInputStream fileInputStream = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                int length = fileInputStream.available();
                byte[] bytes = new byte[length];
                fileInputStream.read(bytes);
                result = new String(bytes, "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static long getFileLength(String fileName) {
        long fileLength = -1;
        if (fileName == null) {
            return fileLength;
        }
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            fileLength = file.length();
        }
        return fileLength;
    }

    public static void deleteFile(String fileName) {
        if (fileName == null) {
            return;
        }
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public static long getFolderSize(String filePath) {
        long size = 0;
        if (filePath == null) {
            return size;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return size;
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                File file1 = listFiles[i];
                if (file1 != null) {
                    if (file1.isDirectory()) {
                        size = size + getFolderSize(file1.getAbsolutePath());
                    } else {
                        size = size + file1.length();
                    }
                }
            }
        }
        return size;
    }

    public static void deleteFolder(String filePath) {
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i] != null) {
                    deleteFolder(listFiles[i].getAbsolutePath());
                }
            }
            file.delete();
        }
    }

}
