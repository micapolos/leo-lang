package leo15.type.core

import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.reverse
import leo15.core.Leo
import leo15.core.List
import leo15.core.unsafeSeq

fun <T : Leo<T>> T.assertGives(t: T) =
	eval.assertEqualTo(t.eval)

inline fun <reified T : Leo<T>> List<T>.assertContains(vararg items: T) =
	eval.unsafeSeq.reverse.assertContains(*items.map { it.eval }.toTypedArray())