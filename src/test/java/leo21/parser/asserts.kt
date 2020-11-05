package leo21.parser

import leo.base.assertEqualTo
import leo.base.assertNull

fun <V : Any> Parser<V>?.assertParsed(v: V) =
	this!!.parsedOrNull!!.assertEqualTo(v)

val Parser<*>?.assertNotParsed get() = this?.parsedOrNull.assertNull