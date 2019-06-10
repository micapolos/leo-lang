package leo5.core

import leo.binary.bit

fun hsbMaskArrayValue(hsbMask: Int, fn: (Int) -> Value): Value =
	hsbMaskArrayValue(hsbMask, 0, fn)

fun hsbMaskArrayValue(hsbMask: Int, offset: Int, fn: (Int) -> Value): Value =
	if (hsbMask == 0) fn(offset)
	else value(pair(
		hsbMaskArrayValue(hsbMask.ushr(1), offset, fn),
		hsbMaskArrayValue(hsbMask.ushr(1), offset.or(hsbMask), fn)))

fun Value.hsbMaskArrayAt(hsbMask: Int, index: Int): Value =
	if (hsbMask == 0) this
	else at(index.and(hsbMask).bit).hsbMaskArrayAt(hsbMask.ushr(1), index)
