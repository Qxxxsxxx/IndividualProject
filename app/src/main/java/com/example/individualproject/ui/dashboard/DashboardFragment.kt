package com.example.individualproject.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.individualproject.MainActivity
import com.example.individualproject.R
import com.example.individualproject.databinding.FragmentDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var listView: ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val groupList =  ArrayList<String?>()
    private lateinit var database:DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        database = FirebaseDatabase.getInstance().reference.child("Groups")



//        displayGroups()
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        groups()
        displayGroups()
    }

    private fun groups() {
        val context = context as MainActivity
        listView = context.findViewById(R.id.group_list) as ListView
        arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, groupList)
        listView.adapter = arrayAdapter
    }
    private fun displayGroups() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val set: MutableSet<String?> = HashSet()
                val iterator: Iterator<*> = snapshot.children.iterator()
                while (iterator.hasNext()){
                    set.add((iterator.next() as DataSnapshot).key)
                }
                groupList.clear()
                groupList.addAll(set)
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}