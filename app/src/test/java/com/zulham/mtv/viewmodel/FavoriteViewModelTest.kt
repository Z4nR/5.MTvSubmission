package com.zulham.mtv.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.data.repository.ShowRepository
import com.zulham.mtv.ui.favorite.ui.main.list.FavoriteViewModel
import com.zulham.mtv.utils.DummyData
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
class FavoriteViewModelTest {

    private lateinit var favoriteViewModel: FavoriteViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var  observer: Observer<PagedList<DataEntity>>

    @Mock
    private lateinit var pagedList: PagedList<DataEntity>

    @Before
    fun setUp(){
        favoriteViewModel = FavoriteViewModel(showRepository)
    }

    @Test
    fun getListFavMovie() {
        val dummyData = pagedList

        val movies = MutableLiveData<PagedList<DataEntity>>()
        movies.value = dummyData

        `when`(showRepository.getFavMovie()).thenReturn(movies)

        val movieList = favoriteViewModel.favMovie()
        verify(showRepository).getFavMovie()
        Assert.assertNotNull(movieList)
        Assert.assertEquals( movies , movieList)

        favoriteViewModel.favMovie().observeForever(observer)
        verify(observer).onChanged(dummyData)
    }

    @Test
    fun getListFavTV() {
        val dummyData = pagedList

        val tvs = MutableLiveData<PagedList<DataEntity>>()
        tvs.value = dummyData

        `when`(showRepository.getFavTV()).thenReturn(tvs)

        val tvList = favoriteViewModel.favTVShow()
        verify(showRepository).getFavTV()
        Assert.assertNotNull(tvList)
        Assert.assertEquals( tvs , tvList)

        favoriteViewModel.favTVShow().observeForever(observer)
        verify(observer).onChanged(dummyData)
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
        favoriteViewModel.swipeDeleteFav(tvDetail)

        verify(showRepository).deleteFav(boolean)
    }

}