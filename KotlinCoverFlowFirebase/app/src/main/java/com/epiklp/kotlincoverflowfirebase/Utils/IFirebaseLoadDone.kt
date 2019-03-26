package com.epiklp.kotlincoverflowfirebase.Utils

import com.epiklp.kotlincoverflowfirebase.Model.Movie

interface IFirebaseLoadDone {
    fun onFireBaseLoadSuccess(movieList : List<Movie>)
    fun onFireBaseLoadFail(message : String)

}