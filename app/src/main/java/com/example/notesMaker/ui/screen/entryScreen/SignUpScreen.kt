package com.example.notesMaker.ui.screen.entryScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.NotesMaker.R
import com.example.notesMaker.ui.theme.NotesMakerTheme

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier ,
    viewModel: SignupScreenViewModel = hiltViewModel() ,
    navigateToMainScreen : () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)) ,    // Light blue background
        contentAlignment = Alignment.Center
    ) {
         val uiState by viewModel.uiState.collectAsState()
        val currentUiScreen = uiState.currentScreen
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.outlinedCardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = uiState.responseToDisplay ?: "" , color = MaterialTheme.colorScheme.error )
                CredentialsOfSigningColumn(heading = currentUiScreen.heading  , aheadActionLabel = currentUiScreen.aheadActionButton  ) { username , password ->
                    viewModel.aheadActionButton(username , password)
                    if (uiState.isLoginSuccess) navigateToMainScreen()
                }
                // Login Link
                TextButton(onClick = { viewModel.navigateScreenButtonAction() }) {
                    Text(
                        text = stringResource(id = currentUiScreen.navigateScreenButtonHeading),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun CredentialsOfSigningColumn(
    modifier : Modifier = Modifier,
    heading : Int ,
    aheadActionLabel : Int ,
    aheadAction : (String , String) -> Unit ,
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = stringResource(id = heading),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 16.dp) ,

        )

        // Email Input Field
        TextField(
            value = userName, // You can manage state here
            onValueChange = {userName =  it},
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) ,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ) ,
            maxLines = 1 ,
        )

        // Password Input Field
        TextField(
            value = password, // You can manage state here
            onValueChange = {password = it},
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) ,
            keyboardActions = KeyboardActions(
                onDone = { aheadAction(userName, password) }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ) ,
            maxLines = 1 ,
        )

        // singup or login button
        Button(
            onClick = { aheadAction(userName, password) } ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)) // Pink button
        ) {
            Text(text = stringResource(id = aheadActionLabel), color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // OR Divider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.Gray
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Social Login Icons (You'll add your icons here)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /* Google action */ }) {
                Image(painter = painterResource(id = R.drawable.ic_facebook ), contentDescription = "Google")
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { /* Facebook action */ }) {
                Image(painter = painterResource(id =R.drawable.ic_linkedin), contentDescription = "Facebook")
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { /* LinkedIn action */ }) {
                Image(painter = painterResource(id =R.drawable.ic_istagram), contentDescription = "LinkedIn")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

}
@Preview
@Composable
fun PreviewSignUpScreen() {
    NotesMakerTheme(darkTheme = true) {
    SignupScreen( navigateToMainScreen = {})

    }
}

@Preview
@Composable
fun CredentialsOfSigningPreview() {
    NotesMakerTheme(darkTheme = true) {
    CredentialsOfSigningColumn(heading = R.string.signUp , aheadActionLabel = R.string.create_account ) {
            _ , _ ->
    }
    }
}

