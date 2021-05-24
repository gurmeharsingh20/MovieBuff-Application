package com.GurmeharSingh.moviebuff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide

class DescriptionActivity : AppCompatActivity() {

    lateinit var firstImage: ImageView
    lateinit var secondImage: ImageView
    lateinit var titleDescription: TextView
    lateinit var ratingDescription: TextView
    lateinit var dateDescription: TextView
    lateinit var overview: TextView
    lateinit var overviewDescription: TextView
    lateinit var review: TextView
    lateinit var reviewDescription: TextView
    lateinit var author: TextView
    lateinit var user: TextView
    lateinit var progressBar: ProgressBar

    var movieId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);


        firstImage = findViewById(R.id.firstImage)
        secondImage = findViewById(R.id.secondImage)
        titleDescription = findViewById(R.id.title_description)
        ratingDescription = findViewById(R.id.rating_description)
        dateDescription = findViewById(R.id.date_description)
        overview = findViewById(R.id.overview)
        overviewDescription = findViewById(R.id.overview_description)
        review = findViewById(R.id.review)
        reviewDescription = findViewById(R.id.review_description)
        author = findViewById(R.id.author)
        user = findViewById(R.id.user)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        if (intent != null) {
            movieId = intent.getStringExtra("movie_Id")
        } else {
            finish()
            Toast.makeText(this, "Some Unexpected Error Occurred", Toast.LENGTH_LONG).show()
        }

        if (movieId == "100") {
            finish()
            Toast.makeText(this, "Some Unexpected Error Occurred", Toast.LENGTH_LONG).show()
        }

        val url = "https://api.themoviedb.org/3/movie/"
        val api_key = "?api_key=58e390a09e3b507cd178131442e07b16"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url + movieId + api_key, null,
            Response.Listener {
                progressBar.visibility = View.GONE
                val firstImageUrl = it.getString("backdrop_path")
                val secondImageUrl = it.getString("poster_path")
                Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + firstImageUrl).into(firstImage)
                Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + secondImageUrl).into(secondImage)
                titleDescription.text = it.getString("title")
                var value:String = it.getString("title")
                getSupportActionBar()?.setTitle(value)
                ratingDescription.text = it.getString("vote_average")
                dateDescription.text = it.getString("release_date")
                overviewDescription.text = it.getString("overview")
            },
            Response.ErrorListener {
                Toast.makeText(this, "Some Unexpected Error Occurred", Toast.LENGTH_LONG).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

        val jsonObjectRequestNew = JsonObjectRequest(Request.Method.GET, url + movieId + "/reviews" + api_key, null,
            Response.Listener {
                progressBar.visibility = View.GONE
                val movieJsonArray =
                    it.getJSONArray("results")
                for (i in 0 until movieJsonArray.length()) {
                    val movieJsonObject = movieJsonArray.getJSONObject(i)
                        reviewDescription.text = movieJsonObject.getString("content")
                        author.text = movieJsonObject.getString("author")
                }
            },
            Response.ErrorListener {
                Toast.makeText(this, "Some Unexpected Error Occurred", Toast.LENGTH_LONG).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequestNew)
    }
}
