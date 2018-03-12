double radius = 1
double insertionDepthOfWii = 7.1
double wiiCenterOffset=2.2
double wiiConnectorThickness = 7.40
double wiiConnectorWidth = 12.21
double wiiInsetFromEdge = 12.42+(wiiConnectorWidth/2)
double boardX =67.35
double boardY =75.0
double boardZ =1.67
double cutoutRadius=4
double cutoutDepth = 7.3
double ioKaX =63.7
double ioKaY =48.79
double ioKaZ =17.67
double lowerKeepaway = 1.75
double holeCornerInset = 1.70+1.5
double boardConnects = 2.3
double usbThickness = 6.75
CSG wiiConnect = new RoundedCube(	wiiConnectorWidth,// X dimention
			wiiConnectorThickness,// Y dimention
			8.11+(radius)//  Z dimention
			).cornerRadius(radius)// sets the radius of the corner
			.toCSG()
			.toZMin()
			.movez(-radius)

CSG cutout = new RoundedCube(	5,// X dimention
			1.58+radius,// Y dimention
			8.11+(radius*2)//  Z dimention
			)
			.cornerRadius(radius)// sets the radius of the corner
			.toCSG()
			.toZMin()
			.movez(-radius)
			.toYMax()
			.movey(radius)
			.movey(wiiConnect.getMaxY())
CSG wiiBody = new Cube(	20.27,// X dimention
			13,// Y dimention
			23.4//  Z dimention
			)
			.toCSG()
			.toZMax()
CSG mainBoard = new Cube(boardX+boardConnects*2,boardY+boardConnects,boardZ).toCSG()
				.toXMin()
				.toYMin()
				.toZMax()
				.move(-boardConnects,-boardConnects,0)

CSG mainBoardCutout = new RoundedCube(33.0,cutoutDepth+cutoutRadius,boardZ+cutoutRadius*2)
				.cornerRadius(cutoutRadius)
				.toCSG()
				.toYMax()
				.toZMax()	
				.movez(cutoutRadius)
				.movey(mainBoard.getMaxY()+cutoutRadius)
				.movex(mainBoard.getMaxX()/2)
mainBoard=mainBoard.difference(mainBoardCutout)
wiiConnect=wiiConnect
		.difference(cutout)		
		.union(wiiBody)
		.toZMax()
		.movez(insertionDepthOfWii)
		.movey(wiiConnectorThickness/2-wiiCenterOffset)
		.rotx(-90)
		.rotz(180)
		.movex(wiiInsetFromEdge)
		.movez(-boardZ)

CSG IOkeepaway = new Cube(ioKaX,ioKaY,ioKaZ+boardZ+lowerKeepaway).toCSG()
				.toXMin()
				.toYMax()
				.toZMin()
				.movez(-(boardZ+lowerKeepaway))
				.movey(-cutoutDepth+boardY)
				.movex(1.8)
CSG switchkeepaway = new Cube(17,10.6,ioKaZ+boardZ+lowerKeepaway).toCSG()
				.toXMax()
				.toYMin()
				.toZMin()
				.movez(-(boardZ+lowerKeepaway))
				.movey(6.81)
				.movex(boardX-1.63)
CSG holeLower = new Cylinder(1.25,1.25,ioKaZ,(int)20).toCSG()
			.movez(-ioKaZ)
CSG holeUpper = new Cylinder(1.5,1.5,ioKaZ,(int)10).toCSG()
CSG holeHead = new Cylinder(3,3,ioKaZ,(int)20).toCSG()
			.movez(8)			
def hole = holeUpper.union([holeLower,holeHead])
mainBoard=mainBoard
	.union(hole.move(holeCornerInset,holeCornerInset,0))
	.union(hole.move(holeCornerInset,boardY-holeCornerInset,0))
	.union(hole.move(boardX-holeCornerInset,boardY-holeCornerInset,0))
	.union(hole.move(boardX-holeCornerInset,holeCornerInset,0))

CSG wire = new Cylinder(1,40).toCSG()
		.rotx(-90)
		.movez(5)
CSG powerkeepaway = new Cube(17.84,19.79,10.43+boardZ+lowerKeepaway).toCSG()
				.toXMax()
				.toYMin()
				.toZMin()
				.movez(-(boardZ+lowerKeepaway))
				.union(wire.movex(-2))
				.union(wire.movex(-6))
				.movex(53.21)
CSG usbCord = new Cylinder(2.0,40).toCSG()
				.rotx(90)
				.movez(usbThickness/2)
CSG usbCordkeepaway = new RoundedCube(7,1,usbThickness)
				.cornerRadius(1)
				.toCSG()
				.toYMax()
				.toZMin()
				.movey(27.75)
				
CSG usbkeepaway = new RoundedCube(11.05,14.3+2.41,usbThickness)
				.cornerRadius(1)
				.toCSG()
				.toYMin()
				.toZMin()
				.union(usbCordkeepaway)
				.hull()
				.union(usbCord)
				.movey(-cutoutDepth+boardY)
				.movex(boardX/2)
				.movez(11.06)

return [wiiConnect,mainBoard,IOkeepaway,switchkeepaway,powerkeepaway,usbkeepaway]