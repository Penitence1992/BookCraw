package org.penitence.craw.uitl;

/**
 * Created by RenJie on 2017/4/14 0014.
 */
public class URLUtil {

    public static String getHostName(String url){
        String result;
        if(url.startsWith("http://")){
            result = splitHostName(url,7);
        }else if (url.startsWith("https://")){
            result = splitHostName(url,8);
        }else {
            result = url;
        }
        return result.endsWith("/") ? result : result + "/";
    }

    public static String getBasePath(String url, int returnCount){
        String result = url;
        for (int i = 0; i < returnCount; i ++){
            if(url.startsWith("http://")){
                result = splitBasePath(result,7);
            }else if (url.startsWith("https://")){
                result = splitBasePath(result,8);
            }else {
                result = result;
            }
        }
        return result.endsWith("/") ? result : result + "/";
    }

    public static String fillNextUrl(String fullUrl, String nextUrl){
        String tmp = "";
        if (nextUrl.startsWith("http://") || nextUrl.startsWith("https://")) return nextUrl;
        else if (nextUrl.startsWith("/"))   tmp = getHostName(fullUrl) + nextUrl;
        else if (nextUrl.startsWith("..")) tmp = getBasePath(fullUrl,2);
        else tmp = getBasePath(fullUrl,1) + nextUrl;
        if (!tmp.startsWith("http://")) return "http://" + tmp;
        else return tmp;
    }

    private static String splitHostName(final String url,final int count){
        String tmp = url.substring(count);
        return tmp.substring(0,tmp.indexOf("/"));
    }

    private static String splitBasePath(final String url,final int count){
        String tmp = url.substring(count);
        return tmp.substring(0,tmp.lastIndexOf("/")); //www.a.com/abc/aaaa -> www.a.com/abc
    }

    private static String rmParams(final String url){
        return url.substring(0,url.indexOf("?")); // www.a.com/abc?a=b -> www.a.com/abc
    }

    public static String getProtocol(final String fullUrl){
        if( !fullUrl.startsWith("http://") && !fullUrl.startsWith("https://")){
            try {
                throw new IllegalArgumentException("输入的URL没有带有协议名称");
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }

        return fullUrl.substring(0,fullUrl.indexOf("//"));
    }

    public static boolean isFullUrl(final String url){
        if( !url.startsWith("http://") && !url.startsWith("https://")){
            return false;
        }else{
            return true;
        }
    }
}
