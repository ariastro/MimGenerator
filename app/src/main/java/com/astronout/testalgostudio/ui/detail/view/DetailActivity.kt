package com.astronout.testalgostudio.ui.detail.view

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.astronout.testalgostudio.R
import com.astronout.testalgostudio.base.BaseActivity
import com.astronout.testalgostudio.databinding.ActivityDetailBinding
import com.astronout.testalgostudio.ui.main.model.Meme
import com.astronout.testalgostudio.utils.*
import com.astronout.testalgostudio.utils.Constants.FOLDER_NAME
import com.astronout.testalgostudio.utils.glide.GlideApp
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_detail.view.*
import kotlinx.android.synthetic.main.add_text_dialog.view.*
import java.io.*
import java.io.File.separator

class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val REQUEST_WRITE_STORAGE = 6122
    private val REQUEST_CODE_GALLERY = 5211

    private var path: String? = null
    private var addedText: String? = null

    private var imageBitmap: Bitmap? = null

    private lateinit var extraData: Meme

    companion object {
        const val EXTRA_MEME = "EXTRA_MEME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        setSupportActionBar(binding.toolbar)

        extraData = intent.getParcelableExtra(EXTRA_MEME)!!

        createAndloadBitmap()

        binding.btnSimpan.setOnClickListener {
            try {
                if (checkPermissionForWriteStorage(this, REQUEST_WRITE_STORAGE)) {
                    path = saveImage(imageBitmap!!, this)
                    showSuccessToasty(getString(R.string.save_image_success, path))
                }
            } catch (e: java.lang.Exception) {
                showToast(e.message.toString())
            }
        }

        binding.btnAddImage.setOnClickListener {
            imagePickerGallery(REQUEST_CODE_GALLERY)
        }

        binding.btnShare.setOnClickListener {
            if (!path.isNullOrEmpty()) {
                val sheet = SheetShareFragment()
                sheet.setPath(path!!)
                sheet.show(supportFragmentManager, "SheetShareFragment")
            } else {
                showWarningToasty(getString(R.string.image_not_saved_yet))
            }
        }

        binding.btnAddText.setOnClickListener {
            showAddTextDialog()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun createAndloadBitmap() {
        GlideApp.with(this)
            .asBitmap()
            .load(extraData.url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.image.setImageBitmap(resource)
                    imageBitmap = resource
                    try {
                        if (checkPermissionForWriteStorage(this@DetailActivity, REQUEST_WRITE_STORAGE)) {
                            path = saveImage(imageBitmap!!, this@DetailActivity)
                        }
                    } catch (e: java.lang.Exception) {
                        showToast(e.message.toString())
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun showAddTextDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_text_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle(resources.getString(R.string.add_text))
        val mAlertDialog = mBuilder.show()
        mDialogView.btn_dialog_simpan.setOnClickListener {
            when {
                mDialogView.text.text.toString().isEmpty() -> getString(R.string.empty_field)
                else -> {
                    addedText = mDialogView.text.text.toString()
                    val imagePath = Uri.parse(path)
                    val imageFile = (File(imagePath.path!!))
                    val uri = Uri.fromFile(imageFile)
                    uri.let {
                        val processedBitmap = processingBitmap()
                        imageBitmap = processedBitmap
                        if (processedBitmap != null) {
                            binding.image.setImageBitmap(processedBitmap)
                        } else {
                            showErrorToasty(getString(R.string.processing_failed))
                        }
                    }
                    mAlertDialog.dismiss()
                }
            }
        }
        mDialogView.btn_dialog_batal.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    @Suppress("DEPRECATION")
    private fun saveImage(bitmap: Bitmap, context: Context): String {
        val path: String
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$FOLDER_NAME")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
            path = uri.toString()
        } else {
            val directory =
                File(Environment.getExternalStorageDirectory().toString() + separator + FOLDER_NAME)
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            path = "$directory/$fileName"
        }
        return path
    }

    private fun contentValues(): ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    saveImage(imageBitmap!!, this)
                } else {
                    //permission from popup denied
                    showToast(getString(R.string.permission_gallery_denied))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_GALLERY -> {

            }
        }
    }

    fun shareTwitter(path: String) {
        try {
            val imagePath = Uri.parse(path)
            val imageFile = (File(imagePath.path!!))
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "/*"
            intent.setClassName("com.twitter.android", "com.twitter.android.PostActivity")
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showWarningToasty(getString(R.string.no_twitter_app))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun processingBitmap(): Bitmap? {
        val bitmap: Bitmap?

        var newBitmap: Bitmap? = null

        try {
            val imagePath = Uri.parse(path)
            val imageFile = (File(imagePath.path!!))
            bitmap =
                BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.fromFile(imageFile)))

            var config: Bitmap.Config? = bitmap!!.config
            if (config == null) {
                config = Bitmap.Config.ARGB_8888
            }

            newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, config)
            val newCanvas = Canvas(newBitmap)

            newCanvas.drawBitmap(bitmap, 0f, 0f, null)

            val captionString = addedText
            if (captionString != null) {
                val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
                paintText.color = ContextCompat.getColor(this, R.color.colorPrimary)
                paintText.textSize = 100f
                paintText.style = Paint.Style.FILL

                val rectText = Rect()
                paintText.getTextBounds(captionString, 0, captionString.length, rectText)

                newCanvas.drawText(
                    captionString,
                    0f, rectText.height().toFloat(), paintText
                )
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return newBitmap
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}