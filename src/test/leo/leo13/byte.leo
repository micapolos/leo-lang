define: byte has
	first: bit
	second: bit
	third: bit
	fourth: bit
	fifth: bit
	sixth: bit
	seventh: bit
	eight: bit

define: zero byte gives: byte
	first: bit: zero
	second: bit: zero
	third: bit: zero
	fourth: bit: zero
	fifth: bit: zero
	sixth: bit: zero
	seventh: bit: zero
	eight: bit: zero

define: byte inverse gives: byte
	first: given byte first bit negate
	second: given byte second bit negate
	third: given byte third bit negate
	fourth: given byte fourth bit negate
	fifth: given byte fifth bit negate
	sixth: given byte sixth bit negate
	seventh: given byte seventh bit negate
	eight: given byte eight bit negate
