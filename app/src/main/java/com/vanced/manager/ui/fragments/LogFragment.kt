package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingFragment
import com.vanced.manager.databinding.FragmentLogBinding
import com.vanced.manager.utils.AppUtils.logs
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*

class LogFragment : BindingFragment<FragmentLogBinding>() {

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLogBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        binding.bindData()
    }

    private fun FragmentLogBinding.bindData() {
        val logs = TextUtils.concat(*logs.toTypedArray())
        logText.text = logs
        logSave.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val second = calendar.get(Calendar.SECOND)
                val savePath = requireActivity().getExternalFilesDir("logs")?.path + "/$year$month${day}_$hour$minute$second.log"
                val log =
                    File(savePath)
                FileWriter(log).apply {
                    append(logs)
                    flush()
                    close()
                }
                Toast.makeText(requireActivity(), getString(R.string.logs_saved, savePath), Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(requireActivity(), R.string.logs_not_saved, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}