package br.com.vocab.vocab.Model

import kotlin.collections.List

/**
 * Created by Gabriel on 03/05/2018.
 */
class Word (val word_pt: String, val word_en: String, val furigana: String,
            val kanji: String, val gramatical_class: String, val review: Boolean,
            val mnemonic_image_0: String, val mnemonic_text: String,
            val examples: List<Example>) {}