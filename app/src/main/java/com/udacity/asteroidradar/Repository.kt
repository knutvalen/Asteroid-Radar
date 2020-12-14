package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.KEYS.API
import com.udacity.asteroidradar.api.APIService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.Database
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domainmodels.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*

class Repository(private val database: Database) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.dataAccessObject.getAsteroids()
    ) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val startDate = SimpleDateFormat("YYYY-MM-dd").format(Calendar.getInstance().time)
            val response = APIService.feed.getFeed(API, startDate).await()
            val asteroids = parseAsteroidsJsonResult(JSONObject(response))

            val databaseAsteroids = asteroids.map {
                DatabaseAsteroid(
                    id = it.id,
                    name = it.name,
                    closeApproachDate = it.closeApproachDate,
                    absoluteMagnitude = it.absoluteMagnitude,
                    estimatedDiameter = it.estimatedDiameter,
                    relativeVelocity = it.relativeVelocity,
                    distanceFromEarth = it.distanceFromEarth,
                    isPotentiallyHazardous = it.isPotentiallyHazardous
                )
            }.toTypedArray()

            database.dataAccessObject.insertAll(*databaseAsteroids)
        }
    }

}