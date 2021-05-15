package com.zulham.mtv.ui.movie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zulham.mtv.R
import com.zulham.mtv.adapter.ShowAdapter
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.ui.detail.DetailActivity
import com.zulham.mtv.ui.detail.DetailActivity.Companion.EXTRA_SHOW
import com.zulham.mtv.ui.detail.DetailActivity.Companion.EXTRA_TYPE
import com.zulham.mtv.ui.detail.DetailActivity.Companion.MOVIE
import com.zulham.mtv.ui.detail.DetailActivity.Companion.TV_SHOW
import com.zulham.mtv.utils.Factory
import com.zulham.mtv.utils.ShowType.MOVIE_TYPE
import com.zulham.mtv.vo.Status
import kotlinx.android.synthetic.main.error_data.*
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.InternalCoroutinesApi

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@InternalCoroutinesApi
class MovieFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var movieViewModel: MovieViewModel

    private val showAdapter = ShowAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAdapter.setOnItemClickCallback(object : ShowAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataEntity) {
                val intent = Intent(context, DetailActivity::class.java)
                val arg = arguments?.getInt(ARG_SECTION_NUMBER)
                val type = if (arg == MOVIE_TYPE) MOVIE else TV_SHOW
                intent.putExtra(EXTRA_SHOW, data.id)
                intent.putExtra(EXTRA_TYPE, type)
                startActivity(intent)
            }

        })

        rvMovie.apply {
            this.adapter = showAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
        }

        val showType = arguments?.getInt(ARG_SECTION_NUMBER)

        showLoading(true)

        val movieFactory = Factory.getInstance(requireActivity())
        movieViewModel = ViewModelProvider(this, movieFactory)[MovieViewModel::class.java]

        movieViewModel.let {
            if (showType == MOVIE_TYPE) it.setDataMovie(1) else it.setDataTV(1)
        }

        val getData = movieViewModel.let {
            if (showType == MOVIE_TYPE) it.getDataMovie() else it.getDataTV()
        }

        getData.observe(viewLifecycleOwner, { show ->
            if (show != null){
                when (show.status) {
                    Status.LOADING -> {
                        showLoading(true)
                        error_data.visibility = View.GONE
                    }
                    Status.SUCCESS -> {
                        showLoading(false)
                        show.data.let { showAdapter.submitList(it) }
                        error_data.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        Toast.makeText(context, "Something was error, we will check it", Toast.LENGTH_SHORT).show()
                        error_data.visibility = View.VISIBLE
                    }
                }
            }
        })

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            favProgressBar.visibility = View.VISIBLE
        } else {
            favProgressBar.visibility = View.GONE
        }
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): MovieFragment {
            return MovieFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

}
