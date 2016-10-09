package com.example.aisuluu.testtask;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegionsFragment extends Fragment {

    ArrayList<String> regions;
    ArrayAdapter<String> adapter;
    RequestQueue queue;

    private OnFragmentInteractionListener mListener;

    public RegionsFragment() {
        // Required empty public constructor
    }

    public static RegionsFragment newInstance() {
        RegionsFragment fragment = new RegionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regions, container, false);

        regions = new ArrayList<String>();

        ListView listView = (ListView) view.findViewById(R.id.listViewRegions);

        // Create adapter
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, regions);

        // Set adapter to listView
        listView.setAdapter(adapter);

        queue = Volley.newRequestQueue(getContext());
        String url ="http://api.toidriver.kz/regions/?format=json";

        // Request a string response from the provided URL.
        JsonArrayRequest jreq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                // Get all names of regions from provided URL
                                JSONObject jo = response.getJSONObject(i);
                                String name = jo.getString("name");
                                // Add names of regions to ArrayList
                                regions.add(name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error with JSON");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jreq);

        // Load CitiesFragment, when an item from the list is selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.loadCityInRegion(position);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void loadCityInRegion(int id);
    }
}
