package de.goemic.animalsound.ui.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import coil.imageLoader
import coil.request.ImageRequest
import de.goemic.animalsound.R
import de.goemic.animalsound.ui.main.MainViewModel
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment(val item: MainViewModel.AnimalItem) : Fragment() {

    companion object {
        fun newInstance(item: MainViewModel.AnimalItem): DetailFragment {
            return DetailFragment(item)
        }
    }

    //
    // lifecycle methods
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prepareTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setTransitionName(imageView, item.id)

        // delay enter transition until image is loaded
        postponeEnterTransition()

        val imageRequest = ImageRequest.Builder(requireContext())
            .data(item.imageDrawable)
            .target(imageView)
            .listener(
                onSuccess = { _, _ ->
                    // Start the transition once all views have been measured and laid out
                    (view.parent as? ViewGroup)?.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                }
            )
            .build()

        requireContext().imageLoader.enqueue(imageRequest)
    }

    //
    // private methods
    //

    private fun prepareTransition() {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.image_transition)

        sharedElementEnterTransition = transition
    }
}