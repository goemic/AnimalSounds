package de.goemic.animalsound

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.goemic.animalsound.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            val tag = MainFragment::javaClass.name
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), tag)
                .addToBackStack(tag)
                .commit()
        }
    }
}