double windowWidth = 75
double windowHeight = 100
double woodThickness = 5.35

def windowHole = new Cube(woodThickness,windowWidth,windowHeight).toCSG()
def wall = new Cube(woodThickness,windowWidth*3,windowHeight*3).toCSG()
		.difference(windowHole)
		.movez(windowHeight/2)
		.setColor(javafx.scene.paint.Color.CYAN);


return [wall]