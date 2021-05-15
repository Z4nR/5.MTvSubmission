package com.zulham.mtv.data.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.data.local.room.entity.DetailEntity
import com.zulham.mtv.vo.Resources

interface ShowDataSource {

    fun getMovieList(page_movie: Int): LiveData<Resources<PagedList<DataEntity>>>

    fun getTVShowList(page_tv: Int): LiveData<Resources<PagedList<DataEntity>>>

    fun getMovieDetail(id_movie: Int): LiveData<Resources<DetailEntity>>

    fun getTVDetail(id_tv: Int): LiveData<Resources<DetailEntity>>

    fun getFavMovie(): LiveData<PagedList<DataEntity>>

    fun getFavTV(): LiveData<PagedList<DataEntity>>

    fun checkFav(id: Int): LiveData<Boolean>

    fun setFav(id: Int)

    fun deleteFav(id: Int)

}