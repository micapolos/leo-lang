package leo

import leo.*
import leo.base.assertEqualTo
import leo.base.nullInstance
import leo.base.string
import kotlin.test.Test

class FieldTest {
  @Test
  fun string() {
    numberWord.fieldTo(term(1))
        .string
        .assertEqualTo("number 1")
  }

  @Test
  fun orNullReflect_null() {
    Letter::class.nullInstance
        .orNullReflect(letterWord, Letter::reflect)
        .string
        .assertEqualTo("letter optional null")
  }

  @Test
  fun orNullReflect_notNull() {
    Letter.A
        .orNullReflect(letterWord, Letter::reflect)
        .string
        .assertEqualTo("letter optional letter a")
  }
}