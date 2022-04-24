package de.goemic.animalsound.ui.main

import android.content.Context
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import coil.load
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import de.goemic.animalsound.BR
import de.goemic.animalsound.R
import kotlinx.android.parcel.Parcelize
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import java.io.File

class MainViewModel : ViewModel(), Player.Listener {

    //
    // inner class
    //

    @Parcelize
    data class AnimalItem(
        @RawRes val audioRes: Int,
        @DrawableRes val imageDrawable: Int,
        val audioLicenseUrl: String,
        val imageLicenseUrl: String? = null,
        val id: String = "$audioRes - $imageDrawable"
    ) : Parcelable

    interface Interactor {
        fun showDetails(view: View, item: AnimalItem)
        fun hideDetails()
    }

    //
    // fields
    //

    val itemList = ObservableArrayList<AnimalItem>()
    val onItemBindClass: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(AnimalItem::class.java) { itemBinding: ItemBinding<*>, _: Int, _: AnimalItem? ->
            itemBinding.clearExtras()
                .set(BR.item, R.layout.item_animal)
                .bindExtra(BR.viewModel, this)
        }

    private var exoPlayer: ExoPlayer? = null
    private var interactor: Interactor? = null


    //
    // constructor
    //

    init {
        setupItemList()
    }

    //
    // public methods
    //

    fun attach(context: Context, interactor: Interactor) {
        this.interactor = interactor
        if (exoPlayer == null) {
            // start a new item
            exoPlayer = ExoPlayer
                .Builder(context)
                .build()
        }

        exoPlayer!!.addListener(this)
    }

    fun detach() {
        interactor = null
        if (exoPlayer != null) {
            exoPlayer!!.stop()
            exoPlayer!!.removeListener(this)
            exoPlayer = null
        }
    }

    fun onItemClicked(view: View, item: AnimalItem) {
        exoPlayer?.run {
            if (isPlaying) {
                stop()
            }

            val mediaItem = MediaItem.fromUri(
                RawResourceDataSource.buildRawResourceUri(item.audioRes)
            )
            // Set the media item to be played.
            setMediaItem(mediaItem)
            // Prepare the player.
            prepare()
            play()
        }

        interactor?.showDetails(view, item)
    }

    //
    // private methods
    //

    private fun setupItemList() {
        itemList.addAll(
            listOf(
                AnimalItem(
                    R.raw.cow,
                    R.drawable.cow,
                    "https://freesound.org/people/Benboncan/sounds/58277",
                    "https://pixabay.com/images/id-1715829/"
                ),
                AnimalItem(
                    R.raw.dog,
                    R.drawable.dog,
                    "https://freesound.org/people/apolloaiello/sounds/347763",
                    "https://pixabay.com/images/id-6945696/"
                ),
                AnimalItem(
                    R.raw.cat,
                    R.drawable.cat,
                    "https://freeanimalsounds.org",
                    "https://pixabay.com/images/id-6946505/"
                ),
                AnimalItem(
                    R.raw.donkey,
                    R.drawable.donkey,
                    "https://freesound.org/people/LeandiViljoen/sounds/502081",
                    "https://pixabay.com/images/id-3931367/"
                ),
                AnimalItem(
                    R.raw.chick,
                    R.drawable.chick,
                    "https://freesound.org/people/deleted_user_2104797/sounds/164489",
                    "https://pixabay.com/images/id-5014150/"
                ),
                AnimalItem(
                    R.raw.barn,
                    R.drawable.chicken,
                    "https://freesound.org/people/snapssound/sounds/623923"
                ),
                AnimalItem(
                    R.raw.pig,
                    R.drawable.piglet,
                    "https://freesound.org/people/confusion_music/sounds/103421",
                    "https://pixabay.com/images/id-84702/"
                ),
                AnimalItem(
                    R.raw.duck,
                    R.drawable.drake,
                    "https://freeanimalsounds.org",
                    "https://pixabay.com/images/id-2028573/"
                ),
                AnimalItem(
                    R.raw.horse,
                    R.drawable.horse,
                    "https://freesound.org/people/GoodListener/sounds/322445",
                    "https://pixabay.com/images/id-3419146/"
                ),
                AnimalItem(
                    R.raw.sheep,
                    R.drawable.lamb,
                    "https://freeanimalsounds.org",
                    "https://pixabay.com/images/id-4207116/"
                ),
                AnimalItem(
                    R.raw.owl,
                    R.drawable.owl,
                    "https://freesound.org/people/Anthousai/sounds/398734",
                    "https://pixabay.com/images/id-2307405/"
                ),
                AnimalItem(
                    R.raw.bee,
                    R.drawable.bee,
                    "https://freesound.org/people/zedkah/sounds/134813",
                    "https://pixabay.com/images/id-5618012/"
                ),
                AnimalItem(
                    R.raw.elephant,
                    R.drawable.elephant,
                    "https://freeanimalsounds.org",
                    "https://pixabay.com/images/id-2178578/"
                ),
                AnimalItem(
                    R.raw.goat,
                    R.drawable.goat,
                    "https://freeanimalsounds.org",
                    "https://pixabay.com/images/id-4256223/"
                ),
            )
        )
    }

    //
    // Player.Listener
    //

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (!isPlaying) {
            interactor?.hideDetails()
        }
    }

    //
    // BindingAdapter
    //
    companion object {
        @JvmStatic
        @BindingAdapter("filePath")
        fun imageFromFile(imageView: ImageView, filePath: String?) {
            if (filePath != null) {
                imageView.load(File(filePath))
            }
        }

        @JvmStatic
        @BindingAdapter("url")
        fun imageFromUrl(imageView: ImageView, url: String?) {
            imageView.load(url)
        }

        @JvmStatic
        @BindingAdapter("drawable")
        fun imageFromDrawable(imageView: ImageView, @DrawableRes drawable: Int) {
            imageView.load(drawable)
        }
    }
}