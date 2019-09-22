package leo.java.io

import leo.base.Seq
import leo.base.notNullIf
import leo.base.then
import java.io.Reader

val Reader.charSeq: Seq<Char>
	get() =
		Seq {
			read().let { int ->
				notNullIf(int != -1) {
					int.toChar().then(charSeq)
				}
			}
		}
