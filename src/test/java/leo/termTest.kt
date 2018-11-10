package leo

import leo.base.*
import kotlin.test.Test

val personTerm =
    term(
        personWord fieldTo term(
            nameWord fieldTo term(
                firstWord fieldTo term("Michał"),
                lastWord fieldTo term("Pociecha-Łoś")
            ),
            ageWord fieldTo term("42")
        )
    )

class TermTest {
  @Test
  fun string() {
    personTerm
        .string
        .assertEqualTo("person(name(first \"Michał\", last \"Pociecha-Łoś\"), age \"42\")")
  }

  @Test
  fun reflect() {
    personTerm
        .reflect { term(stringWord) }
        .string
        .assertEqualTo("term structure " +
            "field(key word person, value term structure(" +
            "field(key word name, value term structure(" +
            "field(key word first, value term meta string), " +
            "field(key word last, value term meta string))), " +
            "field(key word age, value term meta string)))")
  }

  val termForGet = term(
      oneWord fieldTo term(1),
      ageWord fieldTo term(42),
      ageWord fieldTo term(43),
      twoWord fieldTo term(2)
  )

  @Test
  fun only() {
    termForGet.only(oneWord).assertEqualTo(term(1))
    termForGet.only(twoWord).assertEqualTo(term(2))
    termForGet.only(ageWord).assertEqualTo(null)
    termForGet.only(nameWord).assertEqualTo(null)
  }

  @Test
  fun all() {
    termForGet.all(oneWord).assertEqualTo(stack(term(1)))
    termForGet.all(twoWord).assertEqualTo(stack(term(2)))
    termForGet.all(ageWord).assertEqualTo(stack(term(42), term(43)))
    termForGet.all(nameWord).assertEqualTo(null)
  }

  @Test
  fun nullPushIdentifier() {
    (null as Term<Nothing>?)
        .push(oneWord)
        .assertEqualTo(term(oneWord))
  }

  @Test
  fun nativePushIdentifier() {
    term(1)
        .push(oneWord)
        .assertEqualTo(null)
  }

  @Test
  fun idPushId() {
    term<Nothing>(oneWord)
        .push(twoWord)
        .assertEqualTo(null)
  }

  @Test
  fun listPushWord() {
    term(oneWord fieldTo term(1))
        .push(twoWord)
        .assertEqualTo(null)
  }

  @Test
  fun nullPushField() {
    (null as Term<Nothing>?)
        .push(oneWord fieldTo term(numberWord))
        .assertEqualTo(term(oneWord fieldTo term(numberWord)))
  }

  @Test
  fun nativePushField() {
    term(1)
        .push(twoWord fieldTo term(2))
        .assertEqualTo(null)
  }

  @Test
  fun wordPushField() {
    term<Nothing>(oneWord)
        .push(twoWord fieldTo term(2))
        .assertEqualTo(null)
  }

  @Test
  fun listPushField() {
    term(oneWord fieldTo term(1))
        .push(twoWord fieldTo term(2))
        .assertEqualTo(
            term(
                oneWord fieldTo term(1),
                twoWord fieldTo term(2)
            )
        )
  }

  @Test
  fun foldTokens() {
    term(
        personWord fieldTo term(
            nameWord fieldTo term(stringWord),
            ageWord fieldTo term(12)
        )
    )
        .foldTokens(nullStack<Token<Int>>()) { stackOrNull, token ->
          stackOrNull.push(token)
        }
        .assertEqualTo(
            stack(
                token(personWord),
                beginToken(),
                token(nameWord),
                beginToken(),
                token(stringWord),
                endToken(),
                token(ageWord),
                beginToken(),
                token(12),
                endToken(),
                endToken()
            ))
  }
}