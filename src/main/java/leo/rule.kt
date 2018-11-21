package leo

data class Rule(
	val patternTerm: Term<Pattern>,
	val body: Body) {
	//override fun toString() = reflect.string
}

fun rule(patternTerm: Term<Pattern>, body: Body) =
	Rule(patternTerm, body)

infix fun Term<Pattern>.returns(body: Body) =
	Rule(this, body)

fun Term<Nothing>.parseRule(localFunction: Function): Rule? =
	if (shortRuleSyntax) parseIs(localFunction)
	else parseDefineItIs(localFunction)

fun Term<Nothing>.parseDefineItIs(localFunction: Function): Rule? =
	match(defineWord) { defineTerm ->
		defineTerm?.match(itWord, isWord) { itTerm, isTerm ->
			itTerm?.parsePatternTerm?.let { patternTerm ->
				isTerm?.parseSelectorTerm(patternTerm)?.let { selectorTerm ->
					rule(patternTerm, body(selectorTerm, localFunction))
				}
			}
		}
	}

fun Term<Nothing>.parseIs(localFunction: Function): Rule? =
	structureTermOrNull?.let { structureTerm ->
		when {
			structureTerm.lhsTermOrNull == null -> null
			structureTerm.word != isWord -> null
			else -> structureTerm.lhsTermOrNull.parsePatternTerm.let { patternTerm ->
				rule(patternTerm, body(structureTerm.rhsTermOrNull?.parseSelectorTerm(patternTerm), localFunction))
			}
		}
	}

// === reflect

//val Rule.reflect: Field<Nothing>
//	get() =
//		ruleWord fieldTo term(
//			patternTerm.reflect(Pattern::reflect),
//			body.reflect)