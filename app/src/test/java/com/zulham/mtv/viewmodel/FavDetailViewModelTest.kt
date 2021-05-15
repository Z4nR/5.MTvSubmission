package com.zulham.mtv.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.zulham.mtv.data.Genres
import com.zulham.mtv.data.PH
import com.zulham.mtv.data.ShowEntity
import com.zulham.mtv.data.local.room.entity.DetailEntity
import com.zulham.mtv.data.local.room.entity.GenresItemMovies
import com.zulham.mtv.data.local.room.entity.ProductionCompaniesItemMovies
import com.zulham.mtv.data.repository.ShowRepository
import com.zulham.mtv.ui.favorite.ui.main.detail.DetailFavViewModel
import com.zulham.mtv.ui.favorite.ui.main.detail.DetailFavoriteActivity
import com.zulham.mtv.utils.DummyData
import com.zulham.mtv.vo.Resources
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavDetailViewModelTest {

    private lateinit var detailFavViewModel: DetailFavViewModel

    private val dummyMovie = ShowEntity(1,
        "Wonder Women 1984",
        "December, 16 2020",
        genre = listOf(Genres("Fantastic, Action, Adventure")),
        production = listOf(PH("DC")),
        "Wonder Woman comes into conflict with the Soviet Union during the Cold War in the 1980s and finds a formidable foe by the name of the Cheetah.",
        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
        "https://www.themoviedb.org/t/p/original/fN7f0sajt9uGFX4rPzQdJG3ivCr.jpg")

    private val dummyTV = ShowEntity(1,
        "Alice in Borderland",
        "December, 10 2020",
        genre = listOf(Genres("Drama, Mystery, Action and Adventure")),
        production = listOf(PH("Netflix")),
        "With his two friends, a video-game-obsessed young man finds himself in a strange version of Tokyo where they must compete in dangerous games to win.",
        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/20mOwAAPwZ1vLQkw0fvuQHiG7bO.jpg",
        "https://www.themoviedb.org/t/p/original/8edzqU74USlnfkCzHtLxILUfQW3.jpg")


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var  observer: Observer<Resources<DetailEntity>>

    @Before
    fun setUp(){
        detailFavViewModel = DetailFavViewModel(showRepository)
    }

    @Test
    fun detailFavMovie(){
        val dataDetail = DummyData.generateDummyMovie()[0]
        val production = dataDetail.production?.map { it ->
            ProductionCompaniesItemMovies(it.name)
        }
        val genre = dataDetail.genre?.map { it ->
            GenresItemMovies(it.name)
        }
        val moviesDetail = DetailEntity(
            dataDetail.backdrop,
            dataDetail.description,
            production,
            dataDetail.releaseDate,
            genre,
            dataDetail.showId,
            dataDetail.title,
            dataDetail.img)
        val movies = MutableLiveData<Resources<DetailEntity>>()
        movies.value = Resources.success(moviesDetail)

        `when`(showRepository.getMovieDetail(1)).thenReturn(movies)
        val getData = dummyMovie.showId?.let { detailFavViewModel.setSelectedShow(it) }
        dummyMovie.showId?.let { detailFavViewModel.getData(DetailFavoriteActivity.MOVIE, it) }
        verify(showRepository).getMovieDetail(1)
        Assert.assertNotNull(getData)
        Assert.assertEquals(dataDetail, dummyMovie)

        detailFavViewModel.getData(DetailFavoriteActivity.MOVIE, 1).observeForever(observer)
        verify(observer).onChanged(Resources.success(moviesDetail))
    }

    @Test
    fun detailFavTV(){
        val dataDetail = DummyData.generateDummyTV()[0]
        val production = dataDetail.production?.map { it ->
            ProductionCompaniesItemMovies(it.name)
        }
        val genre = dataDetail.genre?.map { it ->
            GenresItemMovies(it.name)
        }
        val tvsDetail = DetailEntity(
            dataDetail.backdrop,
            dataDetail.description,
            production,
            dataDetail.releaseDate,
            genre,
            dataDetail.showId,
            dataDetail.title,
            dataDetail.img)
        val tvs = MutableLiveData<Resources<DetailEntity>>()
        tvs.value = Resources.success(tvsDetail)

        `when`(showRepository.getTVDetail(1)).thenReturn(tvs)
        val getData = dummyTV.showId?.let { detailFavViewModel.setSelectedShow(it) }
        dummyTV.showId?.let { detailFavViewModel.getData(DetailFavoriteActivity.TV_SHOW, it) }
        verify(showRepository).getTVDetail(1)
        Assert.assertNotNull(getData)
        Assert.assertEquals(dataDetail, dummyTV)

        detailFavViewModel.getData(DetailFavoriteActivity.TV_SHOW, 1).observeForever(observer)
        verify(observer).onChanged(Resources.success(tvsDetail))
    }

}