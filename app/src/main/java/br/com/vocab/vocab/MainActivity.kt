package br.com.vocab.vocab

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.vocab.vocab.Adapter.WordsAdapter
import br.com.vocab.vocab.Model.Word
import br.com.vocab.vocab.Utils.RESTAPI
import br.com.vocab.vocab.Utils.Service
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Service.word = null

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        getWords("")
        getListsMenu()

        nav_view.setNavigationItemSelectedListener(this)
    }

    fun getList(query: String) {
        dialog?.show()
        RESTAPI.getList(query, callback = { list ->
            runOnUiThread {
                dialog?.cancel()

                if (list != null) {
                    setUpRecyclerView(list.words)
                } else {
                    Toast.makeText(this, "Erro ao sincronizar a lista. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun getWords(query: String) {
        dialog?.show()
        RESTAPI.getWords(query, callback = { lists ->
            runOnUiThread {
                dialog?.cancel()

                if (lists != null) {
                    setUpRecyclerView(lists)
                } else {
                    Toast.makeText(this, "Erro ao sincronizar as palavras. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun getListsMenu() {
        RESTAPI.getListsName("?q={}&h={\"\$fields\": {\"list_name\": 1} }", callback = { names ->
            runOnUiThread {
                if (names != null) {
                    setUpSubMenus(names)
                } else {
                    Toast.makeText(this, "Erro ao sincronizar as listas. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun setUpSubMenus(lists: List<String>) {
        val groups = nav_view.menu.findItem(R.id.word_groups).subMenu

        for (i in 0 .. lists.count() - 1) {
            val list = lists[i]
            groups.add(i, Menu.FIRST + i, Menu.FIRST, list).setIcon(R.drawable.ic_list_black_24dp)
        }

    }

    fun setUpRecyclerView(words: List<Word>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WordsAdapter(words, this)
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_search -> return actionSearch()
            R.id.action_add -> return actionAdd()
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun actionAdd(): Boolean {
        return true;
    }

    fun actionSearch(): Boolean {
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        setTitle(item.title)

        when (item.itemId) {
            R.id.nav_all_words -> {
                getWords("?q={}&idtolink=true")
            }

            R.id.nav_review -> {
                getWords("?q={\"review\":true}&idtolink=true")
            }

            else -> {
                getList("?q={\"list_name\":\"" + item.title + "\"}&idtolink=true")
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
