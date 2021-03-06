package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataAccessObject {

    @Query("SELECT * FROM DatabaseAsteroid WHERE closeApproachDate > :dateString ORDER BY closeApproachDate ASC")
    fun getAsteroids(dateString: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM DatabaseAsteroid WHERE closeApproachDate < :dateString")
    fun deleteOldAsteroids(dateString: String)

}

@androidx.room.Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract val dataAccessObject: DataAccessObject
}

private lateinit var INSTANCE: Database

fun getDatabase(context: Context): Database {
    synchronized(Database::class.java) {
        if (!::INSTANCE.isInitialized) {
            val builder = Room.databaseBuilder(
                context.applicationContext, Database::class.java, "asteroids"
            )

            INSTANCE = builder.build()
        }
    }

    return INSTANCE
}