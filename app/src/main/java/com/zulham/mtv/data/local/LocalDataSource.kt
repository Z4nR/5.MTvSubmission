package com.zulham.mtv.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.zulham.mtv.data.local.room.ShowDao
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.data.local.room.entity.DetailEntity

class LocalDataSource private constructor(private val showDao: ShowDao){

    companion object{
        private val INSTANCE: LocalDataSource? = null

        fun getInstance(showDao: ShowDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(showDao)
    }

    fun insertShowData(show: DataEntity){
        return showDao.insertShowData(show)
    }

    fun insertShowDetail(detail: DetailEntity){
        return showDao.insertShowDetail(detail)
    }

    fun getMovieData(): DataSource.Factory<Int, DataEntity> {
        return showDao.getMovieData()
    }

    fun getTVsData(): DataSource.Factory<Int, DataEntity> {
        return showDao.getTVsData()
    }

    fun getDetailMovie(id_movie : Int): LiveData<DetailEntity>{
        return showDao.getMovieDetail(id_movie)
    }

    fun getDetailTVShow(id_tv : Int): LiveData<DetailEntity>{
        return showDao.getTVsDetail(id_tv)
    }

    fun setFavourite(id: Int){
        return showDao.setFav(id)
    }

    fun getFavMovie(): DataSource.Factory<Int, DataEntity> {
        return showDao.getFavMovie()
    }

    fun getFavTV(): DataSource.Factory<Int, DataEntity> {
        return showDao.getFavTV()
    }

    fun deleteFav(id: Int){
        return showDao.deleteFav(id)
    }

    fun checkFav(id: Int): LiveData<Boolean>{
        return showDao.checkFav(id)
    }

}