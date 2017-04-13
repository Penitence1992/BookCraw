package org.penitence.craw.config;

import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class PropertiesUtil {

    private static ConcurrentHashMap<String,String> args = new ConcurrentHashMap<>();
    private static Properties properties = null;

    private static final String DEFAULT_CONFIG_FILE = "application.properties";
    private static String configFile = DEFAULT_CONFIG_FILE;

    static{
        init(getStream());
    }

    private static void init(InputStream inputStream){
        properties = new Properties();
        try {
            properties.load(inputStream);
            for (Map.Entry entry : properties.entrySet()){
                args.put(entry.getKey().toString(),entry.getValue().toString());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static InputStream getStream(){
        return PropertiesUtil.class.getClassLoader().getResourceAsStream(configFile);
    }

    public static void setConfigFile(String configFilePath){
        if(StringUtil.isBlank(configFilePath)){
            try{
                throw new NullPointerException("Config file path much not NULL");
            }catch (NullPointerException e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        PropertiesUtil.configFile = configFilePath;
    }

    public static void reload(){
        init(getStream());
    }

    public static String getPropertyValue(String propertyName){
        return args.get(propertyName);
    }

    public static String getPropertyValue(String propertyName,String defaultValue){
        Optional<String> value = Optional.ofNullable(args.get(propertyName));
        return value.orElse(defaultValue);
    }

    public static void putStartArgs(String[] args){
        for (String arg : args){
            if(arg.startsWith("--")){
                arg = arg.substring(2);
                if(arg.contains("=")){
                    String key = arg.substring(0,arg.indexOf("="));
                    String value = arg.substring(arg.indexOf("=") + 1);
                    PropertiesUtil.args.put(key,value);
                }
            }
        }
    }

}
