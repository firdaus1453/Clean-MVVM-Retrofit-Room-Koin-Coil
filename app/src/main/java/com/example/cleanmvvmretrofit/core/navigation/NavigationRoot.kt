package com.example.cleanmvvmretrofit.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cleanmvvmretrofit.home.domain.UjianWithStudents
import com.example.cleanmvvmretrofit.home.presentation.AcademicSystemApp

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.Home else Routes.Home
    ) {
//        authGraph(navController)
        homeGraph(navController)
    }
}

private fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    composable<Routes.Home> {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AcademicSystemApp(modifier = Modifier.padding(innerPadding))
        }
    }
}

/*private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<Routes.Auth>(
        startDestination = Routes.Login,
    ) {
        composable<Routes.Register> {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate(Routes.Login) {
                        NavOptionsBuilder.popUpTo(Routes.Register) {
                            PopUpToBuilder.inclusive = true
                            PopUpToBuilder.saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate(Routes.Login)
                }
            )
        }
        composable<Routes.Login> {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(Routes.Home) {
                        NavOptionsBuilder.popUpTo(Routes.Auth) {
                            PopUpToBuilder.inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Routes.Register) {
                        NavOptionsBuilder.popUpTo(Routes.Login) {
                            PopUpToBuilder.saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    composable<Routes.Home> {
        BottomNavigationScreen(
            onLogout = {
                navController.navigate(Routes.Auth) {
                    NavOptionsBuilder.popUpTo(Routes.Home) {
                        PopUpToBuilder.inclusive = true
                    }
                }
            },
            onNavigateToDetail = { pokemonId ->
                navController.navigate(
                    Routes.DetailHome(
                        pokemonId = pokemonId
                    )
                )
            }
        )
    }

    composable<Routes.DetailHome> {
        val args = it.toRoute<Routes.DetailHome>()
        HomeDetailScreenRoot(
            pokemonId = args.pokemonId,
            onBackClick = {
                navController.navigateUp()
            }
        )
    }
}*/
