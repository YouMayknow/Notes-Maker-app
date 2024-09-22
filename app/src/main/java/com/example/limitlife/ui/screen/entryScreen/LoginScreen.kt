package com.example.limitlife.ui.screen.entryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.limitlife.R

@Composable
fun LoginScreen (
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))  ,    // Light blue background
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(300.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.outlinedCardElevation(4.dp)
        ) {
            CredentialsOfSigningColumn(
                heading = R.string.Login,
                aheadActionLabel = R.string.login_account
            ) {

            }
        }
    }
}

@Composable
@Preview
fun CredentialsOfSigningColumnPreview() {
    LoginScreen()
}