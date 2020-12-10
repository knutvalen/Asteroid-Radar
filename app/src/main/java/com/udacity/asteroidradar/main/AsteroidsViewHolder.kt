package com.udacity.asteroidradar.main

import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidsItemBinding
import com.udacity.asteroidradar.domainmodels.Asteroid

class AsteroidsViewHolder(
    var binding: AsteroidsItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: AsteroidsClickListener, asteroid: Asteroid) {
        binding.clickListener = clickListener
        binding.asteroid = asteroid
        binding.executePendingBindings()
    }

}