package com.epiklp.kotlincoverflowfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.epiklp.kotlincoverflowfirebase.Utils.Common
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if( intent != null){
            val movie_index = intent.getIntExtra("movie_index", -1)
            if(movie_index != -1)
                loadMovieDetail(movie_index)
        }
    }

    private fun loadMovieDetail(movie_index : Int) {
        val movie = Common.movieLoad[movie_index]

        Picasso.get().load(movie.image).into(movie_image)
        movie_title.text = movie.name
        movie_description.text = movie.description
    }
}
