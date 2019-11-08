package bonch.dev.networkdz.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import bonch.dev.networkdz.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usersButton = findViewById<Button>(R.id.button_user)
        val albumButton = findViewById<Button>(R.id.button_album)
        val photoButton = findViewById<Button>(R.id.button_photo)
        val dialogButton = findViewById<Button>(R.id.button_dialog)

        var dlg = PostCreateDialogFragment()

        usersButton.setOnClickListener {
            val intent = Intent(MainActivity@ this, UsersActivity::class.java)

            startActivity(intent)
        }

        albumButton.setOnClickListener {
            val intent = Intent(MainActivity@ this, AlbumsActivity::class.java)

            startActivity(intent)
        }

        photoButton.setOnClickListener {
            val intent = Intent(MainActivity@ this, PhotosActivity::class.java)

            startActivity(intent)
        }

        dialogButton.setOnClickListener {
            dlg.show(supportFragmentManager, "dlg")
        }
    }
}
