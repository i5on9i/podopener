
/**
 * Created by namh on 2016-08-20.
 */
package com.namh.podopener.frag

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.namh.podopener.R
import com.namh.podopener.downloader.PodBroadcastReceiver
import com.namh.podopener.frag.FragEcoToday.OnFragmentInteractionListener

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragEcoToday.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragEcoToday : Frag(){


    // TODO: Rename and change types of parameters

    private var mListener: OnFragmentInteractionListener? = null

    private var _rv: RecyclerView? = null
    private var _lm: RecyclerView.LayoutManager? = null

    protected var podUrl: String = "http://enabler.kbs.co.kr/api/podcast_channel/feed.xml?channel_id=R2014-0346"


    ////////////////////////////////////////////////////////////
    //
    //
    //  Overrides
    //
    //
    ////////////////////////////////////////////////////////////


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // inflate
        val view = inflater!!.inflate(R.layout.fragment_ecotoday, container, false)

        _setAppBar(view)
        _setFab(view)

        _rv = view.findViewById(R.id.rv_ecotoday) as RecyclerView
        _setRecyclerView(_rv,
                        LinearLayoutManager(this.context))
        _loadPodInfo(url="http://enabler.kbs.co.kr/api/podcast_channel/feed.xml?channel_id=R2014-0346",
                rv=_rv, tag= getPodName())

        // Register BroadcastReceiver
        this.context.registerReceiver(PodBroadcastReceiver(),
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        // Inflate the layout for this fragment
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context as OnFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragEcoTodayInteraction(uri: Uri)
    }


    ////////////////////////////////////////////////////////////
    //
    //
    //  methods
    //
    //
    ////////////////////////////////////////////////////////////
    override fun getPodName(): String{
        return podName

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragEcoTodayInteraction(uri)
        }
    }

    companion object {

        ////////////////////////////////////////////////////////////
        //
        //
        //  Start here...
        //
        //
        ////////////////////////////////////////////////////////////

        private val REQUEST_TAG = "FragEcoToday"
        private val podUrl = "http://enabler.kbs.co.kr/api/podcast_channel/feed.xml?channel_id=R2014-0346"
        private val podName = "ecotoday"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment FragEcoToday.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): FragEcoToday {
            val fragment = FragEcoToday()
            val args = Bundle()
            args.putString(Frag.ARG_PARAM1, param1)
            args.putString(Frag.ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    ////////////////////////////////////////////////////////////
    //
    //
    //  Implements
    //
    //
    ////////////////////////////////////////////////////////////



}// Required empty public constructor
