package com.zulham.mtv.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowEntity(
        val showId: Int? = null,
        val title: String? = "",
        val releaseDate: String? = "",
        val genre: List<Genres>? = null,
        val production: List<PH>? = null,
        val description: String? = "",
        val img: String? = "",
        val backdrop: String? = ""
) : Parcelable

data class PH (
        val name: String? = ""
)

data class Genres (
        val name: String? = ""
)
