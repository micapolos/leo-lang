package leo

import leo.base.bitStream
import leo.base.clampedByte

val newlineChar = '\n'
val newlineByte = newlineChar.clampedByte
val newlineBitStream = newlineByte.bitStream