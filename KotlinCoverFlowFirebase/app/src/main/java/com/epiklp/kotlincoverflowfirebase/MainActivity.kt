package com.epiklp.kotlincoverflowfirebase

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.TextView
import com.epiklp.kotlincoverflowfirebase.Model.Movie
import com.epiklp.kotlincoverflowfirebase.Utils.Common
import com.epiklp.kotlincoverflowfirebase.Utils.IFirebaseLoadDone
import com.epiklp.kotlincoverflowfirebase.Utils.MovieAdapter
import com.google.firebase.database.*
import com.pd.chocobar.ChocoBar
import dmax.dialog.SpotsDialog
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IFirebaseLoadDone {
    override fun onFireBaseLoadSuccess(movieList: List<Movie>) {
        dialog.dismiss()
        setupUI()

        Common.movieLoad = movieList

        movieAdapter = MovieAdapter(this@MainActivity, movieList)
        cover_flow.adapter = movieAdapter
        cover_flow.setOnScrollPositionListener(object : FeatureCoverFlow.OnScrollPositionListener{
            override fun onScrolling() {

            }

            override fun onScrolledToPosition(position: Int) {
                textSwitcher.setText(Common.movieLoad[position].name)
            }

        })

        cover_flow.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, id ->

            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("movie_index", position)
            startActivity(intent)
        }
    }

    @SuppressLint("InflateParams")
    private fun setupUI() {
        setContentView(R.layout.activity_main)

        textSwitcher.setFactory {
            val inflanter = LayoutInflater.from(this@MainActivity)
            inflanter.inflate(R.layout.layout_title, null) as TextView
        }
        val `in` = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        val out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
        textSwitcher.inAnimation = `in`
        textSwitcher.outAnimation = out

        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = resources.displayMetrics.density
        val dpHeight = outMetrics.heightPixels / density
        val dpWidth = outMetrics.widthPixels / density

        cover_flow.coverHeight = dpHeight.toInt()
        cover_flow.coverWidth = dpWidth.toInt()
    }

    @SuppressLint("WrongConstant")
    override fun onFireBaseLoadFail(message: String) {
        ChocoBar.builder().setActivity(this@MainActivity).red().setText(message).setDuration(ChocoBar.LENGTH_SHORT).show()
    }

    lateinit var ifirebaseLoadDone: IFirebaseLoadDone
    lateinit var dbRef : DatabaseReference
    lateinit var dialog: AlertDialog
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ifirebaseLoadDone = this
        loadData()
    }

    private fun loadData() {
        dialog = SpotsDialog.Builder().setContext(this@MainActivity)
            .setCancelable(false)
            .setMessage("Please wait...").build()
        dialog.show()

        dbRef = FirebaseDatabase.getInstance().getReference("Movies")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{

            var movies : MutableList<Movie> = ArrayList()

            override fun onCancelled(error: DatabaseError) {
                ifirebaseLoadDone.onFireBaseLoadFail(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(movieSnapShot in snapshot.children){
                    val movie = movieSnapShot.getValue(Movie::class.java)
                    movies.add(movie!!)
                }

                ifirebaseLoadDone.onFireBaseLoadSuccess(movies)
            }

        })


    }
}
