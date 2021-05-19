package com.zulham.mtv.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.zulham.mtv.data.local.LocalDataSource
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.data.local.room.entity.DetailEntity
import com.zulham.mtv.data.local.room.entity.GenresItemMovies
import com.zulham.mtv.data.local.room.entity.ProductionCompaniesItemMovies
import com.zulham.mtv.data.remote.RemoteDataSource
import com.zulham.mtv.utils.*
import com.zulham.mtv.vo.Resources
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


@Suppress("UNCHECKED_CAST")
@InternalCoroutinesApi
class ShowRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testExecutors = AppExecutors(TestExecutors, TestExecutors, TestExecutors)

    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)
    private val fakeShowRepository = FakeShowRepository(remote, local, appExecutors)

    private val dummyMovie = DummyData.generateDummyMovie()
    private val dummyTV = DummyData.generateDummyTV()

    private val detailItemTV = DetailEntity(
            "https://www.themoviedb.org/t/p/original/8edzqU74USlnfkCzHtLxILUfQW3.jpg",
            "With his two friends, a video-game-obsessed young man finds himself in a strange version of Tokyo where they must compete in dangerous games to win.",
            productionCompanies= listOf(ProductionCompaniesItemMovies("Netflix")),
            "December, 10 2020", genres=listOf(GenresItemMovies("Drama, Mystery, Action and Adventure")),
            1,
            "Alice in Borderland",
            "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/20mOwAAPwZ1vLQkw0fvuQHiG7bO.jpg")

    private val detailItemFilm = DetailEntity(
            "https://www.themoviedb.org/t/p/original/fN7f0sajt9uGFX4rPzQdJG3ivCr.jpg",
            "Wonder Woman comes into conflict with the Soviet Union during the Cold War in the 1980s and finds a formidable foe by the name of the Cheetah.",
            productionCompanies= listOf(ProductionCompaniesItemMovies("DC")),
            "December, 16 2020", genres=listOf(GenresItemMovies("Fantastic, Action, Adventure")),
            1,
            "Wonder Women 1984",
            "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg")

    @Test
    fun getMovieList(){
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, DataEntity>
        `when`(local.getMovieData()).thenReturn(dataSourceFactory)
        fakeShowRepository.getMovieList(1)

        val movieList = PagedListUtilsTest.mockPagedList(DummyData.generateDummyMovie())
        verify(local).getMovieData()
        Assert.assertNotNull(movieList)
        Assert.assertEquals(dummyMovie.size.toLong(), movieList.size.toLong())
    }

    @Test
    fun getTVList(){
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, DataEntity>
        `when`(local.getTVsData()).thenReturn(dataSourceFactory)
        fakeShowRepository.getTVShowList(1)

        val tvList = PagedListUtilsTest.mockPagedList(DummyData.generateDummyTV())
        verify(local).getTVsData()
        Assert.assertNotNull(tvList)
        Assert.assertEquals(dummyTV.size.toLong(), tvList.size.toLong())
    }

    @Test
    fun getMovieDetail(){
        val detailEntity = MutableLiveData<DetailEntity>()
        detailEntity.value = detailItemFilm

        `when`(local.getDetailMovie(1)).thenReturn(detailEntity)

        val movieDetail = LiveDataUtilsTest.getValue(fakeShowRepository.getMovieDetail(1))
        verify(local).getDetailMovie(1)
        Assert.assertNotNull(movieDetail)
        Assert.assertEquals(Resources.success(detailItemFilm), movieDetail)
    }

    @Test
    fun getTVDetail(){
        val detailEntity = MutableLiveData<DetailEntity>()
        detailEntity.value = detailItemTV

        `when`(local.getDetailTVShow(1)).thenReturn(detailEntity)

        val tvDetail = LiveDataUtilsTest.getValue(fakeShowRepository.getTVDetail(1))
        verify(local).getDetailTVShow(1)
        Assert.assertNotNull(tvDetail)
        Assert.assertEquals(Resources.success(detailItemTV), tvDetail)
    }

    @Test
    fun getFavMovie(){
        
        val dummyData = DummyData.generateDummyMovie()
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, DataEntity>

        `when`(local.getFavMovie()).thenReturn(dataSourceFactory)
        fakeShowRepository.getFavMovie()

        val result = PagedListUtilsTest.mockPagedList(dummyData)
        verify(local).getFavMovie()

        Assert.assertNotNull(result)
        Assert.assertEquals(dummyData.size, result.size)

    }
    
    @Test
    fun getFavTV(){
        
        val dummyData = DummyData.generateDummyTV()
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, DataEntity>

        `when`(local.getFavTV()).thenReturn(dataSourceFactory)
        fakeShowRepository.getFavTV()

        val result = PagedListUtilsTest.mockPagedList(dummyData)
        verify(local).getFavTV()

        Assert.assertNotNull(result)
        Assert.assertEquals(dummyData.size, result.size)

    }

    @Test
    fun addFav(){
        val id = 1

        `when`(appExecutors.diskIO()).thenReturn(testExecutors.diskIO())
        doNothing().`when`(local).setFavourite(id)

        fakeShowRepository.setFav(id)
        verify(local).setFavourite(id)

    }

    @Test
    fun delFav(){
        val id = 0

        `when`(appExecutors.diskIO()).thenReturn(testExecutors.diskIO())
        doNothing().`when`(local).deleteFav(id)

        fakeShowRepository.deleteFav(id)
        verify(local).deleteFav(id)

    }

    @Test
    fun checkFav(){
        val expectedValue = true
        val idMovie = 1
        val isFavorite = MutableLiveData<Boolean>()
        isFavorite.value = true
        `when`(local.checkFav(idMovie)).thenReturn(isFavorite)

        val result = LiveDataUtilsTest.getValue(fakeShowRepository.checkFav(idMovie))
        verify(local).checkFav(idMovie)

        Assert.assertEquals(expectedValue, result)
    }

}