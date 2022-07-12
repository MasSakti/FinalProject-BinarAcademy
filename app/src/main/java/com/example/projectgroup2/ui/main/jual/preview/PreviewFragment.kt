package com.example.projectgroup2.ui.main.jual.preview

import android.app.ActionBar
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.FragmentPreviewBinding
import com.example.projectgroup2.ui.main.jual.JualFragment.Companion.ALAMAT_PRODUCT_KEY
import com.example.projectgroup2.ui.main.jual.JualFragment.Companion.DESKRIPSI_PRODUCT_KEY
import com.example.projectgroup2.ui.main.jual.JualFragment.Companion.HARGA_PRODUCT_KEY
import com.example.projectgroup2.ui.main.jual.JualFragment.Companion.IMAGE_PRODUCT_KEY
import com.example.projectgroup2.ui.main.jual.JualFragment.Companion.KATEGORI_PRODUCT_KEY
import com.example.projectgroup2.ui.main.jual.JualFragment.Companion.NAMA_PRODUCT_KEY
import com.example.projectgroup2.utils.listCategoryId
import com.example.projectgroup2.utils.uriToFile
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewFragment : Fragment() {
    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PreviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindView()

        viewModel.getUserData()
    }

    private fun bindViewModel(){
        viewModel.showSuccess.observe(viewLifecycleOwner){
            showToastSuccess()
            findNavController().navigate(R.id.action_previewFragment_to_daftarJualFragment)
        }
    }

    private fun bindView(){
        val bundle = arguments
        val namaProduk = bundle?.getString(NAMA_PRODUCT_KEY)
        val hargaProduk = bundle?.getString(HARGA_PRODUCT_KEY)
        val deskripsiProduk = bundle?.getString(DESKRIPSI_PRODUCT_KEY)
        val imageProduk = bundle?.getString(IMAGE_PRODUCT_KEY)
        val kategoriProduk = bundle?.getString(KATEGORI_PRODUCT_KEY)
        val alamatProduk = bundle?.getString(ALAMAT_PRODUCT_KEY)
//        val userName = bundle?.getString(NAME_USER_KEY)
//        val userAddress = bundle?.getString(ADDRESS_USER_KEY)
//        val userImage = bundle?.getString(IMAGE_USER_KEY)

        viewModel.showUser.observe(viewLifecycleOwner){
            binding.tvPreviewNamaPenjual.text = it.fullName
            binding.tvPreviewKotaPenjual.text = it.address
            Glide.with(requireContext())
                .load(it.imageUrl)
                .into(binding.rivPreviewPhotoPenjual)
        }

        binding.tvProductName.text = namaProduk
        binding.tvProductharga.text = hargaProduk
        binding.tvDeskripsiProduct.text = deskripsiProduk
        binding.tvProductCategory.text = kategoriProduk
        Glide.with(requireContext())
            .load(imageProduk)
            .centerCrop()
            .into(binding.ivProductPreview)

        binding.btnTerbitkan.setOnClickListener {
            viewModel.uploadProduct(
                namaProduk.toString(),
                deskripsiProduk.toString(),
                hargaProduk.toString(),
                listCategoryId,
                alamatProduk.toString(),
                uriToFile(Uri.parse(imageProduk), requireContext())
            )
        }
    }

    private fun showToastSuccess() {
        val snackBarView =
            Snackbar.make(binding.root, "Produk berhasil di terbitkan.", Snackbar.LENGTH_LONG)
        val layoutParams = ActionBar.LayoutParams(snackBarView.view.layoutParams)
        snackBarView.setAction(" ") {
            snackBarView.dismiss()
        }
        val textView =
            snackBarView.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_close_24, 0)
        textView.compoundDrawablePadding = 16
        layoutParams.gravity = Gravity.TOP
        layoutParams.setMargins(32, 150, 32, 0)
        snackBarView.view.setPadding(24, 16, 0, 16)
        snackBarView.view.setBackgroundColor(resources.getColor(R.color.success))
        snackBarView.view.layoutParams = layoutParams
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }
}