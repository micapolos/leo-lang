package leo3

import leo.base.byte
import leo.binary.Bit
import leo32.base.*

data class Reader(
	val readBitFn: () -> Read<Bit>)

data class Read<out V>(
	val value: V,
	val reader: Reader)

typealias ReadFn<V> = Reader.() -> Read<V>

fun Reader.readBit(): Read<Bit> =
	readBitFn()

fun <V, R> Read<V>.map(fn: (V) -> R) =
	Read(fn(value), reader)

fun <V> read(value: V, reader: Reader) =
	Read(value, reader)

fun Reader.readByte() =
	readBit().let { read7 ->
		read7.reader.readBit().let { read6 ->
			read6.reader.readBit().let { read5 ->
				read5.reader.readBit().let { read4 ->
					read4.reader.readBit().let { read3 ->
						read3.reader.readBit().let { read2 ->
							read2.reader.readBit().let { read1 ->
								read1.reader.readBit().let { read0 ->
									read(
										byte(read7.value, read6.value, read5.value, read4.value, read3.value, read2.value, read1.value, read0.value),
										read0.reader)
								}
							}
						}
					}
				}
			}
		}
	}

fun <V> Reader.readLeaf(readValueFn: ReadFn<V>): Read<Leaf<V>> =
	readValueFn().map { leaf(it) }

fun <V> Reader.readLink(readValueFn: ReadFn<V>): Read<Link<V>> =
	readBit().let { readBit ->
		readBit.reader.readValueFn().let { readValue ->
			readValue.map {
				link(readBit.value, it)
			}
		}
	}

fun <V> Reader.readBranch(readValueFn: ReadFn<V>): Read<Branch<V>> =
	readValueFn().let { readAt0 ->
		readAt0.reader.readValueFn().let { readAt1 ->
			readAt1.map {
				branch(readAt0.value, readAt1.value)
			}
		}
	}