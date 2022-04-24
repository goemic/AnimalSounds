package de.goemic.animalsound

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.goemic.animalsound.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    //
    // fields
    //

    private var backPressCounter: Int = 0


    //
    // lifecycle
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), MainFragment.TAG)
                .addToBackStack(MainFragment.TAG)
                .commit()
        }
    }

    override fun onBackPressed() {
        // show info text on every third backpress
        backPressCounter++
        if (backPressCounter % 3 == 0) {
            Toast.makeText(this, R.string.toast_back_press_info, Toast.LENGTH_SHORT).show()
        }

        // prevent onBackPress -> children will most likely do this by accident
        return
    }
}