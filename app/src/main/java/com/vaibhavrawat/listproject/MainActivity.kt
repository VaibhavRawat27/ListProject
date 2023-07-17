package com.vaibhavrawat.listproject

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.vaibhavrawat.listproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var arrayList = arrayListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)
        binding.listView.adapter = adapter
        binding.fab.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.custom_layout_update)
            dialog.setCancelable(false)
            val btnUpdate: Button = dialog.findViewById(R.id.btnUpdate)
            val etNewData: EditText = dialog.findViewById(R.id.etNewData)
            val btnCancel : Button = dialog.findViewById(R.id.btnCancel)
            val tvMainInfo : TextView = dialog.findViewById(R.id.tvMainInfo)
            tvMainInfo.text = "Add data"
            etNewData.hint = "Data"
            btnUpdate.setOnClickListener {
                if (etNewData.text.toString().isEmpty()) {
                    etNewData.error = "Please fill the field!"
                } else {
                    adapter.add(etNewData.text.toString())
                    Toast.makeText(this,"Data added successfully",Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
            btnCancel.setOnClickListener {
                dialog.hide()
            }
            dialog.show()
        }


        binding.listView.setOnItemLongClickListener { _, _, position, _ ->
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Title")
            alertDialog.setMessage("What do you want to do with the selected data ${arrayList[position]}?")
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton("DELETE") { _, _ ->
                val deletedItem = arrayList[position]
                arrayList.removeAt(position)
                adapter.notifyDataSetChanged()
                Snackbar.make(binding.root, "Deleted successfully", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        arrayList.add(position, deletedItem)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this, "Undo done successfully!", Toast.LENGTH_SHORT).show()
                    }.show()
            }
            alertDialog.setNegativeButton("UPDATE") { _, _ ->
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.custom_layout_update)
                dialog.setCancelable(false)
                val btnUpdate: Button = dialog.findViewById(R.id.btnUpdate)
                val btnCancel : Button = dialog.findViewById(R.id.btnCancel)
                val tvMainInfo : TextView = dialog.findViewById(R.id.tvMainInfo)
                val etNewData: EditText = dialog.findViewById(R.id.etNewData)
                tvMainInfo.text = "Update : ${arrayList[position]}?"
                btnUpdate.setOnClickListener {
                    if (etNewData.text.toString().isEmpty()) {
                        etNewData.error = "Please fill the field!"
                    } else {
                        arrayList[position] = etNewData.text.toString()
                        Toast.makeText(this,"Data updated successfully",Toast.LENGTH_SHORT).show()
                        adapter.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                }
                btnCancel.setOnClickListener {
                    dialog.hide()
                }
                dialog.show()
            }
            alertDialog.setNeutralButton("CANCEL") { _, _ ->
                alertDialog.setCancelable(true)
            }
            alertDialog.show()
            return@setOnItemLongClickListener true
        }
    }
}
