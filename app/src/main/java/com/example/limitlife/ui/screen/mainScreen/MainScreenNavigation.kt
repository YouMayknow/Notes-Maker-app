package com.example.limitlife.ui.screen.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
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
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            var shouldRefresh by remember { mutableStateOf(false) }
             savedStateHandle?.getLiveData<Boolean>("shouldRefresh")?.observe(
                LocalLifecycleOwner.current) {
                 shouldRefresh = it ?: false
            }
            UserDetailsAndDrawerScreen(
                shouldRefresh = shouldRefresh,
                viewModel = viewModel,
                onNoteClick = {navController.navigate(RouteEditNoteScreen(it))},
                onAddNoteClick = {navController.navigate(RouteCreateNoteScreen)},
                turnShouldRefreshFalse = {navController.previousBackStackEntry?.savedStateHandle?.set("shouldRefresh" , false)},
                onDrawerItemClicked = { drawerItem ->
                    navController.navigate("DrawerItems/${drawerItem}")
                },
                modifier = modifier
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
              onSaveNoteClick = {
                  navController.navigateUp()
                                val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
                  savedStateHandle?.set("shouldRefresh" , true )
              },
              shortNote = args.shortNote,
              onBackPressed = {navController.navigateUp()}
          )
        }
        composable<RouteCreateNoteScreen> {
            EditNoteScreen(
                onSaveNoteClick = {
                    navController.navigateUp()
                    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
                    savedStateHandle?.set("shouldRefresh" , true )
                },
                onBackPressed =  {navController.navigateUp()})
        }
    }
}

@Serializable
object RouteScreenUserDetail
@Serializable
data class RouteEditNoteScreen(val shortNote: String = "")

@Serializable
object RouteCreateNoteScreen
