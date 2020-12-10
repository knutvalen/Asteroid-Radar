package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.Database
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domainmodels.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: Database) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.dataAccessObject.getAsteroids()
    ) {
        it.asDomainModel()
    }


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids: Array<DatabaseAsteroid> = arrayOf(
                DatabaseAsteroid(
                    2138175,
                    "138175 (2000 EE104)",
                    "",
                    21.491,
                    0.4943561926,
                    6.9977283427,
                    0.0,
                    true
                )
            )

            database.dataAccessObject.insertAll(*asteroids)
        }
    }
}