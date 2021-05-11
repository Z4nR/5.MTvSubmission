package com.zulham.mtv.data.local.room.entity.listconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.zulham.mtv.data.local.room.entity.ProductionCompaniesItemMovies

@ProvidedTypeConverter
class ProductionsConverter {

    @TypeConverter
    fun productionListFromString(value: String?): List<ProductionCompaniesItemMovies>? {
        return value?.let { it ->
            val production = it.split(',')
            return production.map { ProductionCompaniesItemMovies(it) }
        }
    }

    @TypeConverter
    fun stringFromProduction(production: List<ProductionCompaniesItemMovies>?): String? {
        val productions = production?.map { it.name }
        return productions?.joinToString()
    }

}