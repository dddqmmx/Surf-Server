package com.dddqmmx.surf.server.util;


import java.io.File;

public class FileUtil {
    public static File getFileName(int fileId) {
        return new File("surf/file/" + fileId + ".sf");
    }
}
