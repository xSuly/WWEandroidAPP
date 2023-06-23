package com.example.wweshowdown

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.wweshowdown.R
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.io.File


class NavigationDrawerFragment : Fragment(R.layout.fragment_navigation_drawer) {

    public lateinit var drawer: DrawerLayout
    public lateinit var navigationView: NavigationView
    public lateinit var toggle: ActionBarDrawerToggle

    private lateinit var profileImage: ImageView

    private val FILE_NAME = "photo.jpg"
    private val REQUEST_CODE = 42
    private lateinit var photoFile: File

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        if (activity is RecyclerActivity) {
            drawer = view.findViewById(R.id.drawer_layout)
            toggle = object : ActionBarDrawerToggle(
                requireActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            ) {
                override fun onDrawerOpened(drawerView: View) {
                    (activity as RecyclerActivity).recyclerView.visibility = View.INVISIBLE
                    super.onDrawerOpened(drawerView)
                }

                override fun onDrawerClosed(drawerView: View) {
                    (activity as RecyclerActivity).recyclerView.visibility = View.VISIBLE
                    super.onDrawerClosed(drawerView)
                }
            }
        }

        drawer.addDrawerListener(toggle)
        toggle.syncState()



        var navigationView = drawer.findViewById<NavigationView>(R.id.nav_view)!!

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setMessage("Are you sure you want to log out?")
                            .setPositiveButton("Log Out") { dialog, which ->
                                // sign out the user
                                FirebaseAuth.getInstance().signOut()
                                LoginManager.getInstance().logOut()
                                // navigate to the login screen
                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(intent)
                                // finish the current activity to prevent going back to it with the back button
                                activity?.finish()
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                    true
                }
                R.id.nav_profile -> {

                }
                R.id.nav_share -> {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Example")
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Check out this recipe channel I found: https://www.youtube.com/@VillageCookingChannel")
                    startActivity(Intent.createChooser(sharingIntent, "Share via"))
                }
                else -> false
            }
            true
        }

        val headerView = navigationView.getHeaderView(0)
        profileImage = headerView.findViewById<ImageView>(R.id.profile_image)

        profileImage.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = context?.let { it1 -> FileProvider.getUriForFile(it1, "com.example.wweshowdown.fileprovider", photoFile) }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(context, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
    }


    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // val takenImage = data?.extras?.get("data") as Bitmap
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            profileImage.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
