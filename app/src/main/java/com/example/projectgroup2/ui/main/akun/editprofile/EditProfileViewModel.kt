package com.example.projectgroup2.ui.main.akun.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectgroup2.data.api.auth.getUser.GetUserResponse
import com.example.projectgroup2.repository.AuthRepository
import com.example.projectgroup2.utils.reduceFileImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val repoAuth: AuthRepository): ViewModel() {
    val showEditProfile: MutableLiveData<Boolean> = MutableLiveData()
    val showError: MutableLiveData<String> = MutableLiveData()
    val showUser: MutableLiveData<GetUserResponse> = MutableLiveData()

    fun updateUser(
        image: File,
        name: String,
        city: String,
        address: String,
        phoneNumber: String
    ){
        val requestFile = reduceFileImage(image).asRequestBody("image/jpg".toMediaTypeOrNull())
        val profileUser = MultipartBody.Part.createFormData("image", image.name, requestFile)
        val namaRequestBody = name.toRequestBody("text/plain".toMediaType())
        val kotaRequestBody = city.toRequestBody("text/plain".toMediaType())
        val alamatRequestBody = address.toRequestBody("text/plain".toMediaType())
        val noHpRequestBody = phoneNumber.toRequestBody("text/plain".toMediaType())

        CoroutineScope(Dispatchers.IO).launch {
            val result = repoAuth.updateUser(
                token = repoAuth.getToken()!!,
                profileUser,
                namaRequestBody,
                kotaRequestBody,
                alamatRequestBody,
                noHpRequestBody
            )
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    showEditProfile.postValue(true)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }

    fun getUserData(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = repoAuth.getUser(token = repoAuth.getToken()!!)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showUser.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }
}