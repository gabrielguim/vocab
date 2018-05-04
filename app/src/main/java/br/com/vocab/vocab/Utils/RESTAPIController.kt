package br.com.vocab.vocab.Utils

import br.com.vocab.vocab.Model.Word
import br.com.vocab.vocab.Model.WordList
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import okhttp3.*
import java.io.IOException

/**
 * Created by Gabriel on 03/05/2018.
 */
class RESTAPI() {

    companion object {
        private val getListsURL: String = "https://vocabpractice-cbb5.restdb.io/rest/lists"
        private val getVocabularyURL: String = "https://vocabpractice-cbb5.restdb.io/rest/vocabulary"

        fun getWords(query: String?, callback: (List<Word>?) -> Unit) {
            val list = ArrayList<Word>()

            val request = Request.Builder().url(getVocabularyURL.plus(query))
                    ?.addHeader("content-type", "application/json")
                    ?.addHeader("x-apikey", "5aeb6ae025a622ae4d528803")
                    ?.addHeader("cache-control","no-cache")
                    ?.build()!!

            val client = OkHttpClient()
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()

                    val jsonArray = gson.fromJson(body, JsonArray::class.java)
                    for (json in jsonArray) {
                        val word = gson.fromJson(json, Word::class.java)
                        list.add(word)
                    }

                    callback(list)

                }

                override fun onFailure(call: Call?, e: IOException?) {
                    callback(null)
                }
            })
        }

        fun getList(query: String?, callback: (WordList?) -> Unit) {
            val lists = ArrayList<WordList>()

            val request = Request.Builder().url(getListsURL.plus(query))
                    ?.addHeader("content-type", "application/json")
                    ?.addHeader("x-apikey", "5aeb6ae025a622ae4d528803")
                    ?.addHeader("cache-control","no-cache")
                    ?.build()!!

            val client = OkHttpClient()
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val jsonArray = gson.fromJson(body, JsonArray::class.java)
                    val wordlist = gson.fromJson(jsonArray[0], WordList::class.java)

                    callback(wordlist)
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    callback(null)
                }
            })
        }

        fun getListsName(query: String?, callback: (List<String>?) -> Unit) {
            val names = ArrayList<String>()

            val request = Request.Builder().url(getListsURL.plus(query))
                    ?.addHeader("content-type", "application/json")
                    ?.addHeader("x-apikey", "5aeb6ae025a622ae4d528803")
                    ?.addHeader("cache-control","no-cache")
                    ?.build()!!

            val client = OkHttpClient()
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()

                    val jsonArray = gson.fromJson(body, JsonArray::class.java)
                    for (json in jsonArray) {
                        val word = gson.fromJson(json, WordList::class.java)
                        names.add(word.list_name)
                    }

                    callback(names)
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    callback(null)
                }
            })
        }
    }

}
