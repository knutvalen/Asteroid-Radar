package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidsItemBinding
import com.udacity.asteroidradar.domainmodels.Asteroid

class AsteroidsAdapter(
    private val clickListener: AsteroidsClickListener
) : RecyclerView.Adapter<AsteroidsViewHolder>() {

    var asteroids: List<Asteroid> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        val dataBinding: AsteroidsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.asteroids_item, parent, false
        )

        return AsteroidsViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        holder.bind(clickListener, asteroids[position])
    }

    override fun getItemCount() = asteroids.size

}