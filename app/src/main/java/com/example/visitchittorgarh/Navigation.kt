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
import com.example.visitchittorgarh.ui.screens.EmergencyContactsScreen
import com.example.visitchittorgarh.ui.screens.FoodRestaurantsScreen


import androidx.compose.runtime.LaunchedEffect
import com.example.visitchittorgarh.ui.screens.AuthScreen

@Composable
fun MainNavigation() {
  val context = LocalContext.current
  val sharedPrefs = remember { context.getSharedPreferences("chittorgarh_prefs", Context.MODE_PRIVATE) }
  val isFirstLaunch = remember { sharedPrefs.getBoolean("is_first_launch", true) }
  val startDestination = if (isFirstLaunch) Splash else Main
  val backStack = rememberNavBackStack(startDestination)

  // Sync user passes with Firestore on login state change
  LaunchedEffect(key1 = true) {
    com.google.firebase.auth.FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
      val currentUser = firebaseAuth.currentUser
      if (currentUser != null) {
        com.example.visitchittorgarh.data.BookingManager.listenToUserPasses(currentUser.uid)
      } else {
        com.example.visitchittorgarh.data.BookingManager.stopListening()
      }
    }
  }

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Splash> {
          SplashScreen(
            onSplashFinished = {
              sharedPrefs.edit().putBoolean("is_first_launch", false).apply()
              backStack.add(Main)
              backStack.remove(Splash)
            }
          )
        }
        entry<Main> {
          MainScreen(
            modifier = Modifier.fillMaxSize(),
            onBookingPassClick = { title, transport, tPrice, hotel, hPrice, guide, gPrice ->
              if (com.google.firebase.auth.FirebaseAuth.getInstance().currentUser == null) {
                backStack.add(Auth)
              } else {
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
              }
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
            },
            onAuthClick = {
              backStack.add(Auth)
            },
            onEmergencyContactsClick = {
              backStack.add(EmergencyContacts)
            },
            onFoodRestaurantsClick = {
              backStack.add(FoodRestaurants)
            }
          )
        }
        entry<Auth> {
          val isEnglish = sharedPrefs.getBoolean("is_english", true)
          AuthScreen(
            isEnglish = isEnglish,
            onAuthSuccess = {
              backStack.removeLastOrNull()
            },
            onBackClick = {
              backStack.removeLastOrNull()
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
        entry<EmergencyContacts> {
          val isEnglish = sharedPrefs.getBoolean("is_english", true)
          EmergencyContactsScreen(
            isEnglish = isEnglish,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
        entry<FoodRestaurants> {
          val isEnglish = sharedPrefs.getBoolean("is_english", true)
          FoodRestaurantsScreen(
            isEnglish = isEnglish,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
      },
  )
}


