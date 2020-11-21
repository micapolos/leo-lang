package leo21.compiled

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.fold
import leo13.reverse
import leo21.type.ArrowLine
import leo21.type.Choice
import leo21.type.ChoiceLine
import leo21.type.Field
import leo21.type.FieldLine
import leo21.type.Line
import leo21.type.NumberLine
import leo21.type.StringLine
import leo21.type.Type
import leo21.type.line
import leo21.type.linkOrNull
import leo21.type.type

data class Cast<out T : Any>(val t: T, val isIdentity: Boolean)

val <T : Any> T.identityCast get() = Cast(this, isIdentity = true)
val <T : Any> T.nonIdentityCast get() = Cast(this, isIdentity = false)

fun ChoiceCompiled.castOrNull(rhs: Line): Cast<LineCompiled>? =
	notNullIf(rhs is ChoiceLine && choice == rhs.choice) {
		line(this).identityCast
	}

fun Compiled.castOrNull(rhs: Type): Cast<Compiled>? =
	linkOrNull.let { compiledLinkOrNull ->
		rhs.linkOrNull.let { typeLinkOrNull ->
			if (compiledLinkOrNull == null)
				if (typeLinkOrNull == null) identityCast
				else null
			else
				if (typeLinkOrNull == null) null
				else compiledLinkOrNull.lineCompiled.castOrNull(typeLinkOrNull.line)?.let { lineCast ->
					compiledLinkOrNull.compiled.castOrNull(typeLinkOrNull.type)?.let { structCast ->
						if (lineCast.isIdentity && structCast.isIdentity) identityCast
						else structCast.t.plus(lineCast.t).nonIdentityCast
					}
				}
		}
	}

fun LineCompiled.castOrNull(choice: Choice): Cast<LineCompiled>? =
	emptyChoiceCompiled.fold(choice.lineStack.reverse) { choiceLine ->
		if (line == choiceLine) plusChosen(this@castOrNull)
		else plusNotChosen(choiceLine)
	}.let { choiceCompiled ->
		choiceCompiled.termOrNull?.of(line(choiceCompiled.choice))?.nonIdentityCast
	}

fun LineCompiled.castOrNull(rhs: Line): Cast<LineCompiled>? =
	if (rhs is ChoiceLine) castOrNull(rhs.choice)
	else switch(
		{ stringCompiled ->
			notNullIf(rhs is StringLine) { identityCast }
		},
		{ doubleCompiled ->
			notNullIf(rhs is NumberLine) { identityCast }
		},
		{ fieldCompiled ->
			if (rhs is FieldLine) fieldCompiled.castOrNull(rhs.field)?.let { fieldCast ->
				if (fieldCast.isIdentity) identityCast
				else fieldCast.t.lineCompiled.nonIdentityCast
			}
			else null
		},
		{ choiceCompiled ->
			notNullIf(rhs is ChoiceLine) { identityCast }
		},
		{ arrowCompiled ->
			notNullIf(rhs is ArrowLine && arrowCompiled.arrow == rhs.arrow) { identityCast }
		})

fun FieldCompiled.castOrNull(rhsField: Field): Cast<FieldCompiled>? =
	ifOrNull(field.name == rhsField.name) {
		rhsCompiled.castOrNull(rhsField.rhs)?.let { rhsCast ->
			if (rhsCast.isIdentity) identityCast
			else field.name.fieldTo(rhsCast.t).nonIdentityCast
		}
	}