package com.example.notesMaker.ui.screen.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.notesMaker.ui.screen.noteScreen.CreateNoteScreen
import com.example.notesMaker.ui.screen.noteScreen.UpdateNoteScreen
import com.example.notesMaker.ui.screen.notification.NotificationScreen
import kotlinx.serialization.Serializable


/*it is a main  navigation screen that have access of all the screen except entry screen it takes to notesListAndDetail
screen that have the both our screen .
on this basis of the click it prepares the drawer based screen for the user using individual screen
 */
@Composable
fun  MainScreenNavigation (
    modifier: Modifier = Modifier ,
    navController: NavHostController = rememberNavController()  ,
    viewModel: NotesListScreenViewModel = hiltViewModel()
) {
    NavHost(navController = navController, startDestination = RouteScreenUserDetail , modifier = modifier) {
        composable<RouteScreenUserDetail>{
            UserDetailsAndDrawerScreen(
                modifier = modifier,
                viewModel = viewModel,
                onNoteClick = {navController.navigate(RouteUpdateNoteScreen(it))},
                onAddNoteClick = {navController.navigate(RouteCreateNoteScreen)},
                onDrawerItemClicked = { drawerItem ->
                    navController.navigate("DrawerItems/${drawerItem}")
                } ,
                onNotificationsIconClicked = { navController.navigate(RouteNotificationScreen) }
            )
        }
        composable(
            "DrawerItems/{selectedDrawer}" ,
            arguments = listOf(navArgument("selectedDrawer"){
                type = NavType.StringType
            })
        ){
            val selectedDrawer = it.arguments?.getString("selectedDrawer") ?: ""
            IndividualDrawerScreen(onBackClick = { navController.navigate(RouteScreenUserDetail)}, drawerItemName = selectedDrawer)
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
