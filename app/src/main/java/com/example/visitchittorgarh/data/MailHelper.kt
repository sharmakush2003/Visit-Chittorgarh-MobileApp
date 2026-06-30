package com.example.visitchittorgarh.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object MailHelper {
    private const val SMTP_HOST = "smtp.gmail.com"
    private const val SMTP_PORT = "465"
    private const val SENDER_EMAIL = "visitchittorgarh@gmail.com"
    
    // Default placeholder app password. The user will generate one and replace it.
    private const val APP_PASSWORD = "hsmu cxpt rsmc qkts"

    suspend fun sendWelcomeEmail(recipientEmail: String, displayName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val props = Properties().apply {
                put("mail.smtp.host", SMTP_HOST)
                put("mail.smtp.socketFactory.port", SMTP_PORT)
                put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
                put("mail.smtp.auth", "true")
                put("mail.smtp.port", SMTP_PORT)
            }

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD)
                }
            })

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(SENDER_EMAIL, "Visit Chittorgarh"))
                addRecipient(Message.RecipientType.TO, InternetAddress(recipientEmail))
                subject = "Welcome to Visit Chittorgarh!"
                setText("""
                    Hello ${if (displayName.isNotEmpty()) displayName else "Traveler"},

                    Welcome to the Visit Chittorgarh app! We are excited to have you on board. Discover the rich history of Chittorgarh, book royal travel passes, hotel stays, and professional guides all in one place.

                    Best Regards,
                    Visit Chittorgarh Team
                """.trimIndent())
            }

            Transport.send(message)
            Log.d("MailHelper", "Welcome email sent successfully to ${recipientEmail}")
            true
        } catch (e: Exception) {
            Log.e("MailHelper", "Failed to send welcome email: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}
