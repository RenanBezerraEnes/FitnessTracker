package co.tiagoaguiar.fitnesstracker

import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.fitnesstracker.model.Calc
import java.text.SimpleDateFormat
import java.util.Locale

class ListCalcActivity : AppCompatActivity(), OnListClickListener {
    private lateinit var rvListCalc: RecyclerView
    private lateinit var adapter: CalcAdapter
    private lateinit var result: MutableList<Calc>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        result = mutableListOf<Calc>()
        adapter = CalcAdapter(result, this)

        rvListCalc = findViewById(R.id.list_calc)
        rvListCalc.layoutManager = LinearLayoutManager(this)
        rvListCalc.adapter = adapter

        val type =
            intent?.extras?.getString("type") ?: throw IllegalStateException("type not found")

        Thread {
            val app = (application as App)
            val dao = app.db.calcDao()
            val response = dao.getRegisterByType(type)

            runOnUiThread {
                result.addAll(response)
                adapter.notifyDataSetChanged()
            }
        }.start()

    }

    override fun onClick(id: Int, type: String) {
        when (type) {
            "imc" -> {
                val intent = Intent(this, ImcActivity::class.java)
                intent.putExtra("updateId", id)
                startActivity(intent)
            }
            "tmb" -> {
                val intent = Intent(this, TmbActivity::class.java)
                intent.putExtra("updateId", id)
                startActivity(intent)
            }
        }
        finish()
    }

    override fun onLongClick(position: Int, calc: Calc) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.delete_message))
            .setNegativeButton(android.R.string.cancel) { dialog, which ->
            }
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                Thread {
                    val app = application as App
                    val dao = app.db.calcDao()
                    val response = dao.delete(calc)

                    if (response > 0) {
                        runOnUiThread {
                            result.removeAt(position)
                            adapter.notifyItemRemoved(position)
                        }
                    }
                }.start()

            }
            .create()
            .show()
    }

    private inner class CalcAdapter(
        private val calcItems: List<Calc>,
        private val listener: OnListClickListener
    ) : RecyclerView.Adapter<CalcAdapter.CalcViewHolder>() {

        // 1 - specific layout of cel(item of the list)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalcViewHolder {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return CalcViewHolder(view)
        }

        // 2 - Everytime we scroll and it is needed to change the content of the cel(item)
        override fun onBindViewHolder(holder: CalcViewHolder, position: Int) {
            val currentItem = calcItems[position]
            holder.bind(currentItem)
        }

        // 3 - How many cells this list has
        override fun getItemCount(): Int {
            return calcItems.size
        }

        //individual (cel)
        private inner class CalcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: Calc) {

                val tv = itemView as TextView

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
                val date = sdf.format(item.createdDate)
                val res = item.res

                tv.text = getString(R.string.list_response, res, date)


                tv.setOnLongClickListener {
                    listener.onLongClick(adapterPosition, item)
                    true
                }

                tv.setOnClickListener {
                    listener.onClick(item.id, item.type)
                }
            }
        }

    }
}