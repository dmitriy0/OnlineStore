package com.example.onlinestore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_product, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val array = ArrayList<Product>()

        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))
        array.add(Product("dbfggfg","gfggffg","sdgdggfgf","gdggdfgfdg","dfgfgfdg"))

        recyclerView.adapter = ProductAdapter(array, requireActivity(), requireContext())
        return view
    }
}