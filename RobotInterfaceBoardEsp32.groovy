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
CSG mainBoard = new Cube(boardX,boardY,boardZ).toCSG()
				.toXMin()
				.toYMin()
				.toZMax()

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

return [wiiConnect,mainBoard,IOkeepaway]