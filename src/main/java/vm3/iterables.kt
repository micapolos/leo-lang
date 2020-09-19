package vm3

val <T> List<T>.deduplicate: List<T>
	get() =
		LinkedHashSet(this).toList()