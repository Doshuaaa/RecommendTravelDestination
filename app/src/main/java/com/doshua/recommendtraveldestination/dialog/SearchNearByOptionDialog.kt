package com.doshua.recommendtraveldestination.dialog

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import com.doshua.recommendtraveldestination.R
import com.doshua.recommendtraveldestination.databinding.DialogNearbySettingBinding
import com.doshua.recommendtraveldestination.view_model.SearchDialogViewModel
import com.doshua.recommendtraveldestination.view_model.TourListViewModel
import com.google.android.gms.maps.model.LatLng

class SearchNearByOptionDialog(private val context: Context, private val dlgViewModel: SearchDialogViewModel,
    private val tourListViewModel: TourListViewModel, private val currPosition: LatLng): DialogFragment() {

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

                    val b = seekBar.width
                    val ratio = 250 / seekBar.max

//                    val marginStart = ((rect.width() * 0.95f - seekBar.measuredWidth) / 2 ).toInt()
//                    val thumbPos: Int = marginStart + 250 * seekBar.progress / seekBar.max

                    dlgViewModel.apply {
                       // thumbPosition.value = thumbPos
                        progress.value = binding.seekBar.progress.toString()
                    }
                    binding.radiusLayout.updateLayoutParams<MarginLayoutParams> {
                        this.marginStart = ratio
                    }
                }



            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

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

            R.id.tourist -> dlgViewModel.searchType = 12
            R.id.culture -> dlgViewModel.searchType = 14
            R.id.festival -> dlgViewModel.searchType = 15
            R.id.travel_course -> dlgViewModel.searchType = 25
            R.id.leports -> dlgViewModel.searchType = 28
            R.id.lodge -> dlgViewModel.searchType = 32
            R.id.shopping -> dlgViewModel.searchType = 38
            R.id.restaurant -> dlgViewModel.searchType = 39
        }

        tourListViewModel.setTourListLiveData(currPosition.longitude, currPosition.latitude, dlgViewModel.searchType, dlgViewModel.progress.value!!.toInt() * 1000)
    }
}