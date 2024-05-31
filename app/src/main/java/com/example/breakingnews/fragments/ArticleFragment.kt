package com.example.breakingnews.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.breakingnews.MainActivity
import com.example.breakingnews.R
import com.example.breakingnews.databinding.FragmentArticleBinding
import com.example.breakingnews.models.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
    val args:ArticleFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentArticleBinding.bind(view)

        newsViewModel=(activity as MainActivity).newsViewModel
        val article=args.article

        binding.webView.apply {
            webViewClient= WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }

        binding.fab.setOnClickListener{
            newsViewModel.addFavorites(article)
            Snackbar.make(view,"Added to favorites",Snackbar.LENGTH_SHORT).show()
        }

    }

}