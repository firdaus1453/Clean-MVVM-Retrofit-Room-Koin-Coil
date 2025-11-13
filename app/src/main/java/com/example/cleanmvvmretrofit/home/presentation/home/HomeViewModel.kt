package com.example.cleanmvvmretrofit.home.presentation.home

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

class HomeViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadTodos()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnTodoClick -> {
                state = state.copy(selectedTodoId = action.todoId)
            }
            HomeAction.OnRefresh -> {
                loadTodos()
            }
        }
    }

    fun clearSelectedTodo() {
        state = state.copy(selectedTodoId = null)
    }

    private fun loadTodos() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            when (val result = todoRepository.getTodos()) {
                is Result.Success -> {
                    state = state.copy(
                        isLoading = false,
                        todos = result.data,
                        error = null
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.error.toString()
                    )
                    eventChannel.send(
                        HomeEvent.Error(result.error.toString())
                    )
                }
            }
        }
    }
}