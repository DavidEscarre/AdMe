package cat.copernic.project3_group4.user_management.ui.screens

import RegisterViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.project3_group4.R
import cat.copernic.project3_group4.core.ui.theme.BrownTertiary
import cat.copernic.project3_group4.core.ui.theme.OrangePrimary
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navController: NavController) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        registerViewModel.selectedImageUri.value = uri
    }

    // Reinicia los valores cuando se entra en la pantalla
    LaunchedEffect(Unit) {
        registerViewModel.name.value = ""
        registerViewModel.email.value = ""
        registerViewModel.phone.value = ""
        registerViewModel.password.value = ""
        registerViewModel.confirmPassword.value = ""
        registerViewModel.selectedImageUri.value = null
    }

    Box(modifier = Modifier.fillMaxSize().background(OrangePrimary), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            IconButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier.padding(start = 16.dp, top = 16.dp).size(48.dp).align(Alignment.Start).offset(y = -20.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
            }

            registerViewModel.selectedImageUri.value?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = stringResource(R.string.profile_image),
                    modifier = Modifier.size(100.dp).clip(RoundedCornerShape(50.dp)).background(White).clickable { launcher.launch("image/*") },
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.blank_profile_picture_973460_960_720),
                contentDescription = stringResource(R.string.select_image),
                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(50.dp)).clickable { launcher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(32.dp))
            InputField(stringResource(R.string.name), registerViewModel.name.value, { registerViewModel.name.value = it },
                registerViewModel.nameError.value, 60)
            Spacer(modifier = Modifier.height(16.dp))
            InputField(stringResource(R.string.email), registerViewModel.email.value, { registerViewModel.email.value = it },
                registerViewModel.emailError.value, 60)
            Spacer(modifier = Modifier.height(16.dp))
            InputField(stringResource(R.string.phone), registerViewModel.phone.value, { registerViewModel.phone.value = it },
                registerViewModel.phoneError.value, 15)
            Spacer(modifier = Modifier.height(16.dp))
            InputField(stringResource(R.string.password), registerViewModel.password.value, { registerViewModel.password.value = it },
                registerViewModel.passwordError.value, 20, true)
            Spacer(modifier = Modifier.height(16.dp))
            InputField(stringResource(R.string.confirm_password), registerViewModel.confirmPassword.value, { registerViewModel.confirmPassword.value = it },
                registerViewModel.confirmPasswordError.value, 20, true)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { registerViewModel.register(context, navController) },
                colors = ButtonDefaults.buttonColors(containerColor = BrownTertiary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)) {
                Text(stringResource(R.string.register))
            }
        }
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, error: String?, maxLength: Int, isPassword: Boolean = false) {
    Column {
        TextField(
            value = value,
            onValueChange = {
                if (it.length <= maxLength) onValueChange(it)
            },
            label = {
                Text("$label (${value.length}/$maxLength)")
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .background(Color(0xFFFFCDD2), shape = RoundedCornerShape(8.dp)) // Fondo rojo claro
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(8.dp)) // Borde rojo
                    .padding(8.dp) // Espaciado interno
            ) {
                Text(
                    text = error,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


