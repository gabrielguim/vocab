package br.com.vocab.vocab.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.vocab.vocab.Model.Example
import br.com.vocab.vocab.R
import kotlinx.android.synthetic.main.example_row.view.*

/**
 * Created by Gabriel on 03/05/2018.
 */
class ExamplesAdapter(val examples: List<Example>, val context: Context): RecyclerView.Adapter<ExamplesAdapter.CustomViewHolder>() {


    override fun getItemCount(): Int {
        return examples.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.example_row, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val example = examples.get(position)

        holder?.bindView(example, position, context)
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(example: Example, position: Int, context: Context) {
            itemView.title.text = "Exemplo ".plus(position + 1)
            itemView.text_jp.text = example.jp
            itemView.text_pt.text = example.pt
        }

    }

}