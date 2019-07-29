package me.sdi.github.ribs.authorized.repository

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.sdi.github.R
import me.sdi.github.core.Repository

class CurrentUserRepositoriesAdapter : RecyclerView.Adapter<RepositoryViewHolder>() {

    private val userRepositories: MutableList<Repository> = mutableListOf()

    fun update(items: List<Repository>) {
        userRepositories.clear()
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(userRepositories, items))
        userRepositories.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.repository_list_item, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun getItemCount(): Int = userRepositories.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(userRepositories[position])
    }

}

class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val name: TextView = itemView.findViewById(R.id.repository_list_item_project_name)
    private val description: TextView = itemView.findViewById(R.id.repository_list_item_project_description)

    fun bind(repository: Repository) {
        name.text = repository.name

        val drawableRight = if (repository.private) R.drawable.ic_lock_black_24dp else 0
        name.setCompoundDrawablesWithIntrinsicBounds(
            0, 0, drawableRight, 0
        )
        description.text = repository.description
    }
}

class DiffUtilCallback(
    private val before: List<Repository>,
    private val after: List<Repository>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        before[oldItemPosition].id == after[newItemPosition].id

    override fun getOldListSize(): Int = before.size

    override fun getNewListSize(): Int = after.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        before[oldItemPosition] == after[newItemPosition]

}