package com.example.backbase.data.search.dataSource

import com.example.backbase.data.model.Coord
import com.google.gson.annotations.SerializedName

class CityDiskModel (
    @field:SerializedName("_id")
    var id: Int,
    var name: String,
    var country: String,
    @field:SerializedName("coord") var coordinate: Coord
    )