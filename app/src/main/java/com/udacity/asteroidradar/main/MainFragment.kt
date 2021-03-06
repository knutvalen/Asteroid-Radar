package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.APIError
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        ViewModelProvider(this, MainViewModel.Factory(activity.application))
            .get(MainViewModel::class.java)
    }

    private var viewModelAdapter: AsteroidsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModelAdapter = AsteroidsAdapter(AsteroidsClickListener { asteroid ->
            Timber.i(asteroid.name)
            viewModel.displayDetails(asteroid)
        })

        binding.root.findViewById<RecyclerView>(R.id.asteroid_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.asteroids.observe(viewLifecycleOwner, { asteroids ->
            asteroids?.apply {
                viewModelAdapter?.asteroids = asteroids
            }
        })

        viewModel.selectedAsteroid.observe(viewLifecycleOwner, { asteroid ->
            if (asteroid != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.displayDetailsComplete()
            }
        })

        viewModel.apiError.observe(viewLifecycleOwner, { apiError ->
            if (apiError != null) {
                val message = when (apiError) {
                    APIError.NearEarthObjectWebService -> context?.getString(R.string.error_asteroid_api)
                    APIError.AstronomyPictureOfTheDayService -> context?.getString(R.string.error_picture_api)
                    else -> null
                }

                if (message != null) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }

                viewModel.displayErrorMessageComplete()
            }
        })
    }

}
