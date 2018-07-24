package net.albertogarrido.githubreposearch.ui.repodetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_subscriber.view.*
import net.albertogarrido.githubreposearch.R
import net.albertogarrido.githubreposearch.network.Subscriber

class SubscribersAdapter(private val subscriberList: List<Subscriber>) :
        RecyclerView.Adapter<SubscribersAdapter.SubscribersViewHolder>() {

    override fun getItemCount(): Int {
        return subscriberList.size
    }

    override fun onBindViewHolder(holder: SubscribersViewHolder, position: Int) {
        val subscriber = subscriberList[position]
        holder.bind(subscriber)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SubscribersViewHolder {
        val itemView: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_subscriber, viewGroup, false)
        return SubscribersViewHolder(itemView)
    }

    class SubscribersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val subscriberAvatar: ImageView = view.subscriberAvatar
        private val subscriberLogin: TextView = view.subscriberLogin

        fun bind(subscriber: Subscriber) {
            subscriberLogin.text = subscriber.login
            Picasso.get()
                    .load(subscriber.avatarUrl)
                    .into(subscriberAvatar)
        }

    }
}