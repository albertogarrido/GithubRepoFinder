package net.albertogarrido.githubreposearch.ui.repofinder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_github_repo.view.*
import net.albertogarrido.githubreposearch.R
import net.albertogarrido.githubreposearch.network.GithubRepo


class GithubRepoAdapter(private val reposList: List<GithubRepo>) : RecyclerView.Adapter<GithubRepoAdapter.GithubRepoViewHolder>() {
    private lateinit var listener: (View, ImageView, Int) -> Unit

    override fun getItemCount(): Int {
        return reposList.size
    }

    override fun onBindViewHolder(holder: GithubRepoViewHolder, position: Int) {
        val repo = reposList[position]
        holder.bind(repo)
    }

    fun addRecyclerItemClickListener(listener: (View, ImageView, Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GithubRepoViewHolder {
        if (!::listener.isInitialized) {
            throw IllegalArgumentException("listener in adapter should be initialized")
        }
        val itemView: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_github_repo, viewGroup, false)
        return GithubRepoViewHolder(itemView, listener)
    }

    class GithubRepoViewHolder(view: View, private val listener: (View, ImageView, Int) -> Unit) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val container: ViewGroup = view.container
        private val authorAvatar: ImageView = view.authorAvatar
        private val repoName: TextView = view.repoName
        private val repoDescription: TextView = view.repoDescription
        private val countForks: TextView = view.countForks
        private val countWatchers: TextView = view.countWatchers

        fun bind(repo: GithubRepo) {
            repoName.text = repo.name
            repoDescription.text = repo.description ?: ""
            countForks.text = repo.forksCount.toString()
            countWatchers.text = repo.watchersCount.toString()
            Picasso.get()
                    .load(repo.owner.avatarUrl)
                    .resizeDimen(R.dimen.image_side_size, R.dimen.image_side_size)
                    .centerCrop()
                    .into(authorAvatar)
            container.setOnClickListener(this)
        }

        override fun onClick(viewCaller: View) {
            listener(viewCaller, authorAvatar, adapterPosition)
        }
    }
}