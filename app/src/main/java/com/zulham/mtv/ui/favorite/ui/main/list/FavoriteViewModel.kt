package com.zulham.mtv.ui.favorite.ui.main.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.data.repository.ShowRepository
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteViewModel(private val showRepository: ShowRepository) : ViewModel() {

    fun favMovie(): LiveData<PagedList<DataEntity>> {
        return showRepository.getFavMovie()
    }

    fun favTVShow(): LiveData<PagedList<DataEntity>> {
        return showRepository.getFavTV()
    }

    fun swipeDeleteFav(dataEntity: DataEntity){
        dataEntity.id?.let { showRepository.deleteFav(it) }
    }

}