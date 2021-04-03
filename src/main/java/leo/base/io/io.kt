package leo.base.io

import leo.base.println
import java.io.File

class IO<T> private constructor(private val unsafeValueFn: () -> T) {
	companion object {
		fun <T> unsafe(fn: () -> T) = IO(fn)
	}

	val unsafeValue: T get() = unsafeValueFn()
}


fun <T> io(value: T) = IO.unsafe { value }
val io: IO<Unit> = io(Unit)
fun <T, K> IO<T>.bind(fn: (T) -> IO<K>): IO<K> = fn(unsafeValue)
fun <T, K> IO<T>.then(fn: () -> IO<K>): IO<K> = bind { fn() }
fun <T, K> IO<T>.map(fn: (T) -> K): IO<K> = bind { io(fn(it)) }

val File.readStringIO: IO<String> get() = IO.unsafe { readText() }
fun File.writeIO(string: String): IO<Unit> = IO.unsafe { writeText(string) }
val Any?.printlnIO: IO<Unit> get() = IO.unsafe { println }

fun main() {
	io
		.then { File("/Users/micapolos/ui.ini").readStringIO }
		.bind { string ->
			io
				.then { "Writing...".printlnIO }
				.then { string.printlnIO }
				.then { File("/Users/micapolos/ui2.ini").writeIO(string) }
				.then { "Written...".printlnIO }
		}
		.unsafeValue
}