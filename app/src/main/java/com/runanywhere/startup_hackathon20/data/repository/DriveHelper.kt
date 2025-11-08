package com.runanywhere.startup_hackathon20.data.repository

import android.content.Context
import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class DriveHelper(private val context: Context) {

    private var driveService: Drive? = null
    private val pdfCache = mutableMapOf<String, String>() // fileId to extracted text cache

    init {
        // Initialize PDFBox
        PDFBoxResourceLoader.init(context)
    }

    /**
     * Initialize Google Drive service with user credentials
     */
    fun initializeDrive(accountName: String) {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(DriveScopes.DRIVE_READONLY)
        ).apply {
            selectedAccountName = accountName
        }

        driveService = Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("EduVenture RPG")
            .build()
    }

    /**
     * Fetch list of PDF files from a specific Google Drive folder
     */
    suspend fun fetchPDFsFromFolder(folderId: String): List<DriveFile> =
        withContext(Dispatchers.IO) {
            try {
                val result = driveService?.files()?.list()
                    ?.setQ("'$folderId' in parents and mimeType='application/pdf' and trashed=false")
                    ?.setFields("files(id, name, modifiedTime, size)")
                    ?.setOrderBy("modifiedTime desc")
                    ?.execute()

                result?.files?.map { file ->
                    DriveFile(
                        id = file.id,
                        name = file.name,
                        modifiedTime = file.modifiedTime?.value ?: 0,
                        size = file.getSize() ?: 0
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                Log.e("DriveHelper", "Error fetching PDFs: ${e.message}")
                emptyList()
            }
        }

    /**
     * Download and extract text from a PDF file
     */
    suspend fun extractTextFromPDF(fileId: String): String = withContext(Dispatchers.IO) {
        // Check cache first
        pdfCache[fileId]?.let { return@withContext it }

        try {
            // Download PDF file
            val outputStream = ByteArrayOutputStream()
            driveService?.files()?.get(fileId)?.executeMediaAndDownloadTo(outputStream)

            // Save to temporary file
            val tempFile = File.createTempFile("pdf_", ".pdf", context.cacheDir)
            FileOutputStream(tempFile).use { fos ->
                fos.write(outputStream.toByteArray())
            }

            // Extract text using PDFBox
            val document = PDDocument.load(tempFile)
            val stripper = PDFTextStripper()
            val text = stripper.getText(document)
            document.close()

            // Clean up temp file
            tempFile.delete()

            // Cache the result
            pdfCache[fileId] = text

            text
        } catch (e: Exception) {
            Log.e("DriveHelper", "Error extracting PDF text: ${e.message}")
            "Error: Could not extract text from PDF. ${e.message}"
        }
    }

    /**
     * Summarize PDF content into chunks for better processing
     */
    fun summarizeTextForChat(text: String, maxLength: Int = 3000): String {
        return if (text.length <= maxLength) {
            text
        } else {
            // Take key sections from beginning, middle, and end
            val chunkSize = maxLength / 3
            val beginning = text.take(chunkSize)
            val middle =
                text.substring(text.length / 2 - chunkSize / 2, text.length / 2 + chunkSize / 2)
            val end = text.takeLast(chunkSize)

            """
            [Document Summary - Beginning]
            $beginning
            
            [Document Summary - Middle Section]
            $middle
            
            [Document Summary - Ending]
            $end
            """.trimIndent()
        }
    }

    /**
     * Clear PDF cache
     */
    fun clearCache() {
        pdfCache.clear()
    }
}

data class DriveFile(
    val id: String,
    val name: String,
    val modifiedTime: Long,
    val size: Long
)
