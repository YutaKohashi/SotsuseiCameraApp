package jp.yuta.kohashi.fakelineapp.managers

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import jp.yuta.kohashi.filemanagersampleapp.managers.OnFileCallback
import java.io.*


/**
 * Author : yuta
 * Project name : FakeLineApp
 * Date : 24 / 08 / 2017
 */

class FileManager(context: Context, fragment: Fragment?) {
    private val TAG: String = FileManager::class.java.simpleName

    private val mContext: Context
    private var mFileCallback: OnFileCallback? = null
    private var mFragment: Fragment? = fragment

    constructor(context: Context) : this(context, null) {
    }

    init {
        mContext = context
        if (mContext is OnFileCallback) mFileCallback = mContext
        else {
            if (mFragment is OnFileCallback) {
                mFileCallback = mFragment as OnFileCallback
            } else {
//                throw IllegalArgumentException("OnFileCallbackが実装されていない")
            }
        }
    }

    /**
     *
     */
    fun saveFile(path: String, fileName: String, data: String) {
        if (isExistFile(path, fileName)) {
            mFileCallback?.OnExistedFile(path, fileName, data)
            return
        }
        try {
            // ディレクトリ作成
            var file = File(path)
            if (!file.exists()) file.mkdirs()

            file = File(path, fileName)

            val fileOutputStream: FileOutputStream

            fileOutputStream = FileOutputStream(file, true)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream, "UTF-8")
            val bw = BufferedWriter(outputStreamWriter)
            bw.write(data)
            bw.flush()
            bw.close()
//            text = "saved"
        } catch (e: Exception) {
//            text = "error: FileOutputStream"
            Log.d(TAG, e.toString())
        }
    }

    /**
     * external storageをrootとしてpathを受け取る
     */
    fun saveFileExternalStorage(path: String, fileName: String, data: String) {
        if (isExternalStorageWritable())
            saveFile(Environment.getExternalStorageDirectory().absolutePath.toString() + path, fileName, data)
    }

    /**
     *
     */
    fun loadFile(path: String, fileName: String): String? {
        var text: String? = ""
        val fileInputStream: FileInputStream
        try {
            fileInputStream = FileInputStream(path + fileName)
            val inputStreamReader = InputStreamReader(fileInputStream, "UTF8")
            var lineBuffer: String?

            val reader = BufferedReader(inputStreamReader)

            lineBuffer = reader.readLine()
            while (lineBuffer != null) {
                text += lineBuffer
                lineBuffer = reader.readLine()
            }
        } catch (e: Exception) {
            text = null
            Log.d(TAG, e.toString())
        }

        return text
    }

    /**
     *
     */
    fun loadFileExternalStorage(path: String, fileName: String): String? {
        return if (isExternalStorageReadable()) loadFile(Environment.getExternalStorageDirectory().absolutePath.toString() + path, fileName)
        else null
    }

//    fun  getFilesPathExternalStorage(path:String):MutableList<Uri>{
//        return getFilesPath(Environment.getExternalStorageDirectory().absolutePath.toString() + path)
//    }
//
//    fun getFilesPath(path:String):MutableList<Uri>{
//        return mutableListOf<Uri>().apply {
//            File(path).listFiles().filter { Uri.parse(it.absolutePath) != null }.forEach { add(Uri.parse(it.absolutePath))}
//        }
//    }

    fun getFilesPathExternalStorage(path: String): MutableList<String>? {
        return getFilesPath(Environment.getExternalStorageDirectory().absolutePath.toString() + path)
    }

    fun getFilesPath(path: String): MutableList<String>? {
        return mutableListOf<String>().apply {
            File(path).listFiles()?.filter { Uri.parse(it.absolutePath) != null }?.forEach { add(it.absolutePath) }
        }
    }

    /**
     *
     */
    fun saveBitmap(path: String, fileName: String, bmp: Bitmap) {
        if (isExistFile(path, fileName)) {
            mFileCallback?.OnExistedFile(path, fileName, bmp)
            return
        }

        var fileOutputStream: FileOutputStream? = null
        try {
            // ディレクトリ作成
            var file = File(path)
            if (!file.exists()) file.mkdirs()

            file = File(path, fileName)

            fileOutputStream = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            addImageToGallery(file.absolutePath)
        } catch (e: FileNotFoundException) {
            val e1 = e.message
            if (e.message?.contains("Permission denied") == true)
                mFileCallback?.OnPermissionDenied()
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
        } finally {
            fileOutputStream?.close()
        }
    }

    /**
     *
     */
    fun saveBitmapExternalStorage(path: String, fileName: String, bmp: Bitmap) {
        if (isExternalStorageWritable())
            saveBitmap(Environment.getExternalStorageDirectory().absolutePath.toString() + path, fileName, bmp)
    }

    /**
     *
     */
    fun deleteFile(path: String, fileName: String = "") {
        try {
            val file = if (fileName.isNotEmpty()) File(path, fileName) else File(path)
            file.delete()
            removeImageToGallery(file.absolutePath)
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
        }
    }

    fun deleteFile(uri: Uri) {
        deleteFile(uri.path)
    }

    /**
     *
     */
    fun deleteFileExternalStorage(path: String, fileName: String) {
        if (isExternalStorageWritable())
            deleteFile(Environment.getExternalStorageDirectory().absolutePath.toString() + path, fileName)
    }


    fun isExistFile(path: String, fileName: String): Boolean {
        return File(path, fileName).exists()
    }

    fun isExistFileExternalStorage(path: String, fileName: String): Boolean {
        return isExistFile(Environment.getExternalStorageDirectory().absolutePath.toString() + path, fileName)
    }

    /**
     * ディレクトリがない場合作成
     */
    fun makeDir(filePath: String, dirName: String) {
        val f1 = File(filePath, dirName)
        if (!f1.exists()) {
            f1.mkdirs()
        }
    }

    /**
     *  Checks if external storage is available for read and write
     */
    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    /**
     * Checks if external storage is available to at least read
     */
    private fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state)
    }


    /**
     *
     *
     */
    private fun addImageToGallery(filePath: String) {
        try {
            val values = ContentValues()

            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.MediaColumns.DATA, filePath)

            mContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    /**
     *
     *
     */
    private fun removeImageToGallery(filePath: String) {
        try {
            mContext.contentResolver.delete(
                    MediaStore.Files.getContentUri("external"),
                    MediaStore.Files.FileColumns.DATA + "=?",
                    arrayOf(filePath)
            )
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    //assets フォルダから、テキストファイルを読み込む(Android 用)
    @Throws(IOException::class)
    fun loadTextAsset(fileName: String): String {
        val assetManager = mContext.assets
        val inputStream = assetManager.open(fileName)
        return loadText(inputStream, "UTF-8")
    }

    //設定値
    private val DEFAULT_READ_LENGTH = 8192      //一度に読み込むバッファサイズ

    //ストリームから読み込み、バイト配列で返す
    @Throws(IOException::class)
    fun readStream(inputStream: InputStream, readLength: Int): ByteArray {
        val byteStream = ByteArrayOutputStream(readLength)  //一時バッファのように使う
        val bytes = ByteArray(readLength)    //read() 毎に読み込むバッファ
        val bis = BufferedInputStream(inputStream, readLength)

        try {
            var len = bis.read(bytes, 0, readLength)
            while (len > 0) {
                byteStream.write(bytes, 0, len)    //ストリームバッファに溜め込む
                len = bis.read(bytes, 0, readLength)
            }
            return byteStream.toByteArray()    //byte[] に変換

        } finally {
            try {
                byteStream.reset()     //すべてのデータを破棄
                bis.close()            //ストリームを閉じる
            } catch (e: Exception) {
                //IOException
            }

        }
    }

    //ストリームから読み込み、テキストエンコードして返す
    @Throws(IOException::class, UnsupportedEncodingException::class)
    fun loadText(inputStream: InputStream, charsetName: String): String {
        return kotlin.text.String(readStream(inputStream, DEFAULT_READ_LENGTH), charset(charsetName))
    }

}