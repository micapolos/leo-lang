package leo.base

data class Writer<V, R>(
	val written: R,
	val writeFn: R.(V) -> R)

fun <V, R> R.writer(writeFn: R.(V) -> R): Writer<V, R> =
	Writer(this, writeFn)

fun <V> nullWriter(): Writer<V, Unit> =
	Unit.writer { Unit }

fun <V> printlnWriter(): Writer<V, Unit> =
	Unit.writer { println(this); Unit }

fun <V, R> Writer<V, R>.write(value: V): Writer<V, R> =
	Writer(writeFn.invoke(this.written, value), writeFn)

fun <V, R> Writer<V, R>.write(stream: Stream<V>) =
	fold(stream, Writer<V, R>::write)

fun <A, B, R> Writer<B, R>.map(fn: (A) -> B): Writer<A, R> =
	written.writer { a ->
		writeFn.invoke(this, fn(a))
	}

val <V, R> Writer<V, R>.join: Writer<Stream<V>, R>
	get() =
		written.writer { stream ->
			stream
				.foldFirst { value -> writeFn.invoke(this, value) }
				.foldNext(writeFn)
		}

val <R> Writer<Bit, R>.bitByteWriter: Writer<Byte, R>
	get() =
		join.map(Byte::bitStream)

//val <R> Writer<Byte, R>.byteBitWriter: Writer<Bit, R>
//	get() =

val <V> Stack<V>?.writer: Writer<V, Stack<V>?>
	get() =
		Writer(this, Stack<V>?::push)

fun <V> writerStackOrNull(fn: Writer<V, Stack<V>?>.() -> Writer<V, Stack<V>?>): Stack<V>? =
	fn(nullStack<V>().writer).written