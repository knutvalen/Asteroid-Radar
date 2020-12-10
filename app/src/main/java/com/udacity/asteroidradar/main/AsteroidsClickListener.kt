package com.udacity.asteroidradar.main

import com.udacity.asteroidradar.domainmodels.Asteroid

class AsteroidsClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}