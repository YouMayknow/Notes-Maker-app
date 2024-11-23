package com.example.notesMaker.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.notesMaker.ui.screen.mainScreen.IndividualDrawerScreen
import com.example.notesMaker.ui.screen.mainScreen.UserDetailsAndDrawerScreen
import com.example.notesMaker.ui.screen.noteScreen.CreateNoteScreen
import com.example.notesMaker.ui.screen.noteScreen.UpdateNoteScreen
import com.example.notesMaker.ui.screen.notification.NotificationScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.nestedMainScreenNavigation(navController : NavHostController) {
    navigation<MainScreen>(RouteScreenUserDetail){
        composable<RouteScreenUserDetail>{
            UserDetailsAndDrawerScreen(
//                    viewModel = viewModel,
                onNoteClick = { navController.navigate(RouteUpdateNoteScreen(it)) },
                onAddNoteClick = { navController.navigate(RouteCreateNoteScreen) },
                onDrawerItemClicked = { drawerItem ->
                    navController.navigate("DrawerItems/${drawerItem}")
                },
                onNotificationsIconClicked = {
                    navController.navigate(
                        RouteNotificationScreen
                    )
                }
            )
        }
        composable(
            "DrawerItems/{selectedDrawer}" ,
            arguments = listOf(navArgument("selectedDrawer"){
                type = NavType.StringType
            })
        ){
            val selectedDrawer = it.arguments?.getString("selectedDrawer") ?: ""
            IndividualDrawerScreen(onBackClick = {
                navController.navigate(
                    RouteScreenUserDetail
                )
            }, drawerItemName = selectedDrawer)
        }
        composable<RouteUpdateNoteScreen> {
            val args =  it.toRoute<RouteUpdateNoteScreen>()
            UpdateNoteScreen(
                shortNote = args.shortNote,
                onBackPressed = { navController.navigateUp() },
                redirectBackToDetailedList = { navController.navigateUp() }
            )
        }
        composable<RouteCreateNoteScreen> {
            CreateNoteScreen(
                onBackPressed = {
                    navController.navigateUp()
                },
                redirectBackToDetailedList = { navController.navigateUp() }
            )
        }
        composable<RouteNotificationScreen>{
            NotificationScreen(onBackClick = { navController.navigateUp() })
        }
    }
}

@Serializable
object RouteScreenUserDetail
@Serializable
data class RouteUpdateNoteScreen(val shortNote: String = "")


@Serializable
object RouteNotificationScreen
@Serializable
object RouteCreateNoteScreen

