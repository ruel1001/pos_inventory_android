package com.android.boilerplate.ui.sample.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.android.boilerplate.R
import com.android.boilerplate.databinding.DialogPaymentBinding
import com.android.boilerplate.utils.setOnSingleClickListener

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpensesDialog : DialogFragment() {

    private var viewBinding: DialogPaymentBinding? = null
    private var callback: ApproveCallBack? = null

    private var file = ""
    private var remarks = ""
    private var name = ""
    private var isUpdate = false
    private var isCreate = false
    private var isDelete = false
    private var isSearch = false
    lateinit var imgFile: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.dialog_payment,
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
        viewBinding = DialogPaymentBinding.bind(view)
        setClickListener()


    }

    private fun setClickListener() {
        viewBinding?.createLinearLayout?.setOnSingleClickListener {
            callback?.onSuccess(true,false,false,false)
            dismiss()
        }

        viewBinding?.updateLinearLayout?.setOnSingleClickListener {
            callback?.onSuccess(false,true,false,false)
            dismiss()
        }

        viewBinding?.deleteLinearLayout?.setOnSingleClickListener {
            callback?.onSuccess(false,false,true,false)
            dismiss()
        }

        viewBinding?.searchLinearLayout?.setOnSingleClickListener {
            callback?.onSuccess(false,false,false,true)
            dismiss()
        }
        viewBinding?.closeImageView?.setOnClickListener {
            dismiss()
        }
        viewBinding?.nameTextView?.text="Expenses"
    }




    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    interface ApproveCallBack {
        fun onSuccess(isCreate:Boolean,isUpdate:Boolean,isDelete:Boolean,isSearch:Boolean)
    }

    companion object {
        private const val PERMISSION_WRITE_EXTERNAL = 101
        fun newInstance(callback: ApproveCallBack? = null) = ExpensesDialog()
            .apply {
                this.callback = callback

            }

        val TAG: String = ExpensesDialog::class.java.simpleName
    }
}