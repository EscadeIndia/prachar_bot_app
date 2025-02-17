package com.prachaarbot.domain.use_case

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import androidx.annotation.Nullable
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.model.Data
import com.prachaarbot.domain.repo.SentMessageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class SentMessageUseCase @Inject constructor(
    private val smsSentRepo: SentMessageRepository,
    private val localPrefData: LocalPrefData,
    @ApplicationContext private val context: Context
) {

    suspend fun sendMessageStatus(
        platform: String,
        mobile: String,
        status: String,
        messageBody: String
    ) =
        smsSentRepo.messageSentStatus(platform, mobile, status, messageBody)

    suspend fun getSmsBody() = smsSentRepo.getSmsData()

    suspend fun updatePref(data: Data?) {
        localPrefData.textMessageBody = data?.textMsg
        localPrefData.whatsAppMessageBody = data?.whatsMsg
        if (localPrefData.whatsAppMessageBodyImage.isNullOrBlank()) downloadImage(data?.img)
    }

    suspend fun downloadImage(imageUrl: String?) {
        Glide.with(context).asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    @Nullable transition: Transition<in Bitmap?>?
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        getBitmapUriFromBitmap(context, resource)
                    }
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
            })
    }

    suspend fun getBitmapUriFromBitmap(context: Context, bitmap: Bitmap) {
        try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_${System.currentTimeMillis()}.png"
            )
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            FileProvider.getUriForFile(context, "com.prachaarbot.fileprovider", file)?.let {
                localPrefData.whatsAppMessageBodyImage = it.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}