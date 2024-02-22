package com.android.boilerplate.ui.sample.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager

import com.android.boilerplate.R
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse
import com.android.boilerplate.databinding.DialogMaterialListBinding
import com.android.boilerplate.databinding.DialogPaymentBinding
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewState
import com.android.boilerplate.ui.article.viewmodel.CreateMaterialViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateMaterialViewState
import com.android.boilerplate.ui.sample.adapter.MaintenanceAdapter
import com.android.boilerplate.ui.sample.adapter.MaterialListAdapter
import com.android.boilerplate.utils.EndlessRecyclerScrollListener
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MaterialListDialog : DialogFragment() ,MaterialListAdapter.ArticleCallback{

    private var viewBinding: DialogMaterialListBinding? = null
    private var callback: ApproveCallBack? = null

    private var linearLayoutManager: LinearLayoutManager? = null
    private var materListAdapter: MaterialListAdapter? = null
    private var scrollListener: EndlessRecyclerScrollListener? = null
    private val viewModel: CreateMaterialViewModel by viewModels()

    private var  itemName=""
    private var   itemID=""
    private var   itemQuantity=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.dialog_material_list,
            container,
            false
        )
    }




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DialogMaterialListBinding.bind(view)
        setClickListener()

        setupPaymentList()
        observeArticleList()
        viewModel.getShowAllMaterial("")
        viewBinding?.searchNameEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used in this example
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Text is changing
            }

            override fun afterTextChanged(s: Editable?) {
                // After the text has changed
                val newText = s.toString()
                viewModel.getShowAllMaterial(newText)
            }
        })

    }

    private fun setupPaymentList() = viewBinding?.run {

        materListAdapter = MaterialListAdapter(requireActivity(), this@MaterialListDialog)
        linearLayoutManager = LinearLayoutManager(context)
        nameslistRecyclerView?.layoutManager = linearLayoutManager
        nameslistRecyclerView?.adapter = materListAdapter
        setupScrollListener()
    }

    private fun setClickListener() {

        viewBinding?.createLinearLayout?.setOnSingleClickListener {

            dismiss()
        }

        viewBinding?.nameTextView?.text="Material List"
    }

    private fun setupScrollListener() {
        linearLayoutManager?.let {
            scrollListener = object : EndlessRecyclerScrollListener(it) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.getShowAllMaterial("")
                }
            }
        }
        scrollListener?.let {
            it.visibleThreshold = 3
            viewBinding?.nameslistRecyclerView?.addOnScrollListener(it)
        }
    }

    private fun observeArticleList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.materialSharedFlow.collect { viewState ->
                    handleViewState(viewState)
                }
            }
        }
    }

    private fun handleViewState(viewState: CreateMaterialViewState) {
        when (viewState) {
            is CreateMaterialViewState.Loading -> Unit
            is CreateMaterialViewState.SuccessMaterialList -> {
                materListAdapter?.clear()
                materListAdapter?.appendData(viewState.response?.data.orEmpty())

            }

            is CreateMaterialViewState.PopupError -> {
                showPopupError(
                    requireActivity(),
                    childFragmentManager,
                    viewState.errorCode,
                    viewState.message
                )
            }

            else -> Unit
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    interface ApproveCallBack {
        fun onSuccess(itemName : String,itemID :String,itemQuantity: String)
    }

    companion object {
        private const val PERMISSION_WRITE_EXTERNAL = 101
        fun newInstance(callback: ApproveCallBack? = null) = MaterialListDialog()
            .apply {
                this.callback = callback

            }

        val TAG: String = MaterialListDialog::class.java.simpleName
    }

    override fun onItemClicked(data: ShowMaterialListResponse.MaterialListData) {
        callback?.onSuccess(data.material_name.toString(),data.material_id.toString(),data.quantity.toString())
        dismiss()
    }
}