package br.com.vocab.vocab

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import br.com.vocab.vocab.Adapter.ExamplesAdapter
import br.com.vocab.vocab.Adapter.WordsAdapter
import br.com.vocab.vocab.Model.Word
import br.com.vocab.vocab.Utils.Service
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_examples.*
import kotlinx.android.synthetic.main.fragment_general.*
import kotlinx.android.synthetic.main.fragment_general.view.*
import kotlinx.android.synthetic.main.fragment_mnemonic.*
import kotlinx.android.synthetic.main.fragment_mnemonic.view.*

class DetailActivity : AppCompatActivity() {

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

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.action_general -> {
                return true
            }

            R.id.action_mnemonic -> {
                return true
            }

            R.id.action_examples -> {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            when(position) {
                1 -> {
                   return GeneralFragment()
                }

                2 -> {
                    return MnemonicFragment()
                }

                3 -> {
                    return ExamplesFragment()
                }

                else -> {
                    return GeneralFragment()
                }
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }

    class GeneralFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_general, container, false)

            setUpInfo()

            return rootView
        }

        fun setUpInfo() {
            val word: Word = Service.word!!

            word_text.text = word.word_pt
            word_en_text.text = word.word_en
            furigana_text.text = word.furigana
            kanji_text.text = word.kanji
            category_text.text = word.gramatical_class

        }

    }

    class MnemonicFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_mnemonic, container, false)

            setUpInfo()
            return rootView
        }

        fun setUpInfo() {
            val word: Word = Service.word!!

            Glide.with(this).load(word.mnemonic_image_0).into(mnemonic_image)
            mnemonic_text.text = word.mnemonic_text
        }

    }

    class ExamplesFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_examples, container, false)

            setUpInfo()
            return rootView
        }

        fun setUpInfo() {
            val word: Word = Service.word!!

            recyclerViewEx.layoutManager = LinearLayoutManager(context)
            recyclerViewEx.adapter = ExamplesAdapter(word.examples, context)

        }

    }
}
