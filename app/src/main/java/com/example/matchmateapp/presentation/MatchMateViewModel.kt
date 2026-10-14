package com.example.matchmateapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.matchmateapp.domain.model.MatchStatus
import com.example.matchmateapp.domain.usecase.GetMatchesUseCase
import com.example.matchmateapp.domain.usecase.UpdateMatchStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchMateViewModel @Inject constructor(
    getMatchesUseCase: GetMatchesUseCase,
    private val updateMatchStatusUseCase: UpdateMatchStatusUseCase
) : ViewModel() {

    val matches = getMatchesUseCase().cachedIn(viewModelScope)

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun updateStatus(userId: String, status: MatchStatus) {
        viewModelScope.launch {
            updateMatchStatusUseCase(userId, status)
            _message.value = if (status == MatchStatus.ACCEPTED) {
                "Accepted"
            } else {
                "Declined"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
