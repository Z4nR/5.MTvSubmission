package com.zulham.mtv.ui.favorite.ui.main.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zulham.mtv.R
import com.zulham.mtv.data.local.room.entity.DetailEntity
import com.zulham.mtv.utils.Factory
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail_favorite.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class DetailFavoriteActivity : AppCompatActivity() {

    private lateinit var detailFavViewModel: DetailFavViewModel

    companion object{

        const val EXTRA_SHOW = "extra_show"
        const val EXTRA_TYPE = "extra_type"
        const val MOVIE = "movie"
        const val TV_SHOW = "tv_show"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_favorite)

        showLoading(true)

        backHome()

        val detailFavFactory = Factory.getInstance(applicationContext)
        detailFavViewModel = ViewModelProvider(this, detailFavFactory)[DetailFavViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            val showId = extras.getInt(EXTRA_SHOW)
            val showType = extras.getString(EXTRA_TYPE)
            detailFavViewModel.getData(showType, showId).observe(this, {
                showDetailFav(it.data)
                showLoading(false)
            })
        }

    }

    private fun showDetailFav(show: DetailEntity?){
        val w = 1000
        val h = 1000
        val imgUrl = "https://image.tmdb.org/t/p/w300/"

        Glide.with(this@DetailFavoriteActivity)
            .load(imgUrl + show?.backdropPath)
            .error(R.drawable.ic_launcher_foreground)
            .apply(RequestOptions().override(w, h))
            .into(IV_posterDetailFav)

        titleDetailFav.text = show?.title
        genreDetailFav.text = show?.genres?.map { genre -> genre.name }?.joinToString()
        showIdFav.text = show?.id.toString()
        showProductionFav.text = show?.productionCompanies?.map { production -> production.name }?.joinToString()
        tv_justified_paragraphFav.text = show?.overview
    }

    private fun backHome() {
        supportActionBar?.title = "Detail Collection"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            detailProgressBarFav.visibility = View.VISIBLE
        } else {
            detailProgressBarFav.visibility = View.GONE
        }
    }
}