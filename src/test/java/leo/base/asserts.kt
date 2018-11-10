package leo.base

import kotlin.test.assertEquals

fun <V> V.assertEqualTo(other: V) =
  assertEquals(other, this)
