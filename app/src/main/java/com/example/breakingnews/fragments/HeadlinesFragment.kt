package com.example.breakingnews.fragments

import NewsRv
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingnews.MainActivity
import com.example.breakingnews.R
import com.example.breakingnews.api.ResearchKey
import com.example.breakingnews.databinding.FragmentHeadlinesBinding
import com.example.breakingnews.models.NewsViewModel
import com.example.breakingnews.models.Resource

class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsRvAdapter:NewsRv
    lateinit var retryButton:Button
    lateinit var errorText:TextView
    lateinit var itemHeadlinesError:CardView
    lateinit var binding: FragmentHeadlinesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentHeadlinesBinding.bind(view)

        itemHeadlinesError=view.findViewById(R.id.itemHeadlinesError)

        val inflater=requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View=inflater.inflate(R.layout.error,null)

        retryButton=view.findViewById(R.id.retryButton)
        errorText=view.findViewById(R.id.errorText)

        newsViewModel=(activity as MainActivity).newsViewModel
        setUpHeadlines()

        newsRvAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_headlinesFragment_to_articleFragment,bundle)
        }

        newsViewModel.headlines.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsRvAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages=newsResponse.totalResults / ResearchKey.PAGE_SIZE + 2
                        isLastPage=newsViewModel.headlinesPage == totalPages
                        if(isLastPage){
                            binding.recyclerHeadlines.setPadding(0,0,0,0)
                        }


                    }
                }
                is Resource.Error<*> ->{
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"Error message:$message",Toast.LENGTH_SHORT).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading<*> -> {
                    showProgressBar()
                }
            }
        })

        retryButton.setOnClickListener {
            newsViewModel.getHeadlines("us")
        }
    }

    var isError=false
    var isloading=false
    var isLastPage=false
    var isScrolling=false

    private fun hideProgressBar(){
        binding.ProgressBar.visibility=View.INVISIBLE
        isloading=false
    }
    private fun showProgressBar(){
        binding.ProgressBar.visibility=View.VISIBLE
        isloading=true
    }
    private fun hideErrorMessage(){
        itemHeadlinesError.visibility=View.INVISIBLE
        isError=false
    }
    private fun showErrorMessage(message:String){
        itemHeadlinesError.visibility=View.VISIBLE
        errorText.text=message
        isError=true
    }

    val scrollListener=object:RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotErrors =!isError
            val isNotLoadingandLastPage =!isloading && !isLastPage
            val isAtLastItem=firstVisibleItemPosition + visibleItemCount >=totalItemCount
            val isNotAtBeginning=firstVisibleItemPosition>=0
            val isTotalMoreThanVisible=totalItemCount >= ResearchKey.PAGE_SIZE
            val shouldPaginate=isNotErrors && isNotLoadingandLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                newsViewModel.getHeadlines("en")
                isScrolling=false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }
    }

        private fun setUpHeadlines(){
            newsRvAdapter=NewsRv()
            binding.recyclerHeadlines.apply {
                adapter=newsRvAdapter
                layoutManager=LinearLayoutManager(activity)
                addOnScrollListener(this@HeadlinesFragment.scrollListener)
            }
        }
}