package br.com.vocab.vocab

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import br.com.vocab.vocab.Model.Example
import br.com.vocab.vocab.Model.Word

import kotlinx.android.synthetic.main.activity_add_word.*

class AddWordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun addWord() {
//        val title = "a"
//        val title_en = "a"
//        val furigana = "a"
//        val kanji = "a"
//        val gramatical_class = ""
//        val image_mine = "a"
//        val text_mine = "a"
//        val examples = ArrayList<Example>()
//
//        val word = Word(title, title_en, furigana, kanji, gramatical_class, image_mine, text_mine, examples)
    }

}
