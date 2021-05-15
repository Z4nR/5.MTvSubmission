package com.zulham.mtv.data.remote.response

import com.google.gson.annotations.SerializedName

data class PageResponseMovies(

	@field:SerializedName("results")
	val results: List<ResultsMovies>? = null

)

data class ResultsMovies(

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
