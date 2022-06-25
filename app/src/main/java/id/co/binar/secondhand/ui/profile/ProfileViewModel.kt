package id.co.binar.secondhand.ui.profile

import android.graphics.Bitmap
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.binar.secondhand.data.local.AuthDao
import id.co.binar.secondhand.data.local.model.AuthLocal
import id.co.binar.secondhand.model.auth.*
import id.co.binar.secondhand.repository.AuthRepository
import id.co.binar.secondhand.util.DataStoreManager
import id.co.binar.secondhand.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val _field = state.getLiveData<AddAuthRequest>("FIELD")
    val field: LiveData<AddAuthRequest> = _field
    fun field(field: AddAuthRequest) {
        _field.postValue(field)
    }

    private val _bitmap = state.getLiveData<Bitmap>("BITMAP")
    val bitmap: LiveData<Bitmap> = _bitmap
    fun bitmap(bitmap: Bitmap) {
        _bitmap.postValue(bitmap)
    }

    private val _register = MutableLiveData<Resource<AddAuthResponse>>()
    val register : LiveData<Resource<AddAuthResponse>> = _register
    fun register(field: AddAuthRequest, image: MultipartBody.Part) = CoroutineScope(Dispatchers.IO).launch {
        authRepository.register(field, image).collectLatest {
            _register.postValue(it)
        }
    }

    private val _updateAccount = MutableLiveData<Resource<UpdateAuthByTokenResponse>>()
    val updateAccount : LiveData<Resource<UpdateAuthByTokenResponse>> = _updateAccount
    fun updateAccount(field: UpdateAuthByTokenRequest, image: MultipartBody.Part) = CoroutineScope(Dispatchers.IO).launch {
        authRepository.updateAccount(field, image).collectLatest {
            _updateAccount.postValue(it)
        }
    }

    val getAccount = authRepository.getAccount().asLiveData()
}