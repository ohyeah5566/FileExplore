package com.ohyeah5566.fileexplore

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ohyeah5566.fileexplore.databinding.ItemImageFileBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileAdapter : RecyclerView.Adapter<FileAdapter.ViewHolder>() {
    lateinit var onFileClick: (file: File) -> Unit
    var list = emptyArray<File>()

    class ViewHolder(val binding: ItemImageFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File, onFileClick: (file: File) -> Unit) {

            if (file.isFile) {
                Glide.with(itemView.context)
                    .load(file)
                    .into(binding.imageView)

                binding.name.text = file.name
                binding.size.text = Formatter.formatFileSize(itemView.context, file.length())
                binding.date.text = SimpleDateFormat("MM月dd日").format(Date(file.lastModified()))
                binding.detailGroup.visibility = View.VISIBLE
            }
            if (file.isDirectory) {
                Glide.with(itemView.context)
                    .load(R.drawable.ic_folder)
                    .into(binding.imageView)
                binding.name.text = file.name
                binding.detailGroup.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                if (file.isDirectory)
                    onFileClick.invoke(file)
            }
        }

        companion object {
            fun createView(parent: ViewGroup) =
                ViewHolder(
                    ItemImageFileBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createView(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], onFileClick)
    }

    override fun getItemCount() = list.size

    fun updateFiles(files: Array<File>) {
        list = files
        notifyDataSetChanged()
    }
}