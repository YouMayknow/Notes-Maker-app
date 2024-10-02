package com.example.limitlife.ui.screen.entryScreen

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.limitlife.R
import com.example.limitlife.ui.theme.LimitLifeTheme

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier ,
    navigateToLoginScreen : () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize() ,    // Light blue background
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(300.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.outlinedCardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CredentialsOfSigningColumn(heading =R.string.signUp , aheadActionLabel = R.string.create_account  ) {

                }
                // Login Link
                TextButton(onClick = navigateToLoginScreen) {
                    Text(
                        text = "Already a user? LOGIN",
                      //  color = Color.Blue,
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
    aheadAction : () -> Unit ,
) {
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
            value = "", // You can manage state here
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Password Input Field
        TextField(
            value = "", // You can manage state here
            onValueChange = {},
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Sign Up Button
        Button(
            onClick =aheadAction ,
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
            Divider(modifier = Modifier.weight(1f), color = Color.Gray)
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
    LimitLifeTheme(darkTheme = true) {
    SignupScreen(navigateToLoginScreen = {})

    }
}

@Preview
@Composable
fun CredentialsOfSigningPreview() {
    LimitLifeTheme(darkTheme = true) {
    CredentialsOfSigningColumn(heading = R.string.signUp , aheadActionLabel = R.string.create_account ){}
    }
}

