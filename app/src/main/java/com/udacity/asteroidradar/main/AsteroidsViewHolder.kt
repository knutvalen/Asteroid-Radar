package com.udacity.asteroidradar.main

import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidsItemBinding
import com.udacity.asteroidradar.domainmodels.Asteroid

class AsteroidsViewHolder(
    var binding: AsteroidsItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: AsteroidsClickListener, asteroid: Asteroid) {
        binding.clickListener = clickListener
        binding.asteroid = asteroid

        val potentiallyHazardousIcon = when {
            asteroid.isPotentiallyHazardous -> ContextCompat.getDrawable(binding.root.context, R.drawable.ic_status_potentially_hazardous)
            else -> {
                val icon = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_status_normal)
                val typedValue = TypedValue()
                binding.root.context.theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true)
                icon?.setTint(typedValue.data)
                icon
            }
        }

        binding.imageViewHazardousIcon.setImageDrawable(potentiallyHazardousIcon)
        binding.executePendingBindings()
    }

}