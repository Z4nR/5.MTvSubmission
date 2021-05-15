package com.zulham.mtv.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.data.repository.ShowRepository
import com.zulham.mtv.ui.movie.MovieViewModel
import com.zulham.mtv.vo.Resources
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    private lateinit var movieViewModel: MovieViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var  observer: Observer<Resources<PagedList<DataEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<DataEntity>

    @Before
    fun setUp(){
        movieViewModel = MovieViewModel(showRepository)
    }

    @Test
    fun getListMovie() {
        val dummyData = Resources.success(pagedList)

        val movies = MutableLiveData<Resources<PagedList<DataEntity>>>()
        movies.value = dummyData

        `when`(showRepository.getMovieList(1)).thenReturn(movies)
        movieViewModel.setDataMovie(1)
        val movieList = movieViewModel.getDataMovie()
        verify(showRepository).getMovieList(1)
        Assert.assertNotNull(movieList)
        Assert.assertEquals( movies , movieList)

        movieViewModel.getDataMovie().observeForever(observer)
        verify(observer).onChanged(dummyData)
    }

    @Test
    fun getListTV() {
        val dummyData = Resources.success(pagedList)

        val tvs = MutableLiveData<Resources<PagedList<DataEntity>>>()
        tvs.value = dummyData

        `when`(showRepository.getTVShowList(1)).thenReturn(tvs)
        movieViewModel.setDataTV(1)
        val movieList = movieViewModel.getDataTV()
        verify(showRepository).getTVShowList(1)
        Assert.assertNotNull(movieList)
        Assert.assertEquals( tvs , movieList)

        movieViewModel.getDataMovie().observeForever(observer)
        verify(observer).onChanged(dummyData)
    }

}