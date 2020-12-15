package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.KEYS.API
import com.udacity.asteroidradar.api.APIService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.Database
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domainmodels.Asteroid
import com.udacity.asteroidradar.domainmodels.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*

class Repository(private val database: Database) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.dataAccessObject.getAsteroids(getTodayString())
    ) {
        it.asDomainModel()
    }

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private fun getTodayString(): String {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(Calendar.getInstance().time)
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val response = APIService.nearEarthObject.getFeed(API, getTodayString()).await()
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

    suspend fun refreshPictureOfTheDay() {
        val pictureOfTheDay = APIService
            .astronomyPictureOfTheDay
            .getAstronomyPictureOfTheDay(API, getTodayString())
            .await()

        if (pictureOfTheDay.mediaType == "image") {
            _pictureOfTheDay.value = pictureOfTheDay
        }
    }

    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            database.dataAccessObject.deleteOldAsteroids(getTodayString())
        }
    }

}