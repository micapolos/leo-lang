package leo5

data class Dispatcher(val map: Map<String, Body>)

fun dispatcher(vararg pairs: Pair<String, Body>) = Dispatcher(mapOf(*pairs))
fun Dispatcher.at(name: String) = map.getValue(name)
