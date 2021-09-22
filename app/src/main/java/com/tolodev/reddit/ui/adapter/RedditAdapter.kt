package com.tolodev.reddit.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tolodev.reddit.R
import com.tolodev.reddit.databinding.ItemRedditPostViewBinding
import com.tolodev.reddit.ui.models.RedditPost

class RedditAdapter(private val action: (redditPost: RedditPost) -> Unit) :
    RecyclerView.Adapter<RedditAdapter.ItemRecipeViewHolder>() {

    private var posts: List<RedditPost> = ArrayList()

    fun setPosts(posts: List<RedditPost>) {
        this.posts = posts
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemRecipeViewHolder {
        val binding = ItemRedditPostViewBinding.inflate(LayoutInflater.from(parent.context)).apply {
            root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ItemRecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemRecipeViewHolder, position: Int) {
        val recipe = posts[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = posts.size

    inner class ItemRecipeViewHolder(private val binding: ItemRedditPostViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(redditPost: RedditPost) {
            with(binding) {
                textViewAuthor.text = redditPost.author
                textViewCreationDate.text = redditPost.createdUtc.toString().plus(root.context.getString(R.string.copy_ago))
                Glide.with(root.context).load(redditPost.thumbnail).into(imageViewPhoto)
                textViewTitle.text = redditPost.title
                textViewComments.text = root.context.getString(R.string.copy_comments).plus(redditPost.numComments.toString())
                viewUnreadPost.isInvisible = redditPost.visited
                root.setOnClickListener {
                    action.invoke(redditPost)
                    val position = posts.indexOf(redditPost)
                    notifyItemChanged(position)
                }
            }
        }
    }
}
