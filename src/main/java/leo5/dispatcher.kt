package leo5

data class Dispatcher(val map: Map<String, Function>)

fun dispatcher(vararg pairs: Pair<String, Function>) = Dispatcher(mapOf(*pairs))
fun Dispatcher.at(name: String) = map.getValue(name)
