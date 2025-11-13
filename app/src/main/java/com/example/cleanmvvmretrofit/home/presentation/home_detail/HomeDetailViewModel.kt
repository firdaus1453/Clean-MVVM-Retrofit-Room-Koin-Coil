package com.example.cleanmvvmretrofit.home.presentation.home_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanmvvmretrofit.core.domain.util.Result
import com.example.cleanmvvmretrofit.home.domain.TodoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeDetailViewModel(
    private val todoRepository: TodoRepository,
    private val todoId: Int
) : ViewModel() {

    var state by mutableStateOf(HomeDetailState())
        private set

    private val eventChannel = Channel<HomeDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadTodoDetail()
    }

    fun onAction(action: HomeDetailAction) {
        when (action) {
            HomeDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(HomeDetailEvent.NavigateBack)
                }
            }
        }
    }

    fun reloadTodoDetail(newTodoId: Int) {
        loadTodoDetail(newTodoId)
    }

    private fun loadTodoDetail(id: Int = todoId) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            when (val result = todoRepository.getTodoDetail(id)) {
                is Result.Success -> {
                    state = state.copy(
                        isLoading = false,
                        todo = result.data,
                        error = null
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.error.toString()
                    )
                    eventChannel.send(
                        HomeDetailEvent.Error(result.error.toString())
                    )
                }
            }
        }
    }
}