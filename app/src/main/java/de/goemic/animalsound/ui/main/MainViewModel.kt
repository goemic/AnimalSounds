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

    var exoPlayer: ExoPlayer? = null
    var interactor: Interactor? = null


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
            exoPlayer = null

            exoPlayer!!.removeListener(this)
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
        // todo: add license relevant information
        itemList.addAll(
            listOf(
                AnimalItem(R.raw.cow, R.drawable.cow),
                AnimalItem(R.raw.dog, R.drawable.dog),
                AnimalItem(R.raw.cat, R.drawable.cat),
                AnimalItem(R.raw.donkey, R.drawable.donkey),
                AnimalItem(R.raw.chick, R.drawable.chick),
                AnimalItem(R.raw.barn, R.drawable.chicken),
                AnimalItem(R.raw.pig, R.drawable.piglet),
                AnimalItem(R.raw.duck, R.drawable.drake),
                AnimalItem(R.raw.horse, R.drawable.horse),
                AnimalItem(R.raw.sheep, R.drawable.lamb),
                AnimalItem(R.raw.owl, R.drawable.owl),
                AnimalItem(R.raw.bee, R.drawable.bee),
                AnimalItem(R.raw.elephant, R.drawable.elephant),
                AnimalItem(R.raw.goat, R.drawable.goat),
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