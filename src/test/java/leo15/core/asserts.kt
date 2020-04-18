package leo15.core

import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.reverse

fun <T : Leo<T>> T.assertGives(t: T) =
	eval.assertEqualTo(t.eval)

inline fun <reified T : Leo<T>> List<T>.assertContains(vararg items: T) =
	eval.unsafeSeq.reverse.assertContains(*items.map { it.eval }.toTypedArray())