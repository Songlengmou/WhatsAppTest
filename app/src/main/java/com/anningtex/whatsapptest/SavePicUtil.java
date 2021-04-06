package com.anningtex.whatsapptest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author Song
 * @Desc: 保存图片到本地并刷新图库
 */
public class SavePicUtil {

    private void saveInRoot(Context context) {
        if (getSDPath() != null) {
            String fileName = getSDPath() + "/ATest";
            //"Pic"子目录文件夹
            File file = new File(fileName, "Pic");
            if (!file.mkdir()) {
                Toast.makeText(context, "目录已存在...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "创建新目录...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getSDPath() {
        File sdDir = null;
        //判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            //获取根目录
            sdDir = Environment.getExternalStorageDirectory();
            Log.e("TAG外部存储可用", sdDir.toString());
        }
        return sdDir.toString();
    }

    /**
     * 保存图片到指定路径
     *
     * @param context
     * @param bitmap   要保存的图片
     * @param fileName 自定义图片名称  getString(R.string.app_name) + "" + System.currentTimeMillis()+".png"
     * @return true 成功 false失败
     */
    public static boolean saveImageToGallery(Context context, Bitmap bitmap, String fileName) {
        // 保存图片至指定路径
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ATest/";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        if (appDir.exists()) {
            try {
                File file = new File(appDir, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                //通过io流的方式来压缩保存图片(80代表压缩20%)
                boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.flush();
                fos.close();
                //发送广播通知系统图库刷新数据
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                if (isSuccess) {
                    Toast.makeText(context, "Save Success", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(context, "Save Fail", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 返回保存的路径
     */
    public static String saveBitmap(Context context, Bitmap bm, String picName) {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ATest/" + picName;
            File file = new File(path);
            Log.e("999", "---->path=" + path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.flush();
            out.close();
//            //发送广播通知系统图库刷新数据
//            Uri uri = Uri.fromFile(file);
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Log.e("999", "保存成功：path=" + path);
            Toast.makeText(context, "保存成功：path=" + path, Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 删除文件夹和文件夹里面的文件
     */
    public static void deleteDir(String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                // 删除所有文件
                file.delete();
            } else if (file.isDirectory()) {
                // 递规的方式删除文件夹
                deleteDirWihtFile(file);
            }
        }
        // 删除目录本身
        dir.delete();
    }
}
