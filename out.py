def testPrintLoop(x):
	while x > 0:
		print(x)
		x = x - 1
	
	
def testConcatLoop(y):
	x = len(y) - 1
	output = y
	while x > 0:
		output = output + y
		x = x - 1
	return output
	
def testIf(d, x):
	if d > 5.1:
		print("Hi")
	elif d > 0.1:
		print(x)
	else:
		print(3.2 * 4.0)
	
	
def testIfLoop(x):
	output = 1
	switch = True
	while x > 0:
		if switch:
			output = output * 2
			switch = False
		else:
			switch = True
		x = x - 1
	return output
	
def foo():
	print("ran foo")
	
	
def testFuncCallLoop(x):
	while x > 0:
		foo()
		x = x - 2
	
	
def main():
	testPrintLoop(5)
	str = "a1"
	print(testConcatLoop(str))
	testIf(1.1, 3)
	intI = testIfLoop(3)
	print(intI)
	testFuncCallLoop(5)
	
	
main()
