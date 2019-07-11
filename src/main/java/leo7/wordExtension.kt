package leo7

data class WordExtension(val word: Word, val letter: Letter)
fun Word.extensionWith(letter: Letter) = WordExtension(this, letter)
