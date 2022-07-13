package id.co.binar.secondhand.model.buyer.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import id.co.binar.secondhand.model.seller.category.GetCategoryResponse
import id.co.binar.secondhand.model.seller.order.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Items(

	@SerializedName("page")
	val page: Long? = null,

	@SerializedName("per_page")
	val per_page: Long? = null,

	@SerializedName("data")
	val data: List<GetProductResponse>? = null
) : Parcelable

@Parcelize
data class GetProductResponse(

	@SerializedName("image_name")
	val imageName: String? = null,

	@SerializedName("updated_at")
	val updatedAt: String? = null,

	@SerializedName("user_id")
	val userId: Int? = null,

	@SerializedName("image_url")
	val imageUrl: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("base_price")
	val basePrice: Long? = null,

	@SerializedName("created_at")
	val createdAt: String? = null,

	@SerializedName("location")
	val location: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("User")
	val user: User? = null,

	@SerializedName("Categories")
	val categories: List<GetCategoryResponse>? = null
) : Parcelable
