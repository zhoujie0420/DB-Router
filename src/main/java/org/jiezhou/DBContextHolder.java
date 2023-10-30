package org.jiezhou;

public class DBContextHolder {

    private static final ThreadLocal<String> dbKey = new ThreadLocal<>();
    private static final ThreadLocal<String> tbKey = new ThreadLocal<>();
    public static void setDBKey(String key) {
        dbKey.set(key);
    }
    public static String getDBKey() {
        return dbKey.get();
    }
    public static void setTBKey(String key) {
        tbKey.set(key);
    }
    public static String getTBKey() {
        return tbKey.get();
    }
    public static void clear() {
        dbKey.remove();
        tbKey.remove();
    }
}
