package com.ohyeah5566.fileexplore

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ohyeah5566.fileexplore.databinding.ItemImageFileBinding
import com.ohyeah5566.fileexplore.databinding.NoFilesBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private companion object {
        const val TYPE_NO_FILES = 0
        const val TYPE_FILE = 1
    }
    lateinit var onDirClick: (file: File) -> Unit
    var list = emptyArray<File>()

    class FileViewHolder(val binding: ItemImageFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File, onDirClick: (file: File) -> Unit) {
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
                    onDirClick.invoke(file)
            }
        }

        companion object {
            fun createView(parent: ViewGroup) =
                FileViewHolder(
                    ItemImageFileBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }
    }

    class NoFileViewHolder(val binding: NoFilesBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun createView(parent: ViewGroup) =
                NoFileViewHolder(
                    NoFilesBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.isEmpty()) {
            TYPE_NO_FILES
        } else {
            TYPE_FILE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FILE -> {
                FileViewHolder.createView(parent)
            }
            else -> {
                NoFileViewHolder.createView(parent)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FileViewHolder) {
            holder.bind(list[position], onDirClick)
        }
    }

    override fun getItemCount() = if (list.isNotEmpty()) list.size else 1

    fun updateFiles(files: Array<File>) {
        list = files
        notifyDataSetChanged()
    }
}