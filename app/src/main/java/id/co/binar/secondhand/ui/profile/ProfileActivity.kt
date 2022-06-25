package id.co.binar.secondhand.ui.profile

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import coil.load
import coil.size.ViewSizeResolver
import com.github.dhaval2404.imagepicker.ImagePicker
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.EasyPermissions.somePermissionPermanentlyDenied
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import id.co.binar.secondhand.R
import id.co.binar.secondhand.databinding.ActivityProfileBinding
import id.co.binar.secondhand.model.auth.AddAuthRequest
import id.co.binar.secondhand.model.auth.UpdateAuthByTokenRequest
import id.co.binar.secondhand.util.*
import io.github.anderscheow.validator.Validator
import io.github.anderscheow.validator.constant.Mode
import io.github.anderscheow.validator.validator

const val PASSING_FROM_REGISTER_TO_PROFILE = "PASSING_FROM_REGISTER_TO_PROFILE"
const val PASSING_FROM_ACCOUNT_TO_PROFILE = "PASSING_FROM_ACCOUNT_TO_PROFILE"

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Lengkapi Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        bindObserver()
        bindView()
    }

    private fun bindObserver() {
        viewModel.updateAccount.observe(this) {
            when(it) {
                is Resource.Success -> {
                    this.onToast("Data berhasil diupdate")
                    onBackPressed()
                }
                is Resource.Loading -> {
                    this.onToast("Mohon menunggu...")
                }
                is Resource.Error -> {
                    this.onSnackError(binding.root, it.error?.message.toString())
                }
            }
        }
        viewModel.register.observe(this) {
            when(it) {
                is Resource.Success -> {
                    this.onToast("Data berhasil disimpan")
                    onBackPressed()
                }
                is Resource.Loading -> {
                    this.onToast("Mohon menunggu...")
                }
                is Resource.Error -> {
                    this.onSnackError(binding.root, it.error?.message.toString())
                }
            }
        }
        viewModel.bitmap.observe(this) {
            it?.let { bmp ->
                binding.ivImageProfile.setImageBitmap(bmp)
            }
        }
    }

    private fun bindView() {
        if (intent.hasExtra(PASSING_FROM_ACCOUNT_TO_PROFILE)) {
            intent.extras?.getBoolean(PASSING_FROM_ACCOUNT_TO_PROFILE)?.let {
                if (it) {
                    binding.txtInputLayoutEmail.visibility = View.VISIBLE
                    binding.txtInputLayoutPassword.visibility = View.VISIBLE
                    viewModel.getAccount.observe(this) {
                        binding.apply {
                            ivImageProfile.load(it.data?.imageUrl) {
                                placeholder(R.drawable.ic_profile_image)
                                error(R.drawable.ic_profile_image)
                                size(ViewSizeResolver(binding.ivImageProfile))
                            }
                            viewModel.bitmap(binding.ivImageProfile.drawable.toBitmap())
                            txtInputEmail.setText(it.data?.email)
                            txtInputLayoutNama.setText(it.data?.fullName)
                            txtInputLayoutKota.setText(it.data?.city)
                            txtInputLayoutAlamat.setText(it.data?.address)
                            txtInputLayoutNoHandphone.setText(it.data?.phoneNumber.toString())
                        }
                        when (it) {
                            is Resource.Success -> {}
                            is Resource.Loading -> {}
                            is Resource.Error -> {
                                this.onSnackError(binding.root, it.error?.message.toString())
                            }
                        }
                    }
                }
            }
        } else if (intent.hasExtra(PASSING_FROM_REGISTER_TO_PROFILE)) {
            intent.extras?.getParcelable<AddAuthRequest>(PASSING_FROM_REGISTER_TO_PROFILE)?.let {
                viewModel.field(it)
                binding.txtInputLayoutNama.setText(it.fullName)
            }
        }

        binding.toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24)
        binding.ivImageProfile.setOnClickListener {
            requestCameraAndWriteExternalPermission(this)
        }
        binding.btnDaftar.setOnClickListener {
            onValidate()
        }
    }

    private val choosePhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val resultCode = it.resultCode
        val data = it.data
        try {
            when (resultCode) {
                RESULT_OK -> {
                    data?.let { intent ->
                        binding.ivImageProfile.setImageURI(intent.data)
                        val drawable = binding.ivImageProfile.drawable.toBitmap()
                        viewModel.bitmap(drawable)
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    throw Exception(ImagePicker.getError(data))
                }
            }
        } catch (ex: Exception) {
            this@ProfileActivity.onToast(ex.message.toString())
        }
    }

    private fun choosePhotoPermission() {
        ImagePicker.with(this)
            .galleryMimeTypes(
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            )
            .cropSquare()
            .compress(2048)
            .maxResultSize(2048, 2048)
            .createIntent {
                choosePhoto.launch(it)
            }
    }

    private fun onValidate() {
        if (intent.hasExtra(PASSING_FROM_REGISTER_TO_PROFILE)) {
            validator(this) {
                mode = Mode.SINGLE
                listener = onProfileSubmit
                validate(
                    generalValid(binding.etNamaProfile),
                    generalValid(binding.etKotaProfile),
                    generalValid(binding.etAlamatProfile),
                    phoneValid(binding.etNoHandphoneProfile)
                )
            }
        } else if (intent.hasExtra(PASSING_FROM_ACCOUNT_TO_PROFILE)) {
            validator(this) {
                mode = Mode.SINGLE
                listener = onProfileSubmit
                validate(
                    emailValid(binding.txtInputLayoutEmail),
                    passwordValid(binding.txtInputLayoutPassword),
                    generalValid(binding.etNamaProfile),
                    generalValid(binding.etKotaProfile),
                    generalValid(binding.etAlamatProfile),
                    phoneValid(binding.etNoHandphoneProfile)
                )
            }
        }
    }

    private val onProfileSubmit = object : Validator.OnValidateListener {
        override fun onValidateSuccess(values: List<String>) {
            val bitmap = viewModel.bitmap.value
            if (bitmap == null) {
                this@ProfileActivity.onSnackError(binding.root, "File gambar tidak boleh kosong!")
            } else {
                if (intent.hasExtra(PASSING_FROM_REGISTER_TO_PROFILE)) {
                    val it = viewModel.field.value
                    viewModel.register(
                        AddAuthRequest(
                            email = it?.email,
                            password = it?.password,
                            fullName = binding.txtInputLayoutNama.text.toString(),
                            phoneNumber = binding.txtInputLayoutNoHandphone.text.toString().toLong(),
                            address = binding.txtInputLayoutAlamat.text.toString(),
                            city = binding.txtInputLayoutKota.text.toString()
                        ),
                        this@ProfileActivity.buildImageMultipart("image", bitmap)
                    )
                } else if (intent.hasExtra(PASSING_FROM_ACCOUNT_TO_PROFILE)) {
                    viewModel.updateAccount(
                        UpdateAuthByTokenRequest(
                            password = binding.txtInputPassword.text.toString(),
                            email = binding.txtInputEmail.text.toString(),
                            fullName = binding.txtInputLayoutNama.text.toString(),
                            phoneNumber = binding.txtInputLayoutNoHandphone.text.toString().toLong(),
                            address = binding.txtInputLayoutAlamat.text.toString(),
                            city = binding.txtInputLayoutKota.text.toString()
                        ),
                        this@ProfileActivity.buildImageMultipart("image", bitmap)
                    )
                }
            }
        }

        override fun onValidateFailed(errors: List<String>) {}
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (somePermissionPermanentlyDenied(this, listOf(perms[0]))) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == PERMISSION_CAMERA_AND_WRITE_EXTERNAL) {
            choosePhotoPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}