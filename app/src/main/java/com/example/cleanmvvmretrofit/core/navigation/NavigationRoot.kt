@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.example.cleanmvvmretrofit.core.navigation

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.cleanmvvmretrofit.core.presentation.ui.ObserveAsEvents
import com.example.cleanmvvmretrofit.home.presentation.home.HomeAction
import com.example.cleanmvvmretrofit.home.presentation.home.HomeEvent
import com.example.cleanmvvmretrofit.home.presentation.home.HomeViewModel
import com.example.cleanmvvmretrofit.home.presentation.home.RootHomeScreen
import com.example.cleanmvvmretrofit.home.presentation.home_detail.RootHomeDetailScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is HomeEvent.Error -> {
                Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                RootHomeScreen(
                    onItemClick = { todoId ->
                        viewModel.onAction(HomeAction.OnTodoClick(todoId))
                        scope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail
                            )
                        }
                    },
                    viewModel = viewModel
                )
            }
        },
        detailPane = {
            AnimatedPane {
                state.selectedTodoId?.let { todoId ->
                    RootHomeDetailScreen(
                        todoId = todoId,
                        onBackClick = {
                            backDispatcher?.onBackPressed()
                        }
                    )
                }
            }
        },
        modifier = modifier
    )
}
