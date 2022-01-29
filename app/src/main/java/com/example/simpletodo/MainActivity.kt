package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLonClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

//        //1. detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener{
//            //Code in here is going to be executed when the user clicks on a button
//            //Log.i("Ugo","User clicked button")
//        }

        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        //Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        //Set layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Set up button and input field so that user can enter a task and add it to list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        // get a reference to button
        //setting on click listener
        findViewById<Button>(R.id.button).setOnClickListener{
            //1. Grab the text the user inputted
            val userInputtedTask = inputTextField.text.toString()
            //2. add string to list of tasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter that out data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)
            //3. reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save the data that the user has inputted
    // Save data by writing and reading from a file

    //Get the file we need
    fun getDataFile(): File {

        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line in the data file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }
    //Save items by writing them into our data file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch(ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}