package com.namh.podopener.frag

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError

import com.namh.podopener.R
import com.namh.podopener.downloader.DownloadIdStore
import com.namh.podopener.downloader.PodBroadcastReceiver
import com.namh.podopener.recyclerview.DividerItemDecoration
import com.namh.podopener.recyclerview.Pod
import com.namh.podopener.recyclerview.PodAdapter
import org.json.JSONObject
import volley.PodRssRequest
import volley.PodVolleyRequestQueue
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by namh on 2016-08-20.
 */


abstract class Frag : Fragment(),
        Response.Listener<List<Pod>>, Response.ErrorListener {

    protected var _queue: RequestQueue? = null
    protected var _adapter: PodAdapter? = null

    protected var mParam1: String? = null
    protected var mParam2: String? = null


    private var __rv: RecyclerView? = null



    ////////////////////////////////////////////////////////////
    //
    //
    //  methods
    //
    //
    ////////////////////////////////////////////////////////////

    protected fun _setAppBar(view: View) {
        // toolbar
        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        val atvt = activity as AppCompatActivity
        atvt.setSupportActionBar(toolbar)

        // back button
        val ab = atvt.supportActionBar
        ab!!.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        ab.setDisplayHomeAsUpEnabled(true)
    }

    protected fun _setFab(view: View) {
        val fab = view.findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
    }

    protected fun _loadPodInfo(url: String, rv: RecyclerView?, tag: String="Frag"){
        __rv = rv

        _queue = PodVolleyRequestQueue.getInstance(this.activity.applicationContext).requestQueue

        val jsonRequest = PodRssRequest(Request.Method.GET, url,
                JSONObject(), this, this)
        jsonRequest.setTag(tag)

        // request
        _queue!!.add(jsonRequest)
        // @see onResponse

    }

    protected fun _setRecyclerView(rv: RecyclerView?, lm: RecyclerView.LayoutManager){

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv!!.setHasFixedSize(true);

        // use a linear layout manager
        rv!!.setLayoutManager(lm);


        // divider
        rv?.addItemDecoration(DividerItemDecoration(this.context, R.drawable.divider))

        rv!!.setAdapter(_adapter);


    }

    ////////////////////////////////////////////////////////////
    //
    //
    //  override
    //
    //
    ////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // for options menu
        setHasOptionsMenu(true)

        if (arguments != null) {
            mParam1 = arguments.getString(Frag.ARG_PARAM1)
            mParam2 = arguments.getString(Frag.ARG_PARAM2)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        } else if (id == android.R.id.home) {
            // open the Drawer menu
            val drawer = activity.findViewById(R.id.drawer_layout) as DrawerLayout
            drawer.openDrawer(GravityCompat.START)
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    open fun getPodName(): String{
        return "Frag"
    }
    //---------------------------------------------------------------- Volley

    override fun onErrorResponse(error: VolleyError) {
        // mTextView!!.setText(error.getMessage())
    }

    override fun onResponse(response: List<Pod>) {
        _adapter = PodAdapter(response, onItemClick=f@{ view, pod ->

            val status = pod.status
            when(status){
                DownloadManager.STATUS_RUNNING -> {
                    val dm = this.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    dm.remove(pod.downloadId)
                    pod.status = DownloadManager.STATUS_FAILED
                    return@f
                }
                DownloadManager.STATUS_FAILED -> {

                    // change status
                    (view.findViewById(R.id.tv_pod_etc) as TextView).text = "Downloading"
                    pod.status = DownloadManager.STATUS_RUNNING


                    val dlpath = "podopener"
                    val name = getPodName()

                    var javaDate: Date? = null
                    try{
                        val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
                        javaDate = formatter.parse(pod.pubDate)
                    } catch (pe: ParseException){
                        val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm zzz", Locale.ENGLISH)
                        javaDate = formatter.parse(pod.pubDate)
                    }
                    val outFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    val date = outFormatter.format(javaDate)

                    val newFilename = "$dlpath/$name-$date.mp3"


                    // Make a request
                    val request = DownloadManager.Request(Uri.parse(pod.url))
                            .setAllowedOverRoaming(false)
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .setTitle(newFilename)
                            .setDescription(newFilename)


                    if (android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED) {
                        request.setDestinationInExternalPublicDir(
                                android.os.Environment.DIRECTORY_DOWNLOADS, newFilename)
                    }

                    // you can hide download status
                    // request.setVisibleInDownloadsUi(false);
                    // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

                    // enqueue
                    val dm = this.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val id = dm.enqueue(request)
                    pod.downloadId = id
                    DownloadIdStore.getInstance().put(id, pod)

                }
                else -> {
                    Log.i("Frag", "status : " + status)
                }

            }


        })
        __rv!!.adapter = _adapter

    }



    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        public val ARG_PARAM1 = "param1"
        public val ARG_PARAM2 = "param2"
    }
}