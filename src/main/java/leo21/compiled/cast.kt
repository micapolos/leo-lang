package leo21.compiled

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.fold
import leo13.reverse
import leo21.type.ArrowLine
import leo21.type.Choice
import leo21.type.ChoiceType
import leo21.type.DoubleLine
import leo21.type.Field
import leo21.type.FieldLine
import leo21.type.Line
import leo21.type.RecurseType
import leo21.type.RecursiveType
import leo21.type.StringLine
import leo21.type.Struct
import leo21.type.StructType
import leo21.type.Type
import leo21.type.line
import leo21.type.linkOrNull
import leo21.type.struct
import leo21.type.type

data class Cast<out T : Any>(val t: T, val isIdentity: Boolean)

val <T : Any> T.identityCast get() = Cast(this, isIdentity = true)
val <T : Any> T.nonIdentityCast get() = Cast(this, isIdentity = false)

fun Compiled.castOrNull(rhs: Type): Cast<Compiled>? =
	switch(
		{ it.castOrNull(rhs) },
		{ it.castOrNull(rhs) })

fun StructCompiled.castOrNull(rhs: Type): Cast<Compiled>? =
	when (rhs) {
		is StructType -> castOrNull(rhs.struct)?.let { cast ->
			if (cast.isIdentity) compiled(this).identityCast
			else compiled(cast.t).nonIdentityCast
		}
		is ChoiceType -> onlyLineOrNull?.castOrNull(rhs.choice)
		is RecursiveType -> null
		is RecurseType -> null
	}

fun ChoiceCompiled.castOrNull(rhs: Type): Cast<Compiled>? =
	notNullIf(rhs is ChoiceType && choice == rhs.choice) {
		compiled(this).identityCast
	}

fun StructCompiled.castOrNull(rhs: Struct): Cast<StructCompiled>? =
	linkOrNull.let { compiledLinkOrNull ->
		rhs.linkOrNull.let { structLinkOrNull ->
			if (compiledLinkOrNull == null)
				if (structLinkOrNull == null) identityCast
				else null
			else
				if (structLinkOrNull == null) null
				else compiledLinkOrNull.lineCompiled.castOrNull(structLinkOrNull.line)?.let { lineCast ->
					compiledLinkOrNull.structCompiled.castOrNull(structLinkOrNull.struct)?.let { structCast ->
						if (lineCast.isIdentity && structCast.isIdentity) identityCast
						else structCast.t.plus(lineCast.t).nonIdentityCast
					}
				}
		}
	}

fun LineCompiled.castOrNull(choice: Choice): Cast<Compiled>? =
	emptyChoiceTyped.fold(choice.lineStack.reverse) { choiceLine ->
		if (line == choiceLine) plusChosen(this@castOrNull)
		else plusNotChosen(choiceLine)
	}.let { choiceCompiled ->
		choiceCompiled.termOrNull?.of(type(choiceCompiled.choice))?.nonIdentityCast
	}

fun LineCompiled.castOrNull(rhs: Line): Cast<LineCompiled>? =
	switch(
		{ stringCompiled ->
			notNullIf(rhs is StringLine) { identityCast }
		},
		{ doubleCompiled ->
			notNullIf(rhs is DoubleLine) { identityCast }
		},
		{ fieldCompiled ->
			if (rhs is FieldLine) fieldCompiled.castOrNull(rhs.field)?.let { fieldCast ->
				if (fieldCast.isIdentity) identityCast
				else fieldCast.t.lineCompiled.nonIdentityCast
			}
			else null
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