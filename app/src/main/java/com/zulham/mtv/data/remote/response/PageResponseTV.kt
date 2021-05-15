package com.zulham.mtv.data.remote.response

import com.google.gson.annotations.SerializedName

data class PageResponseTV(

	@field:SerializedName("results")
	val results: List<ResultsTV>? = null

)

data class ResultsTV(

	@field:SerializedName("first_air_date")
	val firstAirDate: String? = null,

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
