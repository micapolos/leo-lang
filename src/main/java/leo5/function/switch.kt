package leo5.function

data class Switch(val selector: Any, val fnList: List<(Any) -> Any>)

fun switch(selector: Any, vararg fns: (Any) -> Any) = Switch(selector, fns.toList())

@Suppress("UNCHECKED_CAST")
val Switch.invoke
	get() = (selector as IndexedValue<Any>).let { indexedValue ->
		fnList[indexedValue.index].invoke(indexedValue.value)
	}
