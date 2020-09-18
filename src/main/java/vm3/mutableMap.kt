package vm3

fun <K, V> MutableMap<K, V>.get(key: K, fn: () -> V): V {
	val value = get(key)
	return if (value != null) value
	else {
		val value = fn()
		this[key] = value
		value
	}
}
