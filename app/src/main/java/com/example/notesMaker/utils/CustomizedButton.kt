package com.example.notesMaker.utils

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext

@Composable
fun CustomizedButton(
    modifier: Modifier = Modifier,
    duration : Int = 10,
    onButtonClick : () ->  Unit,
    buttonText : @Composable () -> Unit,
    waitingAnimation : @Composable () -> Unit = { CircularProgressIndicator() }
){
    var  disableDuration  = remember { mutableIntStateOf(duration) }
    var  isButtonEnabled =  remember { mutableStateOf(false) }
    LaunchedEffect(isButtonEnabled.value) {
            while (disableDuration.intValue > 0 ){
                delay(1000)
                disableDuration.intValue = disableDuration.intValue -1
                isButtonEnabled.value = true
            }
        disableDuration.intValue = duration
    }
    Column(modifier) {
        Button(
            onClick = {
                if (isButtonEnabled.value){
                    onButtonClick()
                    isButtonEnabled.value = false
                }
            } ,
            enabled = isButtonEnabled.value ,
        ) {
            CircularProgressIndicator(color = Color.Red)
        }
        Text("Resend code in ${disableDuration.intValue}s ")
    }

}


@Composable
@Preview
private fun CustomizedButtonPreview(){
    CustomizedButton(
        duration = 10 ,
        onButtonClick =  {} ,
        buttonText = {Text("Get otp")}
    )
}
