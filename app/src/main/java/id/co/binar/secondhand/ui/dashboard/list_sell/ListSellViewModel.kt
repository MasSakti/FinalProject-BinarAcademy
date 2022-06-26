package id.co.binar.secondhand.ui.dashboard.list_sell

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.binar.secondhand.data.local.model.SellerProductLocal
import id.co.binar.secondhand.model.seller.product.DeleteProductByIdResponse
import id.co.binar.secondhand.repository.AuthRepository
import id.co.binar.secondhand.repository.SellerRepository
import id.co.binar.secondhand.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListSellViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val sellerRepository: SellerRepository
) : ViewModel() {
    val getAccount = authRepository.getAccount().asLiveData()

    private val _deleteProduct = MutableLiveData<Resource<DeleteProductByIdResponse>>()
    val deleteProduct: LiveData<Resource<DeleteProductByIdResponse>> = _deleteProduct
    fun deleteProduct(id_product: Int) = CoroutineScope(Dispatchers.IO).launch {
        sellerRepository.deleteProduct(id_product).collectLatest {
            _deleteProduct.postValue(it)
        }
    }

    private val _getProduct = MutableLiveData<Resource<List<SellerProductLocal>>>()
    val getProduct: LiveData<Resource<List<SellerProductLocal>>> = _getProduct
    fun getProduct() = CoroutineScope(Dispatchers.IO).launch {
        sellerRepository.getProduct().collectLatest {
            _getProduct.postValue(it)
        }
    }
}