package com.example.cleanmvvmretrofit.home.presentation.home_detail

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cleanmvvmretrofit.core.presentation.designsystem.Background
import com.example.cleanmvvmretrofit.core.presentation.designsystem.Black
import com.example.cleanmvvmretrofit.core.presentation.designsystem.Blue
import com.example.cleanmvvmretrofit.core.presentation.designsystem.BlueDark
import com.example.cleanmvvmretrofit.core.presentation.designsystem.DarkRed
import com.example.cleanmvvmretrofit.core.presentation.designsystem.Gray
import com.example.cleanmvvmretrofit.core.presentation.designsystem.Green
import com.example.cleanmvvmretrofit.core.presentation.designsystem.TextSecondary
import com.example.cleanmvvmretrofit.core.presentation.designsystem.White
import com.example.cleanmvvmretrofit.core.presentation.ui.ObserveAsEvents
import com.example.cleanmvvmretrofit.home.domain.TodosResponse
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RootHomeDetailScreen(
    todoId: Int,
    onBackClick: () -> Unit = {},
    viewModel: HomeDetailViewModel = koinViewModel { parametersOf(todoId) }
) {
    val state = viewModel.state

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            HomeDetailEvent.NavigateBack -> onBackClick()
            is HomeDetailEvent.Error -> {
            }
        }
    }

    HomeDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                HomeDetailAction.OnBackClick -> onBackClick()
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeDetailScreen(
    modifier: Modifier = Modifier,
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Todo Detail",
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(HomeDetailAction.OnBackClick) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Blue
                    )
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Error",
                            tint = DarkRed,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Error: ${state.error}",
                            color = DarkRed,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                state.todo != null -> {
                    TodoDetailContent(
                        todo = state.todo,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun TodoDetailContent(
    todo: TodosResponse,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(Background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Blue,
                            BlueDark
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = White.copy(alpha = 0.2f)
                ) {
                    Icon(
                        imageVector = if (todo.completed) {
                            Icons.Default.CheckCircle
                        } else {
                            Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier
                            .padding(20.dp)
                            .size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (todo.completed) "COMPLETED" else "PENDING",
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            InfoCard(
                title = "ID",
                value = "#${todo.id}",
                icon = Icons.Default.Info
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoCard(
                title = "User ID",
                value = "#${todo.userId}",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "TITLE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = todo.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Black,
                        lineHeight = 26.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (todo.completed) {
                            Icons.Default.CheckCircle
                        } else {
                            Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = if (todo.completed) {
                            Green
                        } else {
                            Gray
                        },
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "STATUS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (todo.completed) "Completed" else "Pending",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (todo.completed) {
                                Green
                            } else {
                                Gray
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Blue.copy(alpha = 0.1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Blue,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Black
                )
            }
        }
    }
}