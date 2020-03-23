package leo.base

inline fun <V, T> V.runWithSuperStack(crossinline fn: V.() -> T) =
	try {
		fn()
	} catch (soe: StackOverflowError) {
		var result: T? = null
		val thread = Thread {
			result = fn()
		}
		thread.start()
		thread.join()
		result!!
	}

fun main() {
	val size = 100000

	printTime {
		repeat(100) {
			0.explodeSuperStack(size)
		}
	}

	printTime {
		repeat(100) {
			0.explode(size)
		}
	}
}

fun Int.explode(max: Int): Int =
	if (this == max) max
	else inc().explode(max)

fun Int.explodeSuperStack(max: Int): Int =
	if (this == max) max
	else inc().runWithSuperStack { explodeSuperStack(max) }
