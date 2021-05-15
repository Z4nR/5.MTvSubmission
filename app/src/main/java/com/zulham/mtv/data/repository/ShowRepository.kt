package com.zulham.mtv.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.zulham.mtv.data.NetworkResource
import com.zulham.mtv.data.local.LocalDataSource
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.data.local.room.entity.DetailEntity
import com.zulham.mtv.data.local.room.entity.GenresItemMovies
import com.zulham.mtv.data.local.room.entity.ProductionCompaniesItemMovies
import com.zulham.mtv.data.remote.RemoteDataSource
import com.zulham.mtv.data.remote.ShowDataSource
import com.zulham.mtv.data.remote.response.ResultsMovies
import com.zulham.mtv.data.remote.response.ShowResponseMovies
import com.zulham.mtv.data.remote.statusresponse.ApiResponse
import com.zulham.mtv.utils.AppExecutors
import com.zulham.mtv.utils.ShowType.MOVIE_TYPE
import com.zulham.mtv.utils.ShowType.TV_TYPE
import com.zulham.mtv.vo.Resources
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ShowRepository private constructor(private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource, private val appExecutors: AppExecutors): ShowDataSource{

    companion object{
        @Volatile
        private var instance: ShowRepository? = null
        fun getInstance(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource, appExecutors: AppExecutors): ShowRepository =
            instance ?: synchronized(this){
                instance ?: ShowRepository(remoteDataSource, localDataSource, appExecutors)
            }
    }

    override fun getMovieList(page_movie: Int): LiveData<Resources<PagedList<DataEntity>>> {
        return object : NetworkResource<PagedList<DataEntity>, List<ResultsMovies>>(appExecutors){

            override fun loadFromDB(): LiveData<PagedList<DataEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getMovieData(), config).build()
            }

            override fun shouldFetch(data: PagedList<DataEntity>?): Boolean =
                data == null || data.isNullOrEmpty()

            override fun createCall(): LiveData<ApiResponse<List<ResultsMovies>>> {
                val mListMovie = MutableLiveData<ApiResponse<List<ResultsMovies>>>()
                remoteDataSource.getMovieList(
                    page_movie,
                    object : RemoteDataSource.LoadListCallback {
                        override fun onAllListReceive(resultsItem: ApiResponse<List<ResultsMovies>>) {
                            mListMovie.postValue(resultsItem)
                        }
                    })

                return mListMovie
            }

            override fun saveCallResult(data: List<ResultsMovies>) {
                data.forEach { response ->
                    val list = DataEntity(
                        response.overview,
                        response.title,
                        response.posterPath,
                        response.backdropPath,
                        response.releaseDate,
                        response.id,
                        false,
                        MOVIE_TYPE
                    )
                    localDataSource.insertShowData(list)
                }

            }

        }.asLiveData()

    }

    override fun getTVShowList(page_tv: Int): LiveData<Resources<PagedList<DataEntity>>> {
        return object : NetworkResource<PagedList<DataEntity>, List<ResultsMovies>>(appExecutors){
            override fun loadFromDB(): LiveData<PagedList<DataEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getTVsData(), config).build()
            }

            override fun shouldFetch(data: PagedList<DataEntity>?): Boolean =
                data == null || data.isNullOrEmpty()

            override fun createCall(): LiveData<ApiResponse<List<ResultsMovies>>> {
                val mListTV = MutableLiveData<ApiResponse<List<ResultsMovies>>>()
                remoteDataSource.getTVShowList(
                    page_tv,
                    object : RemoteDataSource.LoadListCallback {
                        override fun onAllListReceive(resultsItem: ApiResponse<List<ResultsMovies>>) {
                            mListTV.postValue(resultsItem)
                        }

                    })

                return mListTV
            }

            override fun saveCallResult(data: List<ResultsMovies>) {
                data.forEach { response ->
                    val list = DataEntity(
                        response.overview,
                        response.title,
                        response.posterPath,
                        response.backdropPath,
                        response.releaseDate,
                        response.id,
                        false,
                        TV_TYPE
                    )
                    localDataSource.insertShowData(list)
                }
            }

        }.asLiveData()

    }

    override fun getMovieDetail(id_movie: Int): LiveData<Resources<DetailEntity>> {
        return object : NetworkResource<DetailEntity, ShowResponseMovies>(appExecutors){
            override fun loadFromDB(): LiveData<DetailEntity> = localDataSource.getDetailMovie(id_movie)

            override fun shouldFetch(data: DetailEntity?): Boolean =
                data == null

            override fun createCall(): LiveData<ApiResponse<ShowResponseMovies>> {
                val movieDetail = MutableLiveData<ApiResponse<ShowResponseMovies>>()
                remoteDataSource.getMovieDetail(
                    id_movie,
                    object : RemoteDataSource.LoadDetailCallback {
                        override fun onDetailReceive(detailItem: ApiResponse<ShowResponseMovies>) {
                            movieDetail.postValue(detailItem)
                        }

                    }
                )
                return movieDetail
            }

            override fun saveCallResult(data: ShowResponseMovies) {
                val genres = data.genres?.map { it -> GenresItemMovies(it.name) }
                val production = data.productionCompanies?.map { it -> ProductionCompaniesItemMovies(it.name) }
                val detail = DetailEntity(
                    data.backdropPath,
                    data.overview,
                    production,
                    data.releaseDate,
                    genres,
                    data.id,
                    data.title,
                    data.posterPath,
                    false,
                    MOVIE_TYPE
                )
                localDataSource.insertShowDetail(detail)
            }

        }.asLiveData()

    }

    override fun getTVDetail(id_tv: Int): LiveData<Resources<DetailEntity>> {
        return object : NetworkResource<DetailEntity, ShowResponseMovies>(appExecutors){
            override fun loadFromDB(): LiveData<DetailEntity> = localDataSource.getDetailTVShow(id_tv)

            override fun shouldFetch(data: DetailEntity?): Boolean =
                data == null

            override fun createCall(): LiveData<ApiResponse<ShowResponseMovies>> {
                val tvDetail = MutableLiveData<ApiResponse<ShowResponseMovies>>()
                remoteDataSource.getTVDetail(
                    id_tv,
                    object : RemoteDataSource.LoadDetailCallback {
                        override fun onDetailReceive(detailItem: ApiResponse<ShowResponseMovies>) {
                            tvDetail.postValue(detailItem)
                        }

                    }
                )
                return tvDetail
            }

            override fun saveCallResult(data: ShowResponseMovies) {
                val genres = data.genres?.map { it -> GenresItemMovies(it.name) }
                val production = data.productionCompanies?.map { it -> ProductionCompaniesItemMovies(it.name) }
                val detail = DetailEntity(
                    data.backdropPath,
                    data.overview,
                    production,
                    data.releaseDate,
                    genres,
                    data.id,
                    data.title,
                    data.posterPath,
                    false,
                    TV_TYPE
                )
                localDataSource.insertShowDetail(detail)
            }

        }.asLiveData()
    }

    override fun getFavMovie(): LiveData<PagedList<DataEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localDataSource.getFavMovie(), config).build()
    }

    override fun getFavTV(): LiveData<PagedList<DataEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localDataSource.getFavTV(), config).build()
    }

    override fun checkFav(id: Int): LiveData<Boolean> {
        return localDataSource.checkFav(id)
    }

    override fun setFav(id: Int) {
        appExecutors.diskIO().execute{
            localDataSource.setFavourite(id)
        }
    }

    override fun deleteFav(id: Int) {
        appExecutors.diskIO().execute {
            localDataSource.deleteFav(id)
        }
    }

}