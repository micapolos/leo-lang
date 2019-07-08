package leo8

sealed class Get

data class NameGet(val name: Name) : Get()
data class IndexGet(val index: Index) : Get()

fun get(name: Name): Get = NameGet(name)
fun get(index: Index): Get = IndexGet(index)
