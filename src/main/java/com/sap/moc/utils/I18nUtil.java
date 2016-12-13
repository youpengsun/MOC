package com.sap.moc.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * get string by locale
 * @author I322244
 *
 */
public class I18nUtil {
    private static final String BUNDLENAME = "i18n.MessageBundle";
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    //Locale.CHINESE for ZH    
    
    public static String getKey(String key,Locale locale){
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLENAME, locale);
        return bundle.getString(key);
    }
    /**
     * use default locale to get string
     * @param key
     * @return
     */
    public static String getKey(String key){
        return getKey(key,DEFAULT_LOCALE);
    }
}
