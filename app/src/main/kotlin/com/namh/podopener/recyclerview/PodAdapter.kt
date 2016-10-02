package com.namh.podopener.recyclerview

import android.app.DownloadManager
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.namh.podopener.R
import com.namh.podopener.downloader.DownloadIdStore


/**
 * Created by namh on 2016-08-20.
 */
// Provide a suitable constructor (depends on the kind of dataset)
class PodAdapter : RecyclerView.Adapter<PodAdapter.ViewHolder> {

    private val _dataset: List<Pod>

    private val _listener: (v: View, pod: Pod)->Unit

    constructor(dataset: List<Pod>, onItemClick: (v: View, pod: Pod)-> Unit) : super(){
        _dataset = dataset
        _listener = onItemClick

    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // each data item is just a string in this case
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var box: RelativeLayout? = null
        var title: TextView? = null
        init{
            box = view.findViewById(R.id.rl_pod) as RelativeLayout
            title = view.findViewById(R.id.tv_pod_title) as TextView
        }

    }



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PodAdapter.ViewHolder {

        // create a new view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pod, parent, false)
        // set the view's size, margins, paddings and layout parameters

        val vh = ViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title?.text = _dataset[position].title
        holder.box?.setOnClickListener { v ->
            val pod = _dataset[position]
            (v!!.findViewById(R.id.tv_pod_etc) as TextView).text = pod.url
            _listener(v, pod)
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return _dataset.size
    }


}

data class Pod(val title: String,  val url: String, val pubDate: String,
               var downloadId: Long = -1,
               var status: Int = DownloadManager.STATUS_FAILED)
