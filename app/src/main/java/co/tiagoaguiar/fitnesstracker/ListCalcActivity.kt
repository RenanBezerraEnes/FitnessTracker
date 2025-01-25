package co.tiagoaguiar.fitnesstracker

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.fitnesstracker.model.Calc
import java.text.SimpleDateFormat
import java.util.Locale

class ListCalcActivity : AppCompatActivity() {
    private lateinit var rvListCalc: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        val result = mutableListOf<Calc>()
        val adapter = CalcAdapter(result)
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

    private inner class CalcAdapter(
        private val calcItems: List<Calc>,
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

            }
        }

    }
}