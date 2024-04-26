package com.doshua.recommendtraveldestination.dialog


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doshua.recommendtraveldestination.R
import com.doshua.recommendtraveldestination.databinding.DialogBottomMapOptionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SelectedMapAppBottomSheetDialog(
    private val selectedName: String,
    private val selectedMapX: String,
    private val selectedMapY: String,
    private val currMapX: String,
    private val currMapY: String
) : BottomSheetDialogFragment() {

    lateinit var binding: DialogBottomMapOptionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_map_option, container, false)
        binding.dialog = this
        return binding.root
    }

    fun findPathToDestination(map: String) {

        var marketUrlString = ""
        var intent = Intent()
        when(map) {
            "google" -> {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${selectedMapY},${selectedMapX}&travelmode=driving")
                )
            }
            "naver" -> {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("nmap://route/public?dlat=${selectedMapY}&dlng=${selectedMapX}&dname=${selectedName}&appname=com.doshua.recommendtraveldestination")
                )
                marketUrlString = "market://details?id=com.nhn.android.nmap"
            }
            "kakao" -> {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("kakaomap://route?sp=${currMapX},${currMapY}&ep=${selectedMapX},${selectedMapY}&by=FOOT")
                )
                marketUrlString = "market://details?id=net.daum.android.map"
            }
            "tmap" -> {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("tmap://route?startx=${currMapX}&starty=${currMapY}&goalx=${selectedMapX}&goaly=${selectedMapY}&reqCoordType=WGS84&resCoordType=WGS84")
                )
                marketUrlString = "market://details?id=com.skt.tmap.ku"
            }
            else -> Intent()
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(marketUrlString)))
        }
//        val list: MutableList<ResolveInfo>? =
//            context?.packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

    }
}