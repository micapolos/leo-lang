package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class WordTest {
  @Test
  fun string() {
    "foo".wordOrNull?.string.assertEqualTo("word foo")
    "".wordOrNull?.string.assertEqualTo(null)
    "int64".wordOrNull?.string.assertEqualTo(null)
    "Foo".wordOrNull?.string.assertEqualTo(null)
    "bąk".wordOrNull?.string.assertEqualTo(null)
    "foo()".wordOrNull?.string.assertEqualTo(null)
  }
}