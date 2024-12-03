package com.example.notesMaker.utils

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun CustomizedOutlineTextField(
    value : String,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier  ,
    label : @Composable () -> Unit = {},
    placeholder : @Composable () -> Unit = {},
    isError : Boolean = false ,
    visualTransformation : VisualTransformation  =  VisualTransformation.None ,
    trailingIcon :  @Composable () -> Unit = {},
    keyboardAction : KeyboardActions = KeyboardActions { KeyboardActions.Default.onNext }

){
    OutlinedTextField(
        value = value ,
        onValueChange = onValueChange,
        singleLine = true ,
        label = label ,
        shape = MaterialTheme.shapes.small ,
        placeholder = placeholder  ,
        isError = isError  ,
        modifier = modifier ,
        visualTransformation = visualTransformation ,
        trailingIcon = trailingIcon ,
        keyboardActions = keyboardAction
    )
}