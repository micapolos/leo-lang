package leo32.treo

import leo.base.Seq
import leo.base.orIfNull
import leo.base.seq
import leo.base.then

data class Back(
	val nextOrNull: Back?)

val Back?.back get() = Back(this)
val back = null.back

val Back.charSeq: Seq<Char>
	get() =
		Seq { '<' then (nextOrNull?.charSeq.orIfNull { seq() }) }