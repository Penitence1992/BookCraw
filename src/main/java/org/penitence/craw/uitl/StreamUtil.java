package org.penitence.craw.uitl;

import java.io.*;
import java.util.Optional;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class StreamUtil {

    public static void close(Closeable closeable){
        Optional<Closeable> optional = Optional.ofNullable(closeable);
        optional.ifPresent(closeable1 -> {
            try {
                closeable1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void saveToDisk(String[] rows, String path){
        BufferedWriter bufferedWriter = null;
        try {
            File file = new File(path);
            checkOrMakeDir(file.getParentFile());
            bufferedWriter = new BufferedWriter(new FileWriter(path));
            for (String row : rows){
                bufferedWriter.write(row);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            close(bufferedWriter);
        }
    }

    public static void checkOrMakeDir(File dir){
        if(!dir.exists()) dir.mkdirs();
    }
}
