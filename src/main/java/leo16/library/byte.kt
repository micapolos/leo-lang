package leo16.library

import leo.base.clampedByte
import leo.base.print
import leo15.dsl.*
import leo16.compile_
import leo16.invoke
import leo16.names.*
import leo16.nativeField

fun main() {
	byte.value.print
}

val byte = compile_ {
	use { bit }
	use { number }
	use { int }
	use { reflection }

	byte.any
	is_ {
		byte {
			first { bit.any }
			second { bit.any }
			third { bit.any }
			fourth { bit.any }
			fifth { bit.any }
			sixth { bit.any }
			seventh { bit.any }
			eighth { bit.any }
		}
	}

	byte.any.read
	does {
		read.byte.first.bit.number.times { 128.number }
		plus { read.byte.second.bit.number.times { 64.number } }
		plus { read.byte.third.bit.number.times { 32.number } }
		plus { read.byte.fourth.bit.number.times { 16.number } }
		plus { read.byte.fifth.bit.number.times { 8.number } }
		plus { read.byte.sixth.bit.number.times { 4.number } }
		plus { read.byte.seventh.bit.number.times { 2.number } }
		plus { read.byte.eighth.bit.number }
		byte
	}

	test {
		byte {
			first { zero.bit }
			second { zero.bit }
			third { zero.bit }
			fourth { zero.bit }
			fifth { one.bit }
			sixth { one.bit }
			seventh { zero.bit }
			eighth { one.bit }
		}
		as_ { text }
		equals_ { _byte(13.clampedByte.nativeField).toString().text }
	}

	native.any.byte.reflect
	does { meta { byte { reflect.byte.native.int.number } } }

	test {
		byte {
			first { zero.bit }
			second { zero.bit }
			third { zero.bit }
			fourth { zero.bit }
			fifth { one.bit }
			sixth { one.bit }
			seventh { zero.bit }
			eighth { one.bit }
		}
		reflect
		equals_ { meta { byte { 13.number } } }
	}
}