package net.suntrans.common.utils


import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.CursorLoader
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import net.suntrans.tenement.App
import java.util.*


object UiUtils {

    private var mToast: Toast? = null

    val resource1: Resources
        get() = App.application!!.resources

    val context: Context?
        get() = App.application

    /**
     * 检查网络是否可用
     */
    // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
    // 获取NetworkInfo对象
    //					System.out.println(i + "===状态===" + networkInfo[i].getState());
    //					System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
    // 判断当前网络状态是否为连接状态
    val isNetworkAvailable: Boolean
        get() {
            val context = UiUtils.context
            val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager == null) {
                return false
            } else {
                val networkInfo = connectivityManager.allNetworkInfo

                if (networkInfo != null && networkInfo.size > 0) {
                    for (i in networkInfo.indices) {
                        if (networkInfo[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    var handler = Handler(Looper.getMainLooper())

    fun showToast(str: String) {
        if (mToast == null) {
            mToast = Toast.makeText(App.application, str, Toast.LENGTH_SHORT)
        }
        mToast!!.setText(str)

        if (Thread.currentThread()!=Looper.getMainLooper().thread){
            handler.post(Runnable { mToast!!.show() })
        }else{

            mToast!!.show()
        }

    }

    fun showToast(context: Context,str: String) {
        if (mToast == null) {
            mToast = Toast.makeText(context, str, Toast.LENGTH_SHORT)
        }
        mToast!!.setText(str)

        if (Thread.currentThread()!=Looper.getMainLooper().thread){
            handler.post(Runnable { mToast!!.show() })
        }else{

            mToast!!.show()
        }

    }

    fun showToastLong(str: String) {
        if (mToast == null) {
            mToast = Toast.makeText(App.application, str, Toast.LENGTH_LONG)
        }
        mToast!!.setText(str)
        mToast!!.show()
    }

    fun <T> checkNotNull(obj: T?): T {
        if (obj == null) {
            throw NullPointerException()
        }
        return obj
    }

    /**
     * 获取到字符数组
     *
     * @param tabNames 字符数组的id
     */
    fun getStringArray(tabNames: Int): Array<String> {
        return resource1.getStringArray(tabNames)
    }

    /**
     * dip转换px
     */
    fun dip2px(dip: Int): Int {
        val scale = resource1.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }

    /**
     * dip转换px
     */
    fun dip2px(dip: Float, context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }

    /**
     * pxz转换dip
     */

    fun px2dip(px: Int): Int {
        val scale = resource1.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    //	public static void runOnUiThread(Runnable runnable) {
    //		// 在主线程运行
    //		if(android.os.Process.myTid()==App.getMainTid()){
    //			runnable.run();
    //		}else{
    //			//获取handler
    //			App.getHandler().post(runnable);
    //		}
    //	}


    /**
     * 加载view
     *
     * @param layoutId
     * @return
     */
    fun inflate(layoutId: Int): View {
        return View.inflate(context, layoutId, null)
    }

    fun getDimens(homePictureHeight: Int): Int {
        return resource1.getDimension(homePictureHeight).toInt()
    }


    //

    /**
     * 将十六进制的字符串转化为十进制的数值
     */
    fun HexToDec(hexStr: String): Long {
        val hexMap = prepareDate() // 先准备对应关系数据
        val length = hexStr.length
        var result = 0L // 保存最终的结果
        for (i in 0 until length) {
            result += (hexMap[hexStr.subSequence(i, i + 1)]!! * Math.pow(16.0, (length - 1 - i).toDouble())).toLong()
        }
        //        System.out.println("hexStr=" + hexStr + ",result=" + result);
        return result
    }

    /**
     * 准备十六进制字符对应关系。如("1",1)...("A",10),("B",11)
     */
    private fun prepareDate(): HashMap<String, Int> {
        val hashMap = HashMap<String, Int>()
        for (i in 0..9) {
            hashMap.put(i.toString() + "", i)
        }
        hashMap.put("a", 10)
        hashMap.put("b", 11)
        hashMap.put("c", 12)
        hashMap.put("d", 13)
        hashMap.put("e", 14)
        hashMap.put("f", 15)
        return hashMap
    }


    fun getColor(context: Context, color: Int): Int {
        val tv = TypedValue()
        context.theme.resolveAttribute(color, tv, true)
        return tv.data
    }

    fun isVaild(value: String?): Boolean {
        var value = value
        if (value != null) {
            value = value.replace(" ", "")
            if (!TextUtils.equals("", value))
                return true
        }
        return false
    }

    fun getDisplaySize(context: Context): IntArray {
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(displayMetrics)
        return intArrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    fun subBytes(src: ByteArray, begin: Int, count: Int): ByteArray {
        val bs = ByteArray(count)
        System.arraycopy(src, begin, bs, 0, count)
        return bs
    }


    fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null)
            data = uri.path
        else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {

            val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    /**
     * 根据图片的Uri获取图片的绝对路径(已经适配多种API)
     *
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        val sdkVersion = Build.VERSION.SDK_INT
        if (sdkVersion < 11) {
            // SDK < Api11
            return getRealPathFromUri_BelowApi11(context, uri)
        }
        if (sdkVersion < 19) {
            // SDK > 11 && SDK < 19
            return getRealPathFromUri_Api11To18(context, uri)
        } else
        // SDK > 19
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return getRealPathFromUri_AboveApi19(context, uri)
            }
        return null
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun getRealPathFromUri_AboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val wholeID = DocumentsContract.getDocumentId(uri)
        // 使用':'分割
        val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media._ID + "=?"
        val selectionArgs = arrayOf(id)

        val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null)
        val columnIndex = cursor!!.getColumnIndex(projection[0])

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    private fun getRealPathFromUri_Api11To18(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val loader = CursorLoader(context, uri, projection, null, null, null)
        val cursor = loader.loadInBackground()

        if (cursor != null) {
            cursor.moveToFirst()
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]))
            cursor.close()
        }
        return filePath
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private fun getRealPathFromUri_BelowApi11(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]))
            cursor.close()
        }
        return filePath
    }

}
