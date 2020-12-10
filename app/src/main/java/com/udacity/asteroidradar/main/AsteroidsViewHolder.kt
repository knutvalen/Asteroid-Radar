package com.udacity.asteroidradar.main

import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidsItemBinding
import com.udacity.asteroidradar.domainmodels.Asteroid

class AsteroidsViewHolder(
    private var binding: AsteroidsItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(asteroid: Asteroid) {
        binding.asteroid = asteroid
        binding.executePendingBindings()
    }

}