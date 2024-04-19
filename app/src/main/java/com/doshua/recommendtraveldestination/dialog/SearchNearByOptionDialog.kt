package com.doshua.recommendtraveldestination.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.doshua.recommendtraveldestination.R
import com.doshua.recommendtraveldestination.databinding.DialogNearbySettingBinding
import com.doshua.recommendtraveldestination.view_model.SearchDialogViewModel
import com.doshua.recommendtraveldestination.view_model.TourListViewModel
import com.google.android.gms.maps.model.LatLng

class SearchNearByOptionDialog(
    private val context: Context,
    private val dlgViewModel: SearchDialogViewModel,
): DialogFragment() {

    private lateinit var binding: DialogNearbySettingBinding
    private lateinit var rect: Rect

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNearbySettingBinding.inflate(layoutInflater)

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {


                with(binding) {
                    viewModel = dlgViewModel
                    lifecycleOwner = context as AppCompatActivity
                    dlg = this@SearchNearByOptionDialog
                    dlgViewModel.progress.value = binding.seekBar.progress.toString()

//                    val b = seekBar.width
//                    val ratio = 250 / seekBar.max
//
////                    val marginStart = ((rect.width() * 0.95f - seekBar.measuredWidth) / 2 ).toInt()
////                    val thumbPos: Int = marginStart + 250 * seekBar.progress / seekBar.max
//
//                    dlgViewModel.apply {
//                       // thumbPosition.value = thumbPos
//                        progress.value = binding.seekBar.progress.toString()
//                    }
//                    binding.radiusLayout.updateLayoutParams<MarginLayoutParams> {
//                        this.marginStart = ratio
//                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })


//        binding.tourist.setOnClickListener(View.OnClickListener {
//            when(it.id) {
//                R.id.tourist ->  {
//                    if(binding.tourist.isChecked) {
//                        binding.tourist.isChecked = false
//                        binding.tourist.setTextColor(Color.parseColor("#000000"))
//                    } else {
//                        binding.tourist.isChecked = true
//                        binding.tourist.setTextColor(Color.parseColor("#FF9800"))
//                    }
//                }
//                R.id.culture -> dlgViewModel.searchType.value = 14
//                R.id.festival -> dlgViewModel.searchType.value = 15
//                R.id.travel_course -> dlgViewModel.searchType.value = 25
//                R.id.leports -> dlgViewModel.searchType.value = 28
//                R.id.lodge -> dlgViewModel.searchType.value = 32
//                R.id.shopping -> dlgViewModel.searchType.value = 38
//                R.id.restaurant -> dlgViewModel.searchType.value = 39
//            }
//        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        rect = windowManager.currentWindowMetrics.bounds

        val x = (rect.width() * 0.95f).toInt()
        val y = (rect.height() * 0.5f).toInt()
        dialog?.window?.setLayout(x, y)
    }

    fun search() {

        when(binding.searchTypeRadioGroup.checkedRadioButtonId) {

            R.id.tourist -> dlgViewModel.searchType.value = 12
            R.id.culture -> dlgViewModel.searchType.value = 14
            R.id.festival -> dlgViewModel.searchType.value = 15
            R.id.travel_course -> dlgViewModel.searchType.value = 25
            R.id.leports -> dlgViewModel.searchType.value = 28
            R.id.lodge -> dlgViewModel.searchType.value = 32
            R.id.shopping -> dlgViewModel.searchType.value = 38
            R.id.restaurant -> dlgViewModel.searchType.value = 39
        }
        dismiss()
    }


    fun radioButtonListener(view: View) {
        when(view.id) {
            R.id.tourist ->  {
                if(binding.tourist.isChecked) {
                    binding.tourist.isChecked = false
                    binding.tourist.setTextColor(Color.parseColor("#ffffff"))
                } else {
                    binding.tourist.setTextColor(Color.parseColor("#FF9800"))
                }
            }
            R.id.culture -> dlgViewModel.searchType.value = 14
            R.id.festival -> dlgViewModel.searchType.value = 15
            R.id.travel_course -> dlgViewModel.searchType.value = 25
            R.id.leports -> dlgViewModel.searchType.value = 28
            R.id.lodge -> dlgViewModel.searchType.value = 32
            R.id.shopping -> dlgViewModel.searchType.value = 38
            R.id.restaurant -> dlgViewModel.searchType.value = 39
        }
    }
}