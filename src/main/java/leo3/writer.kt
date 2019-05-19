package leo3

import leo.binary.*
import leo32.base.Branch
import leo32.base.Leaf
import leo32.base.Link

data class Writer(var writeFn: Writer.(Bit) -> Writer)

fun writer(write: Writer.(Bit) -> Writer) = Writer(write)

typealias WriteValueFn<V> = Writer.(V) -> Writer

fun Writer.write(bit: Bit) =
	writeFn(bit)

fun <V> Writer.write(leaf: Leaf<V>, writeValueFn: WriteValueFn<V>) =
	writeValueFn(leaf.value)

fun <V> Writer.write(link: Link<V>, writeValueFn: WriteValueFn<V>) =
	writeFn(link.bit).writeValueFn(link.value)

fun <V> Writer.write(branch: Branch<V>, writeValueFn: WriteValueFn<V>) =
	writeValueFn(branch.at0).writeValueFn(branch.at1)

fun Writer.write(byte: Byte) =
	this
		.writeFn(byte.bit7)
		.writeFn(byte.bit6)
		.writeFn(byte.bit5)
		.writeFn(byte.bit4)
		.writeFn(byte.bit3)
		.writeFn(byte.bit2)
		.writeFn(byte.bit1)
		.writeFn(byte.bit0)
