package leo7

data class ScriptBegin(val script: Script, val begin: WordBegin)

fun Script.begin(word: Word) = ScriptBegin(this, word.begin)
