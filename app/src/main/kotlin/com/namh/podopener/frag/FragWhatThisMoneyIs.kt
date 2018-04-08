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

/**
 * Created by namh on 2016-08-20.
 */


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragWhatThisMoneyIs : Frag() {



    private var mListener: OnFragmentInteractionListener? = null
    private var _rv: RecyclerView? = null




    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // inflate
        val view = inflater!!.inflate(R.layout.fragment_grabeco, container, false)

        _setAppBar(view)
        _setFab(view)

        _rv = view.findViewById(R.id.rv_grabeco) as RecyclerView
        _setRecyclerView(_rv,
                LinearLayoutManager(this.context))
        _loadPodInfo(url= podUrl,
                rv=_rv, tag= getPodName())

        // Register BroadcastReceiver
        this.context.registerReceiver(PodBroadcastReceiver(),
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Inflate the layout for this fragment
        return view
    }

    override fun getPodName(): String{
        return podName

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragWhatThisMoneyIsInteraction(uri)
        }
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
        fun onFragWhatThisMoneyIsInteraction(uri: Uri)
    }

    companion object {

        private val podUrl = "http://wizard2.sbs.co.kr/w3/podcast/V2000006789.xml"
        private val podName = "whatthismoneyis"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment TwoFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): FragGrabEco {
            val fragment = FragGrabEco()
            val args = Bundle()
            args.putString(Frag.ARG_PARAM1, param1)
            args.putString(Frag.ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
