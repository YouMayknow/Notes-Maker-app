package com.example.notesMaker.ui.screen.mainScreen

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.repository.OfflineUserDataRepository
import com.example.notesMaker.repository.WorkManagerRepository
import com.example.notesMaker.ui.screen.entryScreen.SignupScreen
import com.example.notesMaker.ui.screen.noteScreen.CreateNoteScreen
import com.example.notesMaker.ui.screen.noteScreen.UpdateNoteScreen
import com.example.notesMaker.ui.screen.notification.Notification
import com.example.notesMaker.ui.screen.notification.NotificationScreen
import com.example.notesMaker.worker.LOGGING_OF_APP
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject


/*it is a main  navigation screen that have access of all the screen except entry screen it takes to notesListAndDetail
screen that have the both our screen .
on this basis of the click it prepares the drawer based screen for the user using individual screen
 */
@Composable
fun  MainScreenNavigation (
    modifier: Modifier = Modifier ,
    navController: NavHostController = rememberNavController()  ,
    viewModel: MainScreenNavigationViewModel = hiltViewModel() ,
) {
    val uiState =  viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
            viewModel.checkLoginRequirement()
        }
        if ((uiState.value.isNewUser == null || uiState.value.isTokenValid == null)  && uiState.value.isNetworkAvailable == null ){
            CircularProgressIndicator()
        }
        else {
            NavHost(
                navController = navController,
                startDestination = MainScreen , // here the start destination will differ on the basis of the viewmodl need to implement
                modifier = modifier
            ) {
                navigation<MainScreen>(RouteScreenUserDetail){
                    composable<RouteScreenUserDetail>{
                        UserDetailsAndDrawerScreen(
                            modifier = modifier,
//                    viewModel = viewModel,
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
                navigation<EntryScreen>(SignUpScreen){
                    composable<SignUpScreen>{
                        SignupScreen(navigateToMainScreen = {navController.navigate(MainScreen)} , modifier =  modifier )
                    }
                }
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

@Serializable object EntryScreen
@Serializable object SignUpScreen
@Serializable object MainScreen

@HiltViewModel
class NotificationScreenVIewModel  @Inject constructor(
    val workManagerRepository: WorkManagerRepository,
    val localDataRepository: OfflineUserDataRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
   private val _uiState= MutableStateFlow(NotificationScreenUiState())
    val uiState = _uiState.asStateFlow()
    fun checkForUnSyncedNotes() = viewModelScope.launch {
        Log.e(LOGGING_OF_APP, "Checking for unsynced notes")
        _uiState.update { it.copy(isSyncing = true) }
        val unSyncedNotes =  localDataRepository.getUnSyncedNotes()
        if (unSyncedNotes.isEmpty()){
            Log.e(LOGGING_OF_APP, "no unsynced notes found")
            _uiState.update { it.copy(isSyncedFailed = false , isSyncing = false) }
        }
        else {
            _uiState.update { it.copy(isSyncedFailed = true)}
          val notification =   createRetryNotification()
            Log.e(LOGGING_OF_APP, "Notification created")
            _uiState.update { it.copy(notifications = listOf(notification) , isSyncing = false) }
        }
    }
    fun createRetryNotification() : Notification {
        Log.e(LOGGING_OF_APP, "Creating a notification along with retrying the notes")
        return Notification(
            heading = "Sync failed",
            body = "Failed to sync notes with id ",
            image = Icons.Default.Refresh,
            time = "10 minutes before",
            action = {
                retryAction()
            }
        )
    }
    fun retryAction() = viewModelScope.launch(){
        val unSyncedNotes = localDataRepository.getUnSyncedNotes()
        Log.e(LOGGING_OF_APP, "worker came to retry function")
        for (unSyncedNote in unSyncedNotes) {
            Log.e(LOGGING_OF_APP, "Retrying to sync notes with id ${unSyncedNote.id} and heading : ${unSyncedNote.heading}")
            viewModelScope.launch{
                if (unSyncedNote.noteId == null || unSyncedNote.noteId == -1) {
                    Log.e(LOGGING_OF_APP, "Retrying to save notes with heading ${unSyncedNote.heading}")
                    workManagerRepository.saveNote(unSyncedNote.heading ?: "" , unSyncedNote.content ?: "")
                } else {
                    Log.e(LOGGING_OF_APP, "Retrying to update notes with heading ${unSyncedNote.heading}")
                    workManagerRepository.updateNote(
                        UpdatedShortNote(
                            content = unSyncedNote.content ?: "",
                            heading = unSyncedNote.heading ?: "",
                            id = unSyncedNote.noteId,
                            lastUpdated = unSyncedNote.lastUpdated ?: "",
                            version = unSyncedNote.version
                        )
                    )
                }
            }
        }
    }
}

data class NotificationScreenUiState(
    val isSyncing : Boolean? = null,
    val notifications : List<Notification> = emptyList(),
    val isSyncedFailed : Boolean? = null,
)