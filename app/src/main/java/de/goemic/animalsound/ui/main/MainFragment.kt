package de.goemic.animalsound.ui.main

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.goemic.animalsound.R
import de.goemic.animalsound.databinding.MainFragmentBinding
import de.goemic.animalsound.ui.detail.DetailFragment


class MainFragment : Fragment(), MainViewModel.Interactor {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

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

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        val layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // delay the reenter transition until view is ready
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.attach(requireContext(), this)
    }

    override fun onDestroy() {
        viewModel.detach()
        super.onDestroy()
    }

    //
    // private methods
    //

    private fun prepareTransition() {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.image_transition)

        sharedElementReturnTransition = transition
    }

    //
    // MainViewModel.Interactor
    //

    override fun showDetails(view: View, item: MainViewModel.AnimalItem) {
        val tag = DetailFragment::javaClass.name

        parentFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(view as ImageView, item.id)
            .addToBackStack(tag)
            .replace(R.id.container, DetailFragment.newInstance(item), tag)
            .commit()
    }

    override fun hideDetails() {
        val tag = DetailFragment::javaClass.name

        if (parentFragmentManager.findFragmentByTag(tag) is DetailFragment) {
            requireActivity().onBackPressed()
        }

    }
}