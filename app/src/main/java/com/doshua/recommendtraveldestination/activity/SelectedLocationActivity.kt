package com.doshua.recommendtraveldestination.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.doshua.recommendtraveldestination.SelectedLocation
import com.doshua.recommendtraveldestination.databinding.ActivitySelectedLocationBinding
import java.io.Serializable
import com.doshua.recommendtraveldestination.R

class SelectedLocationActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectedLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val item = intent.intentSerializable("selected location", SelectedLocation::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_selected_location)
    }

    private fun <T: Serializable> Intent.intentSerializable(key: String, clazz: Class<T>) : T? {

        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            this.getSerializableExtra(key) as T?
        }
    }
}