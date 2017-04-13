package org.penitence.craw.uitl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class CustomClassLoader extends ClassLoader {

    private String classPath;
    private String className;

    public CustomClassLoader(String classPath,String className) {
        this.classPath = classPath.replace("\\","/");
        this.className = className;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = null;
        try {
            classBytes = Files.readAllBytes(Paths.get(new URI("file:///" + classPath)));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return defineClass(className,classBytes, 0, classBytes.length);
    }
}
