package com.doshua.recommendtraveldestination

import java.io.Serializable

data class SelectedLocation(
    val name: String,
    val address: String,
    val mapX: String,
    val mapY: String,
    val phoneNumber: String,
    val image: String
): Serializable