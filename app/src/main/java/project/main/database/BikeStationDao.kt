package project.main.database

import androidx.room.*

@Dao
interface BikeStationDao {
    @get:Query("SELECT * FROM BikeStation ORDER BY id DESC")
    val allData: List<BikeStationEntity>

    @get:Query("SELECT count(*) FROM BikeStation")
    val allSize: Long

    @Query("SELECT * FROM BikeStation WHERE id = :id ORDER BY id DESC")
    fun searchByPkId(id: Long): BikeStationEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: BikeStationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<BikeStationEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: BikeStationEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(list: List<BikeStationEntity>)

    @Delete
    fun delete(entity: BikeStationEntity)

    @Delete
    fun delete(list: List<BikeStationEntity>)

    @Query("DELETE FROM BikeStation")
    fun deleteAll()

    @Query("DELETE FROM BikeStation WHERE id = :id")
    fun deleteByPkId(id: Long)
}