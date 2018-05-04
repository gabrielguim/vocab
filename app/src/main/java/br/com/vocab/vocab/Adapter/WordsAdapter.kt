package br.com.vocab.vocab.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.vocab.vocab.DetailActivity
import br.com.vocab.vocab.Model.Word
import br.com.vocab.vocab.R
import br.com.vocab.vocab.Utils.Service
import kotlinx.android.synthetic.main.word_row.view.*

/**
 * Created by Gabriel on 03/05/2018.
 */
class WordsAdapter(val words: List<Word>, val context: Context): RecyclerView.Adapter<WordsAdapter.CustomViewHolder>() {


    override fun getItemCount(): Int {
        return words.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.word_row, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val word = words.get(position)

        holder?.bindView(word, context)
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(word: Word, context: Context) {
            itemView.title_pt.text = word.word_pt
            itemView.title_jp.text = word.furigana.plus(" • ").plus(word.kanji)

            if (word.review) {
                itemView.review_word.setImageResource(R.drawable.ic_star_black_24dp)
            }

            itemView.review_word.setOnClickListener {
                println("review")
            }

            itemView.setOnClickListener {
                Service.word = word
                val intent = Intent(context, DetailActivity::class.java)
                context.startActivity(intent)
            }

            itemView.edit_word.setOnClickListener {
                Service.word = word
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("editable", true)
                context.startActivity(intent)
            }
        }

    }

}

