package com.example.visitchittorgarh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import com.example.visitchittorgarh.ui.main.MainScreen
import com.example.visitchittorgarh.ui.screens.SplashScreen
import com.example.visitchittorgarh.ui.screens.BookingPassScreen
import com.example.visitchittorgarh.ui.screens.PartnerPortalScreen
import com.example.visitchittorgarh.ui.screens.AboutDeveloperScreen
import com.example.visitchittorgarh.ui.screens.AboutChittorgarhScreen
import com.example.visitchittorgarh.ui.screens.HowToReachScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Splash)
  val context = LocalContext.current
  val sharedPrefs = remember { context.getSharedPreferences("chittorgarh_prefs", Context.MODE_PRIVATE) }

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Splash> {
          SplashScreen(
            onSplashFinished = {
              backStack.add(Main)
              backStack.remove(Splash)
            }
          )
        }
        entry<Main> {
          MainScreen(
            modifier = Modifier.fillMaxSize(),
            onBookingPassClick = { title, transport, tPrice, hotel, hPrice, guide, gPrice ->
              backStack.add(
                BookingPass(
                  pillarTitle = title,
                  transport = transport,
                  transportPrice = tPrice,
                  hotel = hotel,
                  hotelPrice = hPrice,
                  guide = guide,
                  guidePrice = gPrice
                )
              )
            },
            onPartnerPortalClick = {
              backStack.add(PartnerPortal)
            },
            onAboutDeveloperClick = {
              backStack.add(AboutDeveloper)
            },
            onAboutChittorgarhClick = {
              backStack.add(AboutChittorgarh)
            },
            onHowToReachClick = {
              backStack.add(HowToReach)
            }
          )
        }
        entry<BookingPass> { key ->
          val isEnglish = sharedPrefs.getBoolean("is_english", true)
          BookingPassScreen(
            initialPillarTitle = key.pillarTitle,
            initialTransport = key.transport,
            initialTransportPrice = key.transportPrice,
            initialHotel = key.hotel,
            initialHotelPrice = key.hotelPrice,
            initialGuide = key.guide,
            initialGuidePrice = key.guidePrice,
            isEnglish = isEnglish,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
        entry<PartnerPortal> {
          val isEnglish = sharedPrefs.getBoolean("is_english", true)
          PartnerPortalScreen(
            isEnglish = isEnglish,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
        entry<AboutDeveloper> {
          val isEnglish = sharedPrefs.getBoolean("is_english", true)
          AboutDeveloperScreen(
            isEnglish = isEnglish,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
        entry<AboutChittorgarh> {
          val isEnglish = sharedPrefs.getBoolean("is_english", true)
          AboutChittorgarhScreen(
            isEnglish = isEnglish,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
        entry<HowToReach> {
          val isEnglish = sharedPrefs.getBoolean("is_english", true)
          HowToReachScreen(
            isEnglish = isEnglish,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
      },
  )
}


