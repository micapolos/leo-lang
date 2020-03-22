number increment
does
	given number
	plus: 1

number decrement
does
	given number
	minus: 1

vector
gives: vector
	x: number
	y: number

vector
plus: vector
does: vector
	x
		given vector x number
		plus: given plus vector x number
	y
		given vector y number
		plus: given plus vector y number

vector
	x: 10 increment
	y: 20 decrement
plus: vector
	x: 30
	y: 40
