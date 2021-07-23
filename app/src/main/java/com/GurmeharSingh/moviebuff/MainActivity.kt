package com.GurmeharSingh.moviebuff

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.GurmeharSingh.moviebuff.adapter.MovieItemClicked
import com.GurmeharSingh.moviebuff.adapter.MovieListAdapter
import com.GurmeharSingh.moviebuff.model.Movie
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MovieItemClicked {

    private lateinit var mAdapter: MovieListAdapter
    private lateinit var loading_Icon: ProgressBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var hView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

////        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
////        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
//        getSupportActionBar()?.setIcon(R.drawable.ic_baseline_menu_24)

        supportActionBar?.title = "Popular Movies"


        loading_Icon = findViewById(R.id.loading_icon)
        loading_Icon.visibility = View.VISIBLE

        recyclerView.layoutManager = GridLayoutManager(this,2)
        fetchData()
        mAdapter = MovieListAdapter(this) // 2nd parameter is listener and for that, main activity has implemented interface built in MovieListAdapter.kt file.
        recyclerView.adapter = mAdapter

        //Drawer Code

        navigationView = findViewById(R.id.navigation_view)
        hView = navigationView.inflateHeaderView(R.layout.nav_header)
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close)
        toggle.drawerArrowDrawable.setColor(getResources().getColor(R.color.white))
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.topRated -> {
                    val intent = Intent(this,TopRated::class.java)
                    startActivity(intent)
                }
                R.id.nowPlaying -> {
                    val intent = Intent(this,NowPlaying::class.java)
                    startActivity(intent)
                }
                R.id.upcoming -> {
                    val intent = Intent(this,Upcoming::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchData(){
        val url = "https://api.themoviedb.org/3/movie/popular?api_key=58e390a09e3b507cd178131442e07b16"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                loading_Icon.visibility = View.GONE
                val movieJsonArray =
                    it.getJSONArray("results")
                val movieArray = ArrayList<Movie>()
                for (i in 0 until movieJsonArray.length()) {
                    val movieJsonObject = movieJsonArray.getJSONObject(i)
                    val movies = Movie(
                        movieJsonObject.getString("original_title"),
                        movieJsonObject.getString("poster_path"),
                        movieJsonObject.getString("id")
                    )
                    movieArray.add(movies)
                }
                mAdapter.updateMovie(movieArray)
            },
            Response.ErrorListener {
                loading_Icon.visibility = View.VISIBLE
                Toast.makeText(this,"Some Unexpected Error Occurred",Toast.LENGTH_LONG).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: Movie) {
        val intent = Intent(this,DescriptionActivity::class.java)
        intent.putExtra("movie_Id",item.movie_Id)
        startActivity(intent)
    }
}