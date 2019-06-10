package leo5.core

import leo.base.empty
import leo.base.failIfOr
import leo.base.intHsbMaskInt
import leo.binary.bit
import leo.binary.isZero

val Value.int get() = hsbMaskInt(intHsbMaskInt)
fun Value.hsbMaskInt(hsbMask: Int) = hsbMaskAccInt(hsbMask, 0)
fun Value.hsbMaskAccInt(hsbMask: Int, acc: Int): Int =
	if (hsbMask == 0) failIfOr(!isEmpty) { acc }
	else at0.bit.let { bit -> at1.hsbMaskAccInt(hsbMask.ushr(1), acc.or(if (bit.isZero) 0 else hsbMask)) }

fun value(int: Int) = hsbMaskValue(int, intHsbMaskInt)
fun hsbMaskValue(int: Int, hsbMask: Int): Value =
	if (hsbMask == 0) value(empty)
	else value(pair(value(int.and(hsbMask).bit), hsbMaskValue(int, hsbMask.ushr(1))))

