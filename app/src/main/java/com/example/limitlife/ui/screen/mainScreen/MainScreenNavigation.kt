package com.example.limitlife.ui.screen.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.limitlife.model.defaultNote
import com.example.limitlife.network.ShortNote
import kotlinx.serialization.Serializable


/*it is a main  navigation screen that have access of all the screen except entry screen it takes to notesListAndDetail
screen that have the both our screen .
on this basis of the click it prepares the drawer based screen for the user using individual screen
 */
@Composable
fun  MainScreenNavigation (
    navController: NavHostController = rememberNavController()  ,
    modifier: Modifier = Modifier ,
) {
    NavHost(navController = navController, startDestination = RouteScreenUserDetail , modifier = modifier) {
        composable<RouteScreenUserDetail>{
            UserDetailsAndDrawerScreen(
                modifier = modifier,
                onAddNoteClick = {navController.navigate(RouteCreateNoteScreen)} ,
                onNoteClick = {navController.navigate(RouteEditNoteScreen(it))},
                onDrawerItemClicked = { drawerItem ->
                    navController.navigate("DrawerItems/${drawerItem}")
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
            IndividualDrawerScreen(onBackClick = { navController.navigate(RouteScreenUserDetail)}, drawerItemName = selectedDrawer)
        }
        composable<RouteEditNoteScreen> {
            val args =  it.toRoute<RouteEditNoteScreen>()
          EditNoteScreen(
                   shortNote = args.shortNote,
                   navigateToNotesListScreen = {navController.navigate(RouteScreenUserDetail)}
          )
        }
        composable<RouteCreateNoteScreen> {
            EditNoteScreen(
                navigateToNotesListScreen = {navController.navigate(RouteScreenUserDetail)})
        }
    }
}

@Serializable
object RouteScreenUserDetail
@Serializable
data class RouteEditNoteScreen(val shortNote: String = "")

@Serializable
object RouteCreateNoteScreen
