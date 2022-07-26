package binar.and3.kelompok1.secondhand.ui.menu.daftarjual.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import binar.and3.kelompok1.secondhand.data.api.seller.GetProductByIdResponse
import binar.and3.kelompok1.secondhand.data.api.seller.GetProductResponse
import binar.and3.kelompok1.secondhand.data.api.seller.GetSellerOrdersResponse
import binar.and3.kelompok1.secondhand.repository.AuthRepository
import binar.and3.kelompok1.secondhand.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DaftarJualItemViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val shouldShowMyProduct: MutableLiveData<List<GetProductResponse>> = MutableLiveData()
    val shouldShowSellerOrder: MutableLiveData<List<GetSellerOrdersResponse>> = MutableLiveData()
    val shouldShowError: MutableLiveData<String> = MutableLiveData()

    fun onViewLoaded() {
        getMyProduct()
    }

    private fun getMyProduct() {
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = authRepository.getToken().toString()
            val result = productRepository.getSellerProduct(accessToken = accessToken)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    shouldShowMyProduct.postValue(result.body())
                } else {
                    shouldShowError.postValue(result.errorBody().toString())
                }
            }
        }
    }

    fun getSellerOder() {
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = authRepository.getToken().toString()
            val result = productRepository.getSellerOrders(accessToken = accessToken)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    shouldShowSellerOrder.postValue(result.body())
                } else {
                    shouldShowError.postValue(result.errorBody().toString())
                }
            }
        }
    }
}