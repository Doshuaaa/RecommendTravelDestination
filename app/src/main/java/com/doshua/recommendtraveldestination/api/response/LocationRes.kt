package com.doshua.recommendtraveldestination.api.response

data class LocationRes (
    val response: Response
) {
    data class Response(

        val header: Header,
        val body: Body
    )
    data class Header(
        val resultCode: String,
        val resultMsg: String,
    )

    data class Body(
        val items: Items,
        val numOfRows: Number,
        val pageNo: Number,
        val totalCount: Number
    )
    data class Items(
        val item: List<Item>
    )

    data class Item(
        val addr1: String,
        val addr2: String,
        val contentid: String,
        val contenttypeid: String,
        val createdtime: String,
        val dist: String,
        val firstimage: String,
        val firstimage2: String,
        val mapx: String,
        val mapy: String,
        val modifiedtime: String,
        val tel: String,
        val title: String,
    )


}