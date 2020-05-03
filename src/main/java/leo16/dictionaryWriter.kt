package leo16

data class DictionaryWriter(val dictionary: Dictionary, val pattern: Pattern)

fun Dictionary.writer(pattern: Pattern) = DictionaryWriter(this, pattern)