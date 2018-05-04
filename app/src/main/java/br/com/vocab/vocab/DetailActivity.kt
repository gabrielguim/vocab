package br.com.vocab.vocab

import android.content.Intent
import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import br.com.vocab.vocab.Adapter.ExamplesAdapter
import br.com.vocab.vocab.Model.Word
import br.com.vocab.vocab.Utils.RESTAPI
import br.com.vocab.vocab.Utils.Service
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_examples.view.*
import kotlinx.android.synthetic.main.fragment_general.view.*
import kotlinx.android.synthetic.main.fragment_mnemonic.view.*

class DetailActivity : AppCompatActivity() {

    var dialog: AlertDialog? = null

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()

        setTitle(Service.word?.word_pt!!)

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.detail_menu, menu)

        if (Service.word?.review!!) {
            menu.findItem(R.id.action_star).setIcon(R.drawable.ic_star_white_24dp)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_star -> {
                val word = Service.word
                word?.review = !word?.review!!

                dialog?.show();
                RESTAPI.editWord(word, callback = { result ->
                    runOnUiThread {
                        dialog?.cancel()
                        if (result) {
                            if (word.review) { item.setIcon(R.drawable.ic_star_white_24dp) }
                            else { item.setIcon(R.drawable.ic_star_border_white_24dp) }
                            
                            Toast.makeText(this, "Palavra atualizada com sucesso.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Erro atualizar Palavra. Tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })

                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            when(position) {
                0 -> {
                   return GeneralFragment()
                }

                1 -> {
                    return MnemonicFragment()
                }

                2 -> {
                    return ExamplesFragment()
                }

                else -> {
                    return GeneralFragment()
                }
            }
        }

    }

    class GeneralFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_general, container, false)

            setUpInfo(rootView)

            return rootView
        }

        fun setUpInfo(view: View) {
            val word: Word = Service.word!!

            view.word_text.text = word.word_pt
            view.word_en_text.text = word.word_en
            view.furigana_text.text = word.furigana
            view.kanji_text.text = word.kanji
            view.category_text.text = word.gramatical_class

            view.open_kanji.setOnClickListener {
                val uri = Uri.parse("http://jisho.org/search/".plus(word.kanji))
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        }

    }

    class MnemonicFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_mnemonic, container, false)

            setUpInfo(rootView)
            return rootView
        }

        fun setUpInfo(view: View) {
            val word: Word = Service.word!!

            view.mnemonic_text.text = word.mnemonic_text

            if (!word.mnemonic_image.isEmpty()) {
                Glide.with(this).load("https://vocabpractice-cbb5.restdb.io/media/".plus(word.mnemonic_image[0])).into(view.mnemonic_image)
            }

        }

    }

    class ExamplesFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_examples, container, false)

            setUpInfo(rootView)
            return rootView
        }

        fun setUpInfo(view: View) {
            val word: Word = Service.word!!

            view.recyclerViewEx.layoutManager = LinearLayoutManager(context)
            view.recyclerViewEx.adapter = ExamplesAdapter(word.examples, context)

        }

    }
}
