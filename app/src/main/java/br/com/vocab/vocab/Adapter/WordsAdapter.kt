package br.com.vocab.vocab.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.vocab.vocab.DetailActivity
import br.com.vocab.vocab.MainActivity
import br.com.vocab.vocab.Model.Word
import br.com.vocab.vocab.R
import br.com.vocab.vocab.Utils.RESTAPI
import br.com.vocab.vocab.Utils.Service
import kotlinx.android.synthetic.main.word_row.view.*

/**
 * Created by Gabriel on 03/05/2018.
 */
class WordsAdapter(val words: List<Word>, val activity: MainActivity): RecyclerView.Adapter<WordsAdapter.CustomViewHolder>() {


    override fun getItemCount(): Int {
        return words.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.word_row, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val word = words.get(position)

        holder?.bindView(word, this, activity)
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(word: Word, adapter: WordsAdapter, activity: MainActivity) {
            itemView.title_pt.text = word.word_pt
            itemView.title_jp.text = word.furigana.plus(" • ").plus(word.kanji)

            if (word.review) {
                itemView.review_word.setImageResource(R.drawable.ic_star_black_24dp)
            }

            itemView.review_word.setOnClickListener {
                word.review = !word.review
                activity.dialog?.show()
                RESTAPI.editWord(word, callback = { result ->
                    activity.runOnUiThread {
                        adapter.notifyDataSetChanged()
                        activity.dialog?.cancel()

                        if (result) {
                            Toast.makeText(activity, "Palavra atualizada com sucesso.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "Erro atualizar Palavra. Tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }

            itemView.setOnClickListener {
                Service.word = word
                val intent = Intent(activity, DetailActivity::class.java)
                activity.startActivity(intent)
            }

            itemView.edit_word.setOnClickListener {
                Service.word = word
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("editable", true)
                activity.startActivity(intent)
            }
        }

    }

}

