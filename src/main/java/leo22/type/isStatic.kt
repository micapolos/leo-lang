package leo22.type

import leo14.lineSeq
import leo22.dsl.*

val X.typeIsStatic: Boolean
	get() =
		switch_(
			struct { it.structIsStatic },
			choice { false },
			recursive { false },
			recurse { false })

val X.structIsStatic
	get() =
		rhs_.lineSeq.all { it.lineIsStatic }

val X.lineIsStatic
	get() =
		switch_(
			literal { it.literalIsStatic },
			field { it.fieldIsStatic }
		)

val X.fieldIsStatic
	get() =
		rhs.type.typeIsStatic

val X.literalIsStatic
	get() =
		switch_(
			text { false },
			number { false })
