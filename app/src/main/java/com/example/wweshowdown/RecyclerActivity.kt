package com.example.wweshowdown


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wweshowdown.R


class RecyclerActivity : AppCompatActivity() {
    private lateinit var adapter: WrestlerAdapter
    private lateinit var exampleList: MutableList<WrestlerType>
    public lateinit var recyclerView: RecyclerView
    private lateinit var drawerFragment: NavigationDrawerFragment

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        fillExampleList()
        setUpRecyclerView()
        drawerFragment = supportFragmentManager.findFragmentById(R.id.navigation_drawer_fragment) as NavigationDrawerFragment

    }

    private fun fillExampleList() {
        exampleList = mutableListOf(
            WrestlerType(R.drawable.johncena, "John Cena"),
            WrestlerType(R.drawable.randyorton, "Randy Orton"),
            WrestlerType(R.drawable.tripleh, "Triple H"),
        )
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@RecyclerActivity, LinearLayoutManager.VERTICAL, false)
        adapter = WrestlerAdapter(exampleList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.isVerticalScrollBarEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.wrestler_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView


        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        return true
    }

    override fun onBackPressed() {
        if (drawerFragment.isVisible()) {
            drawerFragment.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }
}