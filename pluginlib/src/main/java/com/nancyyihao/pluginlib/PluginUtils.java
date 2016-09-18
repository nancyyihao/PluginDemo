package com.nancyyihao.pluginlib;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import dalvik.system.DexClassLoader;


public class PluginUtils {

    private static final String PLUGIN_PATH = "plugins";
    public static final String PARAM_IS_IN_PLUGIN = "is_in_plugin";
    public static final String PARAM_PLUGIN_NAME = "plugin_name";
    public static final String PARAM_LAUNCH_ACTIVITY = "launch_activity";
    public static final String PARAM_PLUGIN_PATH = "plugin_path";

    static final HashMap<String, DexClassLoader> sClassLoaderMap = new HashMap<>();

    static synchronized ClassLoader getOrCreateClassLoaderByPath(Context c, String pluginName, String apkFilePath) throws Exception {

        DexClassLoader dexClassLoader = sClassLoaderMap.get(pluginName);
        if (dexClassLoader == null) {
            String optimizedDexOutputPath = c.getDir("odex", Context.MODE_PRIVATE).getAbsolutePath();
            dexClassLoader = new DexClassLoader(apkFilePath, optimizedDexOutputPath, null, c.getClassLoader());
            //avoid using multi classloader to load one class
            sClassLoaderMap.put(pluginName, dexClassLoader);
        }
        return dexClassLoader;
    }

    static synchronized ClassLoader getClassLoader(String pluginID) {
        DexClassLoader dexClassLoader = sClassLoaderMap.get(pluginID);
        return dexClassLoader;
    }
    public static File getInstallPath(Context context, String pluginID) {
        File pluginDir = getPluginPath(context);
        if (pluginDir == null) {
            return null;
        }
        int suffixBegin = pluginID.lastIndexOf('.');
        if (suffixBegin != -1 && !pluginID.substring(suffixBegin).equalsIgnoreCase(".apk")) {
            pluginID = pluginID.substring(0, suffixBegin) + ".apk";
        } else if (suffixBegin == -1) {
            pluginID = pluginID + ".apk";
        }
        return new File(pluginDir, pluginID);
    }

    public static File getPluginPath(Context context) {
        return context.getDir(PLUGIN_PATH, Context.MODE_PRIVATE);
    }

    public static void installPlugin(Context context, String pluginPath){
        File pluginFile = new File(context.getDir(PLUGIN_PATH, Context.MODE_PRIVATE), pluginPath);
        if(pluginFile.exists()){
            return;
        }

        BufferedInputStream bis;
        OutputStream dexWriter;

        final int BUF_SIZE = 8 * 1024;
        try {
            bis = new BufferedInputStream(context.getAssets().open(pluginFile.getName()));
            dexWriter = new BufferedOutputStream(
                    new FileOutputStream(pluginFile));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

