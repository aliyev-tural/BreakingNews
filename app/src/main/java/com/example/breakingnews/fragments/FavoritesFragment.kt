package com.example.breakingnews.fragments

import NewsRv
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingnews.MainActivity
import com.example.breakingnews.R
import com.example.breakingnews.databinding.FragmentFavoritesBinding
import com.example.breakingnews.models.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsRv: NewsRv
    lateinit var binding:FragmentFavoritesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)

        newsViewModel = (activity as MainActivity).newsViewModel
        setupFavorites()

        newsRv.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_favouritesFragment_to_articleFragment, bundle)
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsRv.differ.currentList[position]
                newsViewModel.deleteArticles(article)
                Snackbar.make(view,"Removed from favorites",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        newsViewModel.addFavorites(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavorites)
        }
        newsViewModel.getFavoriteNews().observe(viewLifecycleOwner, Observer { articles ->
            newsRv.differ.submitList(articles)
        })


    }
    private fun setupFavorites() {
        newsRv = NewsRv()
        binding.recyclerFavorites.apply {
            adapter = newsRv
            layoutManager = LinearLayoutManager(activity)
        }
    }

}