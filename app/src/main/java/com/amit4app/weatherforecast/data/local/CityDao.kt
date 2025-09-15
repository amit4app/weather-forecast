package com.amit4app.weatherforecast.data.local

import androidx.room.*

@Dao
interface CityDao {
    @Query("""
        SELECT * FROM cities
        WHERE name LIKE :q || '%' COLLATE NOCASE
        ORDER BY lastUsedAt DESC, name ASC
        LIMIT 20
    """)
    suspend fun suggestByPrefix(q: String): List<CityEntity>

    @Query("SELECT * FROM cities ORDER BY lastUsedAt DESC, name ASC LIMIT 10")
    suspend fun recent(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<CityEntity>)

    @Update
    suspend fun update(item: CityEntity)

    @Query("UPDATE cities SET lastUsedAt = :ts WHERE id = :id")
    suspend fun touch(id: Long, ts: Long = System.currentTimeMillis())
}
