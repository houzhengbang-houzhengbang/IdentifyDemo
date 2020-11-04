package com.pad.rzjys.IdentifyUtils;

import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;

import com.myscript.iink.Configuration;
import com.myscript.iink.ContentBlock;
import com.myscript.iink.ContentPackage;
import com.myscript.iink.ContentPart;
import com.myscript.iink.Editor;
import com.myscript.iink.Engine;
import com.myscript.iink.IEditorListener;
import com.myscript.iink.MimeType;
import com.myscript.iink.PointerEvent;
import com.myscript.iink.Renderer;
import com.myscript.iink.uireferenceimplementation.FontMetricsProvider;
import com.pad.rzjys.IdentifyUtils.certificate.MyCertificate;
import com.pad.rzjys.MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * FileName: IdentifyUtils
 * Author: houzhengbang
 * Date: 2020/8/11 2:20 PM
 * Description:
 */
public class IdentifyUtil {

    private static Editor editor;
    private static Engine engine;
    private static Renderer renderer;

    public static Engine SingletonIdentifyUtil() {

        if (engine == null) {
            engine = Engine.create(MyCertificate.getBytes());

            initIdentify();
        }
        return engine;
    }

    public static void IdentifyDestroy() {
        engine = null;
    }

    private static void initIdentify() {
//        //修改引擎配置语言：
//        // configure recognition
//        Configuration conf = engine.getConfiguration();
//        //设置中文识别
//        conf.setStringArray("configuration-manager.search-path", new String[]{"zip://" + MyApplication.getContext().getPackageCodePath() + "!/assets/conf"});
//
//        String tempDir = MyApplication.getContext().getFilesDir().getPath() + File.separator + "tmp";
//        conf.setString("content-package.temp-folder", tempDir);
//        conf.setString("lang", "zh_CN");



//        // 中文
//        Configuration conf = engine.getConfiguration();
//        String confDir = "zip://" + MyApplication.getContext().getPackageCodePath() + "!/assets/conf";
//        conf.setStringArray("configuration-manager.search-path", new String[]{confDir});
//        conf.setString("lang", "zh_CN");
//        conf.setBoolean("gesture.enable", false);
//        conf.setBoolean("export.jiix.strokes", false);
//        conf.setBoolean("export.jiix.bounding-box", false);
//        conf.setBoolean("export.jiix.glyphs", false);
//        conf.setBoolean("export.jiix.primitives", false);
//        conf.setBoolean("export.jiix.chars", false);
//        String tempDir = MyApplication.getContext().getFilesDir().getPath() + File.separator + "tmp";
//        conf.setString("content-package.temp-folder", tempDir);



// 英文
        Configuration conf = engine.getConfiguration();
        String confDir = "zip://"+MyApplication.getContext().getPackageCodePath()+"!/assets/conf";
        conf.setStringArray("configuration-manager.search-path", new String[]{confDir});
        conf.setString("lang", "en_US");
        conf.setBoolean("gesture.enable", false);
        conf.setBoolean("export.jiix.strokes", false);
        conf.setBoolean("export.jiix.bounding-box", false);
        conf.setBoolean("export.jiix.glyphs", false);
        conf.setBoolean("export.jiix.primitives", false);
        conf.setBoolean("export.jiix.chars", false);
        String tempDir = MyApplication.getContext().getFilesDir().getPath() + File.separator + "tmp";
        conf.setString("content-package.temp-folder", tempDir);



        // Configure the engine to disable guides (recommended)
        engine.getConfiguration().setBoolean("text.guides.enable", false);

// Create a renderer with a null render target
        float dpiX = 150;
        float dpiY = 150;
        renderer = engine.createRenderer(dpiX, dpiY, null);


        initEditor();
    }


    private static void initEditor() {

        if (engine == null) {
            SingletonIdentifyUtil();
            return;
        }

        if (renderer == null) {
            float dpiX = 150;
            float dpiY = 150;
            renderer = engine.createRenderer(dpiX, dpiY, null);
        }

        // Create the editor
        editor = engine.createEditor(renderer);


// The editor requires a font metrics provider and a view size *before* calling setPart()
        DisplayMetrics displayMetrics = MyApplication.getContext().getResources().getDisplayMetrics();
        Map<String, Typeface> typefaceMap = new HashMap<>();
        editor.setFontMetricsProvider(new FontMetricsProvider(displayMetrics, typefaceMap));
        editor.setViewSize(1240, 1754);

// Create a temporary package and part for the editor to work with

        try {

            if (packages == null) {
                packages = engine.createPackage("text.iink");
            }
            part = packages.createPart("Text");
//            partSelect = packages.createPart("Text");

            editor.setPart(part);
//            editorSelect.setPart(partSelect);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //识别状态监听 代理
        editor.addListener(new IEditorListener() {
            @Override
            public void partChanging(Editor editor, ContentPart oldPart, ContentPart newPart) {
                // no-op
            }

            @Override
            public void partChanged(Editor editor) {

            }

            @Override
            public void contentChanged(final Editor editor, String[] blockIds) {
                final ContentBlock rootBlock = editor.getRootBlock();

                try {

                    String trim = editor.export_(rootBlock, MimeType.TEXT).trim();
                    if (onIdentifyListeners != null) {
                        onIdentifyListeners.onIdentifyListener(trim);
                    }
                    Log.e("识别editor", "contentChanged1: " + trim);

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Editor editor, String blockId, String message) {

                Log.e("TAG", "Failed to edit block \"" + blockId + "\"" + message);
            }
        });

    }

    static ContentPackage packages = null;
    static ContentPart part;

//    private static void initCreatEdit() {
//        if (editor != null) {
//            editor.setPart(null);
//            editor.clear();
//        }
//        if (packages != null) {
//            part = packages.getPart(0);
//        }
//        if (part != null) {
//            part.close();
//            part = null;
//        }
//
//    }


    private static void setIdentify(ArrayList<PointerEvent> events) {

        if (events.size() < 3) {
            if (onIdentifyListeners != null) {
                onIdentifyListeners.onIdentifyErrorListener();
            }
            return;
        }

        if (editor == null) {
            if (onIdentifyListeners != null) {
                onIdentifyListeners.onIdentifyErrorListener();
            }
            return;
        }
        editor.clear();

        try {
            editor.pointerEvents(events.toArray(new PointerEvent[0]), true);
        } catch (Exception e) {
            Log.e("识别", "setIdentify--: " + e.toString());
            if (onIdentifyListeners != null) {
                onIdentifyListeners.onIdentifyErrorListener();
            }
            if ("java.lang.IllegalStateException: pointer with same pointerId has a trace already pending".equals(e.toString())) {
                initEditor();
            }
        }
    }

    public static void toIdentify( ArrayList<PointerEvent> events) {
            setIdentify(events);
    }

    static onIdentifyListener onIdentifyListeners;

    public static void setonIdentifyListener(onIdentifyListener onIdentifyListener) {
        onIdentifyListeners = onIdentifyListener;
    }

    public interface onIdentifyListener {
        void onIdentifyListener(String s);
        void onIdentifyErrorListener();

    }
}