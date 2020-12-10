package com.udacity.asteroidradar.main

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidsItemBinding

class AsteroidsViewHolder(
    val dataBinding: AsteroidsItemBinding
) : RecyclerView.ViewHolder(dataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.asteroids_item
    }
}