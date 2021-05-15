package com.zulham.mtv.ui.favorite.ui.main.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zulham.mtv.adapter.ShowAdapter
import com.zulham.mtv.data.local.room.entity.DataEntity
import com.zulham.mtv.databinding.FragmentFavoriteBinding
import com.zulham.mtv.ui.favorite.ui.main.detail.DetailFavoriteActivity
import com.zulham.mtv.utils.Factory
import com.zulham.mtv.utils.ShowType.MOVIE_TYPE
import kotlinx.android.synthetic.main.empty_data.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val showAdapter = ShowAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemTouchHelper.attachToRecyclerView(binding.rvFavorite)

        showAdapter.setOnItemClickCallback(object : ShowAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataEntity) {
                val intent = Intent(context, DetailFavoriteActivity::class.java)
                val arg = arguments?.getInt(ARG_SECTION_NUMBER)
                val type = if (arg == MOVIE_TYPE) DetailFavoriteActivity.MOVIE else DetailFavoriteActivity.TV_SHOW
                intent.putExtra(DetailFavoriteActivity.EXTRA_SHOW, data.id)
                intent.putExtra(DetailFavoriteActivity.EXTRA_TYPE, type)
                startActivity(intent)
            }

        })

        binding.rvFavorite.apply {
            this.adapter = showAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
        }

        val showType = arguments?.getInt(ARG_SECTION_NUMBER)

        showLoading(true)

        val favFactory = Factory.getInstance(requireActivity())
        favoriteViewModel = ViewModelProvider(this, favFactory)[FavoriteViewModel::class.java]

        val favShow = favoriteViewModel.let {
            if (showType == MOVIE_TYPE) it.favMovie() else it.favTVShow()
        }

        favShow.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()){
                showLoading(false)
                showAdapter.submitList(it)
                empty_data.visibility = View.GONE
            } else {
                showLoading(false)
                empty_data.visibility = View.VISIBLE
            }
        })

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.favProgressBar.visibility = View.VISIBLE
        } else {
            binding.favProgressBar.visibility = View.GONE
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback(){
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int = makeMovementFlags(0, ItemTouchHelper.LEFT)

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null){
                val swipedPosition = viewHolder.adapterPosition
                val dataEntity = showAdapter.getSwipeData(swipedPosition)
                dataEntity?.let { favoriteViewModel.swipeDeleteFav(it) }
            }
        }

    })

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): FavoriteFragment {
            return FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}