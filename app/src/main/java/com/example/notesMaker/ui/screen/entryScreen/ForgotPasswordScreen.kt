package com.example.notesMaker.ui.screen.entryScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier , 
    onBackToLoginClick: () -> Unit,
    onSendCodeClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "<  Back to Login",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .clickable { onBackToLoginClick() }
                .padding(bottom = 16.dp)
        )
        Text(
            text = "Forgot Password",
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Insert your email address to receive a code for creating a new password",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Button(
            onClick = onSendCodeClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Send Code")
        }
    }
}

@Composable
@Preview
private  fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(modifier = Modifier , {}) { }
}