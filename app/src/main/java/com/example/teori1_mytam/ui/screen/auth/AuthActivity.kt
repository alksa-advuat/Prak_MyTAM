package com.example.teori1_mytam.ui.screen.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teori1_mytam.data.api.RetrofitClient
import com.example.teori1_mytam.data.repository.AuthRepository
import com.example.teori1_mytam.ui.screen.main.MainActivity
import com.example.teori1_mytam.ui.theme.Teori1_MyTAMTheme
import com.example.teori1_mytam.utils.SessionManager

class AuthActivity : ComponentActivity() {

    private val sessionManager by lazy { SessionManager(this) }

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            AuthRepository(RetrofitClient.instance)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Auto-login: kalau token masih ada langsung ke MainActivity
        if (sessionManager.isLoggedIn()) {
            goToMain()
            return
        }

        enableEdgeToEdge()
        setContent {
            Teori1_MyTAMTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                var showRegister by remember { mutableStateOf(true) }

                // Kalau login/register berhasil → simpan session → pindah ke Main
                LaunchedEffect(uiState) {
                    if (uiState is AuthUiState.Success) {
                        val s = uiState as AuthUiState.Success
                        sessionManager.saveSession(s.token, s.email, s.name)
                        goToMain()
                    }
                }

                if (showRegister) {
                    RegisterScreen(
                        uiState    = uiState,
                        onRegister = { name, email, pass ->
                            viewModel.register(name, email, pass)
                        },
                        onGoToLogin = {
                            viewModel.resetState()
                            showRegister = false
                        }
                    )
                } else {
                    LoginScreen(
                        uiState        = uiState,
                        onLogin        = { email, pass ->
                            viewModel.login(email, pass)
                        },
                        onGuestLogin   = { goToMain() },
                        onGoToRegister = {
                            viewModel.resetState()
                            showRegister = true
                        }
                    )
                }
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}