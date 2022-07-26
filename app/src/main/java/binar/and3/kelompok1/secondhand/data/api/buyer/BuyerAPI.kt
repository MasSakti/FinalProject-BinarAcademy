package binar.and3.kelompok1.secondhand.data.api.buyer

import binar.and3.kelompok1.secondhand.data.api.seller.GetProductByIdResponse
import binar.and3.kelompok1.secondhand.data.api.seller.PostProductResponse
import retrofit2.Response
import retrofit2.http.*

interface BuyerAPI {
    @GET("buyer/product")
    suspend fun getBuyerProduct(
        @Query("status") status: String? = null,
        @Query("category_id") categoryId: String? = null,
        @Query("search") search: String? = null
    ): Response<List<BuyerProductResponse>>

    @GET("buyer/product/{id}")
    suspend fun getBuyerProductById(
        @Path("id") id: Int
    ): Response<GetProductByIdResponse>

    @POST("buyer/order")
    suspend fun postBuyerBid(
        @Header("access_token") accessToken: String,
        @Body postBuyerBidRequest: PostBuyerBidRequest
    ): Response<PostBuyerBidResponse>
}