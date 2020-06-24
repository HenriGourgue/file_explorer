package com.example.tp_android_2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_item.view.*
import java.io.File

class MyRecyclerAdapter(private val dataset: Array<File>, private val onClickListener: (View, File) -> Unit ) : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        if(dataset[position].isDirectory)
            return 0
        else
            return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        if(viewType == 0){
            view = LayoutInflater.from(parent.context).inflate(R.layout.my_item, parent, false)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
        }
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.dataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fullfilename = dataset[position].absolutePath.split("/")
        val filename = fullfilename[fullfilename.size - 1]
        holder.itemView.title_file.text = filename
        holder.itemView.setOnClickListener{
            onClickListener.invoke(it, dataset[position])
        }
    }
}