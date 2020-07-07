package com.todo
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import java.io.*

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemClickListener {
    private lateinit var toDoObj: ListView;
    private lateinit var input: EditText;
    private lateinit var btnAdd: Button;

    private lateinit var toDoList: ArrayList<String>;
    private lateinit var toDoListAdapter: ArrayAdapter<String>;
    private val FILENAME: String = "Listinfo.dat";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toDoObj = findViewById(R.id.toDoList);
        input = findViewById(R.id.inputAdd);
        btnAdd = findViewById(R.id.btnAdd);

        toDoList = readData(this);

        toDoListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toDoList);
        toDoObj.adapter = toDoListAdapter;

        btnAdd.setOnClickListener(this);

        toDoObj.onItemClickListener = this;
    }

    private fun writeData(items: ArrayList<String>, context: Context){
        try {
            var fos: FileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            var oos = ObjectOutputStream(fos);
            oos.writeObject(items);
            oos.close();
        }
        catch (e: FileNotFoundException){
            e.printStackTrace();
        }
        catch (e: IOException){
            e.printStackTrace();
        }
    }

    private fun readData(context: Context): ArrayList<String> {
        var itemsList: ArrayList<String> = arrayListOf<String>()
        try{
            var fis: FileInputStream = context.openFileInput(FILENAME);
            var ois = ObjectInputStream(fis);
            itemsList = ois.readObject() as ArrayList<String>;
        }
        catch (e: FileNotFoundException){
            e.printStackTrace();
        }
        catch (e: IOException){
            e.printStackTrace();
        }
        return  itemsList;
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btnAdd -> addToDo()
            }
        }
    }

    private fun addToDo(){
        var toDoInput: String = input.text.toString();
        toDoListAdapter.add(toDoInput);
        input.setText("");
        writeData(toDoList,this);
        val toast = Toast.makeText(this,"Oggetto aggiunto", Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onItemClick(p0: AdapterView<*>?, v: View?, position: Int, p3: Long) {
        toDoList.removeAt(position);
        writeData(toDoList, this);
        toDoListAdapter.notifyDataSetChanged();

        var toast = Toast.makeText(this, "Oggetto eliminato",Toast.LENGTH_SHORT)
        toast.show()

    }
}