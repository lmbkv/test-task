package com.example.aisuluu.testtask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CitiesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String regionId;

    ArrayList<String> cities;
    ArrayAdapter<String> adapter;
    RequestQueue queue;
    String regionOfCity;

    PopupWindow popUpWindow;

    private OnFragmentInteractionListener mListener;

    public CitiesFragment() {
        // Required empty public constructor
    }

    public static CitiesFragment newInstance(String param1) {
        CitiesFragment fragment = new CitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            regionId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cities, container, false);

        cities = new ArrayList<String>();

        ListView listView = (ListView) view.findViewById(R.id.listViewCities);

        // Create adapter
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, cities);

        // Set adapter to listView
        listView.setAdapter(adapter);

        queue = Volley.newRequestQueue(getContext());

        String urlCities = "http://api.toidriver.kz/cities/";
        JsonArrayRequest jreqCities = new JsonArrayRequest(urlCities,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                // In JSONArray, find field with name "region"
                                JSONObject jo = response.getJSONObject(i);
                                JSONObject region = jo.getJSONObject("region");

                                // if regionId is equal to regionId of current city, add to cities ArrayList
                                if(region.getString("id").equals(regionId)){
                                    String name = jo.getString("name");
                                    cities.add(name);
                                    regionOfCity = region.getString("name");
                                }
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
        queue.add(jreqCities);

        // Show AlertDialog with info about region and city, when an item in listView is selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(regionOfCity + " область")
                        .setMessage("Город "+ cities.get(position))
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
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
    }
}
