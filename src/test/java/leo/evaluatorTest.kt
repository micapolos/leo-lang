package leo

import leo.*
import leo.base.assertEqualTo
import leo.base.stack
import kotlin.test.Test

class EvaluatorTest {
  @Test
  fun evaluator() {
    evaluator
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = identityFunction,
                        scriptOrNull = null
                    )
                ),
                wordOrNull = null
            )
        )
  }

  @Test
  fun push_one() {
    evaluator
        .push(oneWord)
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = identityFunction,
                        scriptOrNull = null
                    )
                ),
                wordOrNull = oneWord
            )
        )
  }

  @Test
  fun push_one_() {
    evaluator
        .push(oneWord)
        ?.begin
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = identityFunction,
                        scriptOrNull = null
                    ),
                    Scope(
                        parentWord = oneWord,
                        function = identityFunction,
                        scriptOrNull = null
                    )
                ),
                wordOrNull = null
            )
        )
  }

  @Test
  fun push_one_number() {
    evaluator
        .push(oneWord)
        ?.begin
        ?.push(numberWord)
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = identityFunction,
                        scriptOrNull = null
                    ),
                    Scope(
                        parentWord = oneWord,
                        function = identityFunction,
                        scriptOrNull = null
                    )
                ),
                wordOrNull = numberWord
            )
        )
  }

  @Test
  fun push_one_number__() {
    evaluator
        .push(oneWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = identityFunction,
                        scriptOrNull = script(term(oneWord fieldTo term(numberWord)))
                    )
                ),
                wordOrNull = null
            )
        )
  }

  @Test
  fun push_one_number__two() {
    evaluator
        .push(oneWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        ?.push(twoWord)
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = identityFunction,
                        scriptOrNull = script(term(oneWord fieldTo term(numberWord)))
                    )
                ),
                wordOrNull = twoWord
            )
        )
  }

  @Test
  fun push_one_number__two_() {
    evaluator
        .push(oneWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        ?.push(twoWord)
        ?.begin
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = identityFunction,
                        scriptOrNull = script(term(oneWord fieldTo term(numberWord)))
                    ),
                    Scope(
                        parentWord = twoWord,
                        function = identityFunction,
                        scriptOrNull = null
                    )
                ),
                wordOrNull = null
            )
        )
  }

  @Test
  fun push_one_number__two_string() {
    evaluator
        .push(oneWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        ?.push(twoWord)
        ?.begin
        ?.push(stringWord)
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = identityFunction,
                        scriptOrNull = script(term(oneWord fieldTo term(numberWord)))
                    ),
                    Scope(
                        parentWord = twoWord,
                        function = identityFunction,
                        scriptOrNull = null
                    )
                ),
                wordOrNull = stringWord
            )
        )
  }


  @Test
  fun push_one_number__two_string__() {
    evaluator
        .push(oneWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        ?.push(twoWord)
        ?.begin
        ?.push(stringWord)
        ?.end
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = identityFunction,
                        scriptOrNull = script(
                            term(
                                oneWord fieldTo term(numberWord),
                                twoWord fieldTo term(stringWord)
                            )
                        )
                    )
                ),
                wordOrNull = null
            )
        )
  }

  @Test
  fun push_define_it_one__is_number___() {
    evaluator
        .push(defineWord)
        ?.begin
        ?.push(itWord)
        ?.begin
        ?.push(oneWord)
        ?.end
        ?.push(isWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        ?.end
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = Function(
                            stack(
                                pattern(term(oneWord)) returns
                                    template(term(numberWord))
                            )
                        ),
                        scriptOrNull = null
                    )
                ),
                wordOrNull = null
            )
        )
  }

  @Test
  fun push_define_it_one__is_number___it() {
    evaluator
        .push(defineWord)
        ?.begin
        ?.push(itWord)
        ?.begin
        ?.push(oneWord)
        ?.end
        ?.push(isWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        ?.end
        ?.push(itWord)
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = Function(
                            stack(
                                pattern(term(oneWord)) returns
                                    template(term(numberWord))
                            )
                        ),
                        scriptOrNull = null
                    )
                ),
                wordOrNull = itWord
            )
        )
  }

  @Test
  fun push_define_it_one__is_number___it_() {
    evaluator
        .push(defineWord)
        ?.begin
        ?.push(itWord)
        ?.begin
        ?.push(oneWord)
        ?.end
        ?.push(isWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        ?.end
        ?.push(itWord)
        ?.begin
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = Function(
                            stack(
                                pattern(term(oneWord)) returns
                                    template(term(numberWord))
                            )
                        ),
                        scriptOrNull = null
                    ),
                    Scope(
                        parentWord = itWord,
                        function = Function(
                            stack(
                                pattern(term(oneWord)) returns
                                    template(term(numberWord))
                            )
                        ),
                        scriptOrNull = null
                    )
                ),
                wordOrNull = null
            )
        )
  }

  @Test
  fun push_define_it_one__is_number___it_one() {
    evaluator
        .push(defineWord)
        ?.begin
        ?.push(itWord)
        ?.begin
        ?.push(oneWord)
        ?.end
        ?.push(isWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        ?.end
        ?.push(itWord)
        ?.begin
        ?.push(oneWord)
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = Function(
                            stack(
                                pattern(term(oneWord)) returns
                                    template(term(numberWord))
                            )
                        ),
                        scriptOrNull = null
                    ),
                    Scope(
                        parentWord = itWord,
                        function = Function(
                            stack(
                                pattern(term(oneWord)) returns
                                    template(term(numberWord))
                            )
                        ),
                        scriptOrNull = null
                    )
                ),
                wordOrNull = oneWord
            )
        )
  }

  @Test
  fun push_define_it_one__is_number___it_one__() {
    evaluator
        .push(defineWord)
        ?.begin
        ?.push(itWord)
        ?.begin
        ?.push(oneWord)
        ?.end
        ?.push(isWord)
        ?.begin
        ?.push(numberWord)
        ?.end
        ?.end
        ?.push(itWord)
        ?.begin
        ?.push(oneWord)
        ?.end
        .assertEqualTo(
            Evaluator(
                scopeStack = stack(
                    Scope(
                        parentWord = evaluateWord,
                        function = Function(
                            stack(
                                pattern(term(oneWord)) returns
                                    template(term(numberWord))
                            )
                        ),
                        scriptOrNull = script(term(itWord fieldTo term(numberWord)))
                    )
                ),
                wordOrNull = null
            )
        )
  }
}