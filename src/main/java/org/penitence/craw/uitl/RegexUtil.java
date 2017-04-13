package org.penitence.craw.uitl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class RegexUtil {

    private Pattern pattern;

    public RegexUtil(String regex) {
        pattern = Pattern.compile(regex);
    }

    public String findTheFirst(String matchString){
        Matcher matcher = pattern.matcher(matchString);
        if(matcher.find()) return matcher.group();
        return null;
    }
}
