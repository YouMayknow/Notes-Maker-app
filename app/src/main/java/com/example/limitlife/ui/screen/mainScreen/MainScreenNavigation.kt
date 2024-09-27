package com.example.limitlife.ui.screen.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable

@Composable
fun  MainScreenNavigation (
    navController: NavHostController = rememberNavController()  ,
    modifier: Modifier = Modifier ,
) {
    NavHost(navController = navController, startDestination = ScreenUserDetail , modifier = modifier) {
        composable<ScreenUserDetail>{
            UserDetailedScreen(
                onDrawerItemClicked = {drawerItem ->
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
            IndividualDrawerScreen(onBackClick = { navController.navigate(ScreenUserDetail)}, drawerItemName = selectedDrawer)
        }
    }
}

@Serializable
object ScreenUserDetail

