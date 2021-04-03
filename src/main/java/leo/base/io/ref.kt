package leo.base.io

data class Ref<T>(private var unsafeValue: T) {
	val getIO: IO<T> get() = IO.unsafe { unsafeValue }
	fun setIO(value: T): IO<Unit> = IO.unsafe { unsafeValue = value }
}

fun <T> newRefIO(value: T): IO<Ref<T>> = IO.unsafe { Ref(value) }

fun main() {
	newRefIO(10).bind { ref ->
		io
			.then { ref.getIO.bind { it.printlnIO } }
			.then { ref.getIO.bind { value -> ref.setIO(value.inc()) } }
			.then { ref.getIO.bind { it.printlnIO } }
			.then { ref.getIO.bind { value -> ref.setIO(value.inc()) } }
			.then { ref.getIO.bind { it.printlnIO } }
	}.unsafeRun
}