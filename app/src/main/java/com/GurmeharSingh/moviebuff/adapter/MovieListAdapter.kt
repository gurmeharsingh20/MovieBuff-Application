package com.GurmeharSingh.moviebuff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.GurmeharSingh.moviebuff.R
import com.GurmeharSingh.moviebuff.model.Movie

class MovieListAdapter(private val listener: MovieItemClicked): RecyclerView.Adapter<MovieViewHolder>()  {

    private val items: ArrayList<Movie> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie,parent,false)
        val viewHolder = MovieViewHolder(view) //View Holder -> helps in getting items positions.
        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        Glide.with(holder.itemView.context).load("https://image.tmdb.org/t/p/w500/" + currentItem.imageUrl).into(holder.image)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateMovie(updatedMovies: ArrayList<Movie>) {
        items.clear()
        items.addAll(updatedMovies)
        notifyDataSetChanged()
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.title)
    val image: ImageView = itemView.findViewById(R.id.image)
}

interface MovieItemClicked {
    fun onItemClicked(item: Movie)
}