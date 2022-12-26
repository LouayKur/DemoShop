package com.mckproject.demoshop.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.mckproject.demoshop.databinding.FragmentOrderBinding
import com.mckproject.demoshop.ui.activities.MainActivity
import com.mckproject.demoshop.ui.shopping.ProductListAdapter
import com.mckproject.demoshop.ui.shopping.ShoppingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderFragment : Fragment() {

    private val orderViewModel by viewModel<OrderViewModel>()
    private var _binding: FragmentOrderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerView.apply {
            addItemDecoration(
                DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL)
            )
            adapter = OrderListAdapter(
                onOrderClicked = ::onOrderClicked
            ).also { adapter->
                orderViewModel.list.observe(viewLifecycleOwner){
                    adapter.submitList(it)
                }
            }
        }
           // Log.w("TEEESSTTT", orderViewModel.text.value.toString())

        return root
    }

    private fun onOrderClicked(order: Order){
        findNavController().navigate(OrderFragmentDirections
            .actionNavigationOrderToOrderDetailFragment(order))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}