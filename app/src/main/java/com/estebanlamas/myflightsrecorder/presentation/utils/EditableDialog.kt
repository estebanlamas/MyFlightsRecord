package com.estebanlamas.myflightsrecorder.presentation.utils

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.estebanlamas.myflightsrecorder.R


class EditableDialog(val name: String, val callback: Callback): DialogFragment() {

    interface Callback {
        fun onAcceptChange(newName: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity?.layoutInflater

        val view = inflater?.inflate(R.layout.dialog_edit_name, null)
        val editName = view?.findViewById<EditText>(R.id.editName)
        if(name.isNotEmpty()){
            editName?.setText(name)
        }

        builder.setView(view)
            .setPositiveButton(R.string.accept) { _, _ ->
                callback.onAcceptChange(editName?.text.toString())
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                dismiss()
            }
        return builder.create()
    }
}