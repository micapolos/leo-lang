package leo

import leo.base.*

data class Evaluator(
	val scopeStack: Stack<Scope>,
	val wordOrNull: Word?,
	val readerScript: Script) {
  override fun toString() = reflect.string
}

val leoReaderCoreField =
	leoWord fieldTo term(readerWord fieldTo term<Nothing>(coreWord))

val leoReaderCoreScript =
	script(term(leoReaderCoreField))

val Stack<Scope>.evaluator
  get() =
	  Evaluator(this, null, leoReaderCoreScript)

val Word.evaluator
  get() =
    scope.stack.evaluator

val evaluator =
    evaluateWord.evaluator

val Evaluator.evaluatedScript: Script?
  get() =
    if (scopeStack.pop != null) null
    else scopeStack.top.let { scope ->
      if (wordOrNull == null) scope.scriptOrNull
      else scope.scriptOrNull.push(wordOrNull)
    }

fun Evaluator.push(string: String): Evaluator? =
	string.toByteArray().fold(orNull) { evaluatorOrNull, byte ->
		evaluatorOrNull?.push(byte)
	}

fun Evaluator.push(byteArray: ByteArray): Evaluator? =
    byteArray.fold(this.orNull) { evaluatorOrNull, byte ->
      evaluatorOrNull?.push(byte)
    }

fun Evaluator.push(byte: Byte): Evaluator? =
	readerPush(byte)

fun Evaluator.corePush(byte: Byte): Evaluator? =
    when (byte) {
      '('.toByte() -> begin
      ')'.toByte() -> end
      else -> byte.letterOrNull?.let(this::push)
    }

fun Evaluator.readerPush(byte: Byte): Evaluator? =
	scopeStack
		.top
		.function
		.invoke(readerScript.push(readWord fieldTo term(byte.reflect))!!)
		.let { result ->
			result
				.term
				.structureTermOrNull
				?.fieldStack
				?.reverse
				?.foldTop { topField ->
					if (topField != leoReaderCoreField) null
					else this
				}
				?.andPop { evaluator, followingField ->
					if (followingField.key != readWord) null
					else followingField.value.match(byteWord) { byteValue ->
						(byteWord fieldTo byteValue).parseByte?.let { byte ->
							copy(readerScript = leoReaderCoreScript).corePush(byte)
						}
					}
				}
				?: copy(readerScript = result)
		}

fun Evaluator.push(letter: Letter): Evaluator =
    copy(wordOrNull = wordOrNull.plus(letter))

fun Evaluator.push(word: Word): Evaluator? =
    if (wordOrNull != null) null
    else copy(wordOrNull = word)

val Evaluator.begin: Evaluator?
  get() =
    if (wordOrNull == null) null
    else copy(wordOrNull = null)
        .scopeStack
        .push(scopeStack.top.beginChild(wordOrNull))
        .evaluator

val Evaluator.end: Evaluator?
  get() =
    if (wordOrNull != null)
      scopeStack.updateTopOrNull { topScope ->
        topScope.push(wordOrNull)
      }?.let { updatedScopeStack ->
        updatedScopeStack.pop?.let { poppedScopeStack ->
          poppedScopeStack.updateTopOrNull { scope ->
            scope.push(updatedScopeStack.top.parentWord fieldTo updatedScopeStack.top.scriptOrNull?.term!!)
          }
        }
      }?.evaluator
    else scopeStack
        .pop
        ?.let { poppedScopeStack ->
          if (scopeStack.top.scriptOrNull == null)
            poppedScopeStack.updateTopOrNull { scope ->
              scope.push(scopeStack.top.parentWord)
            }
          else
            poppedScopeStack.updateTopOrNull { scope ->
              scope.push(scopeStack.top.parentWord fieldTo scopeStack.top.scriptOrNull.term)
            }
        }
        ?.evaluator

fun Evaluator.push(token: Token<Nothing>): Evaluator? =
    when (token) {
      is Token.Meta -> null
      is Token.Identifier -> push(token.word)
      is Token.Begin -> begin
      is Token.End -> end
    }

fun Evaluator.push(script: Script): Evaluator =
    script.term.foldTokens(orNull) { evaluatorOrNull, token ->
      evaluatorOrNull?.push(token)
    }!!

val Script.evaluate: Script?
  get() =
    evaluator.push(this).evaluatedScript

// === reflect ===

val Evaluator.reflect: Field<Nothing>
  get() =
    evaluatorWord fieldTo term(
        scopeStack.reflect(scopeWord, Scope::reflect),
	    wordOrNull.orNullReflect(wordWord, Word::reflect),
	    readerWord fieldTo readerScript.term)

