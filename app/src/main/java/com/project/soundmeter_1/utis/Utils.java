package com.project.soundmeter_1.utis;

public class Utils {
    public static final String PREF_NAME = "SoundMeterPreferences";
    public static final String MAX_DB_KEY = "MAX_DB_KEY";

    public static float volume = 10000;
    public static final int msgWhat = 0x1001;
    public static final int refreshTime = 100;
    public static int dbMax = 60;

    public static float dbCount = 40;

    private static float lastDbCount = dbCount;
    private static float min = 0.5f;
    private static float value = 0;
    public static void setDbCount(float dbValue) {
        if (dbValue > lastDbCount) {
            value = dbValue - lastDbCount > min ? dbValue - lastDbCount : min;
        }else{
            value = dbValue - lastDbCount < -min ? dbValue - lastDbCount : -min;
        }
        dbCount = lastDbCount + value * 0.2f ;
        lastDbCount = dbCount;
    }
}
