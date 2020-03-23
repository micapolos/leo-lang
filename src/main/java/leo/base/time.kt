package leo.base

fun timeMillis(fn: () -> Unit): Long {
	val startTime = System.currentTimeMillis()
	fn()
	val endTime = System.currentTimeMillis()
	return endTime - startTime
}

fun printTime(fn: () -> Unit) {
	println("${timeMillis(fn)}ms")
}
