package com.todo
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.io.*

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemClickListener {
    private lateinit var toDoObj: ListView;
    private lateinit var input: EditText;
    private lateinit var btnAdd: Button;

    private lateinit var list: ArrayList<String>;
    private lateinit var toDoListAdapter: ArrayAdapter<String>;
    private val FILENAME: String = "Listinfo.dat";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toDoObj = findViewById(R.id.toDoList);
        input = findViewById(R.id.inputAdd);
        btnAdd = findViewById(R.id.btnAdd);

        list = readData(this);

        toDoListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
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
        writeData(list,this);
        val toast = Toast.makeText(this,"Oggetto aggiunto", Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onItemClick(p0: AdapterView<*>?, v: View?, position: Int, p3: Long) {

        var alert = AlertDialog.Builder(this)
        alert.setTitle("ELIMINARE")
        alert.setMessage("Sei sicuro/a di voler eliminare questo elemento?")
        alert.setPositiveButton("Conferma") { _: DialogInterface, _: Int ->
            list.removeAt(position);
            writeData(list, this);
            toDoListAdapter.notifyDataSetChanged();
            val toast = Toast.makeText(this, "Elemento eliminato",Toast.LENGTH_SHORT)
            toast.show()
        }
        alert.setNegativeButton("Annulla") { _: DialogInterface, _: Int ->
            val toast = Toast.makeText(this, "Annullato",Toast.LENGTH_SHORT)
            toast.show()
        }
        alert.show()
    }
}