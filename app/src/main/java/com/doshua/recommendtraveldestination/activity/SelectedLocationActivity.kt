package com.doshua.recommendtraveldestination.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.doshua.recommendtraveldestination.SelectedLocation
import com.doshua.recommendtraveldestination.databinding.ActivitySelectedLocationBinding
import java.io.Serializable
import com.doshua.recommendtraveldestination.R
import com.doshua.recommendtraveldestination.view_model.SelectedLocationViewModel

class SelectedLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectedLocationBinding
    private val selectedLocationViewModel: SelectedLocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val item = intent.intentSerializable("selected location", SelectedLocation::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_selected_location)

        Glide.with(this).load(item?.image).into(binding.locationImageView)

        binding.apply {
            lifecycleOwner = this@SelectedLocationActivity
            viewModel = selectedLocationViewModel
        }

        selectedLocationViewModel.setSelectedData(item!!.name, item.address, item.phoneNumber)
    }

    private fun <T: Serializable> Intent.intentSerializable(key: String, clazz: Class<T>) : T? {

        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            this.getSerializableExtra(key) as T?
        }
    }
}