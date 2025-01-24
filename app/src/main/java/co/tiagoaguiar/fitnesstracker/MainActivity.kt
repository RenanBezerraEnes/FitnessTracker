package co.tiagoaguiar.fitnesstracker

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(
                id = 1,
                drawableId = R.drawable.baseline_wb_sunny_24,
                textStringId = R.string.imc,
                color = Color.GREEN
            )
        )
        mainItems.add(
            MainItem(
                id = 2,
                drawableId = R.drawable.baseline_account_balance_24,
                textStringId = R.string.tmb,
                color = Color.YELLOW
            )
        )


        val adapter = MainAdapter(mainItems, this)
        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)
    }

    override fun onClick(id: Int) {
        when(id) {
            1 -> {
                val intent = Intent(this, ImcActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private inner class MainAdapter(
        private val mainItems: List<MainItem>,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        // 1 - specific layout of cel(item of the list)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        // 2 - Everytime we scroll and it is needed to change the content of the cel(item)
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val currentItem = mainItems[position]
            holder.bind(currentItem)
        }

        // 3 - How many cells this list has
        override fun getItemCount(): Int {
            return mainItems.size
        }

        //individual (cel)
        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: MainItem) {
                val img: ImageView = itemView.findViewById(R.id.item_img_icon)
                val name: TextView = itemView.findViewById(R.id.item_txt_name)
                val container: LinearLayout = itemView.findViewById(R.id.item_container_imc)

                img.setImageResource(item.drawableId)
                name.setText(item.textStringId)
                container.setBackgroundColor(item.color)

                container.setOnClickListener {
                    onItemClickListener.onClick(item.id)
                }
            }
        }

    }
}