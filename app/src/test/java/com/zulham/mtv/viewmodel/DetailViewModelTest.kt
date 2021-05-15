package com.zulham.mtv.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.zulham.mtv.data.Genres
import com.zulham.mtv.data.PH
import com.zulham.mtv.data.ShowEntity
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.data.local.room.entity.DetailEntity
import com.zulham.mtv.data.local.room.entity.GenresItemMovies
import com.zulham.mtv.data.local.room.entity.ProductionCompaniesItemMovies
import com.zulham.mtv.data.repository.ShowRepository
import com.zulham.mtv.ui.detail.DetailActivity.Companion.MOVIE
import com.zulham.mtv.ui.detail.DetailActivity.Companion.TV_SHOW
import com.zulham.mtv.ui.detail.DetailViewModel
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
class DetailViewModelTest {

    private lateinit var detailViewModel: DetailViewModel

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
        detailViewModel = DetailViewModel(showRepository)
    }

    @Test
    fun getDetailMovie(){
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
        val getData = dummyMovie.showId?.let { detailViewModel.setSelectedShow(it) }
        dummyMovie.showId?.let { detailViewModel.getData(MOVIE, it) }
        verify(showRepository).getMovieDetail(1)
        Assert.assertNotNull(getData)
        Assert.assertEquals(dataDetail, dummyMovie)

        detailViewModel.getData(MOVIE, 1).observeForever(observer)
        verify(observer).onChanged(Resources.success(moviesDetail))
    }

    @Test
    fun getDetailTV(){
        val dataDetail = DummyData.generateDummyTV()[0]
        val production = dataDetail.production?.map { it ->
            ProductionCompaniesItemMovies(it.name)
        }
        val genre = dataDetail.genre?.map { it ->
            GenresItemMovies(it.name)
        }
        val tvDetail = DetailEntity(
                dataDetail.backdrop,
                dataDetail.description,
                production,
                dataDetail.releaseDate,
                genre,
                dataDetail.showId,
                dataDetail.title,
                dataDetail.img)
        val tv = MutableLiveData<Resources<DetailEntity>>()
        tv.value = Resources.success(tvDetail)

        `when`(showRepository.getTVDetail(1)).thenReturn(tv)
        val getData = dummyTV.showId?.let { detailViewModel.setSelectedShow(it) }
        dummyTV.showId?.let { detailViewModel.getData(TV_SHOW, it) }
        verify(showRepository).getTVDetail(1)
        Assert.assertNotNull(getData)
        Assert.assertEquals(dataDetail, dummyTV)

        detailViewModel.getData(TV_SHOW, 1).observeForever(observer)
        verify(observer).onChanged(Resources.success(tvDetail))
    }

    @Test
    fun setFav(){
        val dataDetail = DummyData.generateDummyMovie()[0]
        val moviesDetail = DataEntity(
            dataDetail.description,
            dataDetail.title,
            dataDetail.img,
            dataDetail.backdrop,
            dataDetail.releaseDate,
            dataDetail.showId,
            false,
            null
        )
        val boolean = 1
        doNothing().`when`(showRepository).setFav(boolean)
        detailViewModel.addFav(moviesDetail)

        verify(showRepository).setFav(boolean)
    }

    @Test
    fun deleteFav(){
        val dataDetail = DummyData.generateDummyMovie()[0]
        val tvDetail = DataEntity(
            dataDetail.description,
            dataDetail.title,
            dataDetail.img,
            dataDetail.backdrop,
            dataDetail.releaseDate,
            dataDetail.showId,
            false,
            null
        )
        val boolean = 1
        doNothing().`when`(showRepository).deleteFav(boolean)
        detailViewModel.deleteFav(tvDetail)

        verify(showRepository).deleteFav(boolean)
    }

}