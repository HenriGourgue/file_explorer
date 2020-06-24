package com.example.tp_android_2

import android.R.attr.path
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class MainActivity : AppCompatActivity() {

    var startPath: String = "/sdcard"
    var path: String = ""
    var items: Array<File> = emptyArray()
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var manager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            this.initialize()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 0)
            if(!grantResults.contains(PackageManager.PERMISSION_DENIED))
                this.initialize()
    }

    private fun initialize(){
        this.path=this.startPath
        val tmp = File(this.path).list()
        this.items = File(this.startPath).listFiles()

        this.adapter = MyRecyclerAdapter(this.items, this::onClickFile)
        this.manager = LinearLayoutManager(this)

        recycler = findViewById(R.id.recycler_view)

        recycler.apply {
            this.layoutManager = manager
            this.adapter = adapter
        }

        adapter.notifyDataSetChanged()

        reloadFileList()
    }

    private fun onClickFile(view: View, file: File){
        if(this.path.indexOf(file.absolutePath) == -1){
            this.startPath = this.path
            this.path = file.absolutePath
        }
        reloadFileList()
    }

    fun reloadFileList() {
        val tempFile = File(this.path)
        if(tempFile.canRead() && tempFile.isDirectory){
            this.items = File(this.path).listFiles()
            if(this.items.isNullOrEmpty()){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Erreur")
                builder.setMessage("Ce dossier est vide.")
                builder.setPositiveButton(android.R.string.ok){ _,_ ->

                }
                builder.show()
            } else {
                recycler.adapter = MyRecyclerAdapter(this.items, this::onClickFile)
            }
            adapter.notifyDataSetChanged()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Erreur")
            builder.setMessage("L'ouverture de fichier n'est pas implémentée.")
            builder.setPositiveButton(android.R.string.ok){ _,_ ->

            }
            builder.show()
        }
    }

    override fun onBackPressed() {
        val curFile = File(this.path)
        if(curFile.parentFile.absolutePath == "/"){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Attention")
            builder.setMessage("Voulez-vous quitter ?")
            builder.setPositiveButton(android.R.string.yes){ _,_ ->
                super.onBackPressed()
            }
            builder.setNegativeButton(android.R.string.no) { _,_ ->
                //YOU DO NOTHING JOHN SNOW
            }
            builder.show()
        } else {
            this.path = curFile.parentFile.absolutePath
            this.reloadFileList()
        }
    }
}