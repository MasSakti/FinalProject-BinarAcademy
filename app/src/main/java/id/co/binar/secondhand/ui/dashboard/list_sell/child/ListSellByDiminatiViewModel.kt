package id.co.binar.secondhand.ui.dashboard.list_sell.child

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.binar.secondhand.model.seller.order.GetOrderResponse
import id.co.binar.secondhand.repository.SellerRepository
import id.co.binar.secondhand.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListSellByDiminatiViewModel @Inject constructor(
    private val sellerRepository: SellerRepository
) : ViewModel() {
    private val _getOrder = MutableLiveData<Resource<List<GetOrderResponse>>>()
    val getOrder : LiveData<Resource<List<GetOrderResponse>>> = _getOrder
    fun getOrder() = CoroutineScope(Dispatchers.IO).launch {
        sellerRepository.getOrder().collectLatest {
            _getOrder.postValue(it)
        }
    }
}