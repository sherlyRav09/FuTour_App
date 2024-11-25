package com.example.futour_app.ui.findout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.futour_app.databinding.FragmentFindoutBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.futour_app.R
import com.example.futour_app.WisataAdapter
import com.example.futour_app.WisataItem

class FindOutFragment : Fragment() {

    private val wisataItems = ArrayList<WisataItem>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_findout, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycleView)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = WisataAdapter(wisataItems, requireActivity())
        recyclerView.layoutManager = LinearLayoutManager(activity)

        wisataItems.add(WisataItem(R.drawable.wisata1, "Latte", "0", "0"))
        wisataItems.add(WisataItem(R.drawable.wisata2, "Kapucino", "1", "0"))
        wisataItems.add(WisataItem(R.drawable.wisata3, "Raf", "2", "0"))
        wisataItems.add(WisataItem(R.drawable.wisata4, "Milk Shake", "3", "0"))
        wisataItems.add(WisataItem(R.drawable.wisata5, "Magic Coffee", "4", "0"))
        wisataItems.add(WisataItem(R.drawable.wisata6, "Swedish Coffee", "5", "0"))
        wisataItems.add(WisataItem(R.drawable.wisata7, "Mint Coffee", "6", "0"))
        wisataItems.add(WisataItem(R.drawable.wisata8, "Espresso", "7", "0"))
        wisataItems.add(WisataItem(R.drawable.wisata9, "Americano", "8", "0"))

        return root
    }
}
