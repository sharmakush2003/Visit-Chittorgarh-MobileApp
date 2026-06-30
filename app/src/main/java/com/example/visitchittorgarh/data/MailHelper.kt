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

            val sdf = java.text.SimpleDateFormat("dd MMMM yyyy, hh:mm a", java.util.Locale.getDefault())
            val registrationDate = sdf.format(java.util.Date())
            val nameToUse = if (displayName.isNotEmpty()) displayName else "Traveler"

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(SENDER_EMAIL, "Visit Chittorgarh"))
                addRecipient(Message.RecipientType.TO, InternetAddress(recipientEmail))
                subject = "Welcome to Visit Chittorgarh!"
                
                val htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="utf-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Welcome to Visit Chittorgarh</title>
                    </head>
                    <body style="margin: 0; padding: 0; background-color: #f7f9fc; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;">
                        <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px; background-color: #ffffff; margin: 20px auto; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.1); border: 1px solid #e1e8ed;">
                            <!-- Header -->
                            <tr>
                                <td align="center" style="background-color: #1A365D; padding: 30px 20px; border-bottom: 4px solid #D4AF37;">
                                    <h1 style="color: #D4AF37; margin: 0; font-family: 'Georgia', serif; font-size: 28px; letter-spacing: 1px; text-transform: uppercase;">Visit Chittorgarh</h1>
                                    <p style="color: #ffffff; margin: 5px 0 0 0; font-size: 14px; letter-spacing: 2px; text-transform: uppercase; opacity: 0.9;">The Land of Bravery & Heritage</p>
                                </td>
                            </tr>
                            
                            <!-- Hero Image -->
                            <tr>
                                <td align="center" style="padding: 0;">
                                    <img src="https://raw.githubusercontent.com/sharmakush2003/Visit-Chittorgarh-MobileApp/main/app/src/main/res/drawable/vijay_stambh.jpg" alt="Chittorgarh Vijay Stambh" width="100%" style="display: block; width: 100%; max-height: 280px; object-fit: cover;">
                                </td>
                            </tr>

                            <!-- Content -->
                            <tr>
                                <td style="padding: 40px 30px;">
                                    <h2 style="color: #1A365D; margin-top: 0; font-family: 'Georgia', serif; font-size: 22px;">Khamma Ghani & Welcome, $nameToUse!</h2>
                                    <p style="color: #4a5568; line-height: 1.6; font-size: 15px;">
                                        Thank you for signing up on the <strong>Visit Chittorgarh App</strong>. Your royal account has been successfully created. Explore the land of historic forts, legendary stories of sacrifice, and rich Mewar heritage.
                                    </p>

                                    <!-- Account Info Box -->
                                    <table cellpadding="10" cellspacing="0" width="100%" style="background-color: #f7fafc; border-radius: 12px; margin: 25px 0; border: 1px solid #edf2f7;">
                                        <tr>
                                            <td width="35%" style="color: #718096; font-size: 13px; font-weight: bold; text-transform: uppercase; border-bottom: 1px solid #edf2f7;">Registered Email</td>
                                            <td style="color: #2d3748; font-size: 14px; border-bottom: 1px solid #edf2f7;">$recipientEmail</td>
                                        </tr>
                                        <tr>
                                            <td style="color: #718096; font-size: 13px; font-weight: bold; text-transform: uppercase;">Created On</td>
                                            <td style="color: #2d3748; font-size: 14px;">$registrationDate</td>
                                        </tr>
                                    </table>

                                    <p style="color: #4a5568; line-height: 1.6; font-size: 15px;">
                                        With the app, you can easily generate royal travel passes, plan itineraries, discover sightseeing spots, and hire certified local guides to make your journey unforgettable.
                                    </p>

                                    <!-- Button -->
                                    <div style="text-align: center; margin-top: 35px;">
                                        <a href="https://www.visitchittorgarh.in" target="_blank" style="background-color: #1A365D; color: #D4AF37; padding: 14px 30px; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 15px; border: 2px solid #D4AF37; display: inline-block; box-shadow: 0 4px 6px rgba(26,54,93,0.2);">EXPLORE CHITTORGARH</a>
                                    </div>
                                </td>
                            </tr>

                            <!-- Footer -->
                            <tr>
                                <td align="center" style="background-color: #1a202c; padding: 25px 20px; color: #a0aec0; font-size: 12px;">
                                    <p style="margin: 0 0 8px 0; color: #ffffff; font-weight: bold;">Visit Chittorgarh Tourism App</p>
                                    <p style="margin: 0 0 15px 0; line-height: 1.4;">This is an automated welcome email. Please do not reply directly to this message.</p>
                                    <p style="margin: 0; color: #718096;">© 2026 Visit Chittorgarh. All rights reserved.</p>
                                </td>
                            </tr>
                        </table>
                    </body>
                    </html>
                """.trimIndent()

                setContent(htmlContent, "text/html; charset=utf-8")
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
