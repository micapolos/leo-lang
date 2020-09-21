package vm3

import kotlinx.collections.immutable.PersistentList
import leo.base.byte0
import leo.base.byte1
import leo.base.byte2
import leo.base.byte3
import leo.base.int

inline fun PersistentList<Byte>.addLittleEndian(int: Int) =
	this
		.add(int.byte0)
		.add(int.byte1)
		.add(int.byte2)
		.add(int.byte3)

inline fun PersistentList<Byte>.setLittleEndian(index: Int, int: Int) =
	this
		.set(index + 0, int.byte0)
		.set(index + 1, int.byte1)
		.set(index + 2, int.byte2)
		.set(index + 3, int.byte3)

inline fun PersistentList<Byte>.littleEndianInt(index: Int): Int =
	int(get(index + 3), get(index + 2), get(index + 1), get(index + 0))

inline fun PersistentList<Byte>.updateLittleEndian(index: Int, fn: (Int) -> Int) =
	setLittleEndian(index, fn(littleEndianInt(index)))