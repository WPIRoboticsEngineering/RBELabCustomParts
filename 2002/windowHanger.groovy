double windowWidth = 74
double windowHeight = 100
double woodThickness = 5.5
double boxDepth = 50
double hookWidth  = 15
double lipDepth = 30
double boxThickness =5
def windowHole = new Cube(woodThickness,windowWidth,windowHeight).toCSG()
def wall = new Cube(woodThickness,windowWidth*3,windowHeight*3).toCSG()
		.difference(windowHole)
		.movez(windowHeight/2)
		.setColor(javafx.scene.paint.Color.WHITE);

def hookCore = 	new Cube(hookWidth,windowWidth,hookWidth).toCSG()	
def boxVoid = new Cube(boxDepth,windowWidth-boxThickness*2,windowHeight+lipDepth).toCSG()
				.toXMax()
				.toZMin()
				.movez(-lipDepth)
				.difference(hookCore)
def hook = hookCore
			.difference(wall)
			.setColor(javafx.scene.paint.Color.BLUE);
def chord = new Cube(200,hookWidth,hookWidth).toCSG()
				.toZMin()
				.movez(-lipDepth)
			
def boxStarting = new Cube(boxDepth+boxThickness,windowWidth,windowHeight+lipDepth+boxThickness*2).toCSG()
				.toXMax()
				.toZMin()
				.movez(-lipDepth-boxThickness)
				.difference(boxVoid	)
				.difference(wall)
				.difference(chord)
				.difference(wall.movez(-hookWidth*2/3))
				.union(hook)	
boxStarting.setName("RBE2002-window-box")
boxStarting.setManufacturing({ toMfg ->
	return toMfg
			.roty(90)
			.toZMin()//move it down to the flat surface
})	


return [wall,boxStarting]