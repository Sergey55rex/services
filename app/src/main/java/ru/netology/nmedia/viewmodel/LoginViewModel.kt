package ru.netology.nmedia.viewmodel

import android.app.Application
import android.content.Context
import android.text.Editable

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl


class LoginViewModel (application: Application) : AndroidViewModel(application){
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao(), AppDb.getInstance(context = application).postWorkDao())




    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    fun onLogin(login: Editable, pass: Editable) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.updateUser(login, pass)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }
}