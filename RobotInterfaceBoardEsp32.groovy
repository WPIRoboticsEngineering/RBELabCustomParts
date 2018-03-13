class BoardMaker{
	static double radius = 1
	static double insertionDepthOfWii = 7.1
	static double wiiCenterOffset=2.2
	static double wiiConnectorThickness = 7.40
	static double wiiConnectorWidth = 12.21
	static double wiiInsetFromEdge = 12.42+(wiiConnectorWidth/2)
	static double boardX =67.35
	static double boardY =75.0
	static double boardZ =1.67
	static double cutoutRadius=4
	static double cutoutDepth = 7.3
	static double ioKaX =63.7
	static double ioKaY =48.79
	static double ioKaZ =17.67
	static double lowerKeepaway = 1.75
	static double holeCornerInset = 1.70+1.5
	static double boardConnects = 2.3
	static double usbThickness = 6.75
	static double wireHeight = 5
	static double wireRadius = 1.5
	static double negativeWireOffset = 2
	static double positiveWireOffset = 6
	static double caseOutSet = 4
	static double powerKeepawayOffset=53.21
	static double usbHeight=11.06
	
	def makeWiiConnector(){
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
		CSG wiiClip = new Cube(	17.9,// X dimention
					4.62,// Y dimention
					5//  Z dimention
					)
					.toCSG()
					.toZMax()	
					.toYMax()
					.movey(wiiBody.getMinY())		
		wiiConnect=wiiConnect
				.difference(cutout)		
				.union([wiiBody,wiiClip])
				
				
		return wiiConnect	
	}
	def makeBoard(){
		CSG wiiConnect = makeWiiConnector()	
		wiiConnect=wiiConnect
				.toZMax()
				.movez(insertionDepthOfWii)
				.movey(wiiConnectorThickness/2-wiiCenterOffset)		
				.rotx(-90)
				.rotz(180)
				.movex(wiiInsetFromEdge)
				.movez(-boardZ)
							
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
		
		CSG antenna = new Cube(21.85,7,13.69+boardZ+lowerKeepaway).toCSG()
					.toYMax()
					.toZMin()
					.movey(-ioKaY)
					.movex(ioKaX/2)
		CSG IOkeepaway = new Cube(ioKaX,ioKaY,ioKaZ+boardZ+lowerKeepaway).toCSG()
						.toXMin()
						.toYMax()
						.toZMin()
						.union(antenna)
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
		CSG holeHead = new Cylinder(3.1,3.1,ioKaZ,(int)20).toCSG()
					.movez(7)			
		def hole = holeUpper.union([holeLower,holeHead])
		mainBoard=mainBoard
			.union(hole.move(holeCornerInset,holeCornerInset,0))
			.union(hole.move(holeCornerInset,boardY-holeCornerInset,0))
			.union(hole.move(boardX-holeCornerInset,boardY-holeCornerInset,0))
			.union(hole.move(boardX-holeCornerInset,holeCornerInset,0))
		
		CSG wire = new Cylinder(wireRadius,40).toCSG()
				.movez(-wireRadius)
				.rotx(-90)
				.movez(wireHeight)
		CSG wirekeepaway = new Cube(positiveWireOffset+wireRadius*4,boardConnects,wireHeight).toCSG()
							.toXMax()
							.toZMin()
							.toYMax()
							.movex(wireRadius)
		CSG powerkeepaway = new Cube(17.84,19.79,10.43+boardZ+lowerKeepaway).toCSG()
						.toXMax()
						.toYMin()
						.toZMin()
						.movez(-(boardZ+lowerKeepaway))
						.union(wire.rotz(-20).movex(-negativeWireOffset))
						.union(wire.rotz(20).movex(-positiveWireOffset))
						.union(wirekeepaway)
						.movex(powerKeepawayOffset)
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
						.movez(usbHeight)
		
		return [wiiConnect,mainBoard,IOkeepaway,switchkeepaway,powerkeepaway,usbkeepaway]
	}
	def makeCase(){
		def board =makeBoard()
		double caseRounding = 1
		double frontCaseDepth = -cutoutDepth+boardY-ioKaY-caseRounding+3
		double lowerCaseDepth = Math.abs(board[0].getMinZ())
		
		CSG wirekeepaway = new RoundedCube(positiveWireOffset+wireRadius*8,caseOutSet-boardConnects,wireHeight+caseRounding*2)
							.cornerRadius(caseRounding)
							.toCSG()
							.toXMax()
							.toZMin()
							.toYMin()
							.movez(-caseRounding*2)
							.movex(wireRadius*3)	
							.movey(-caseOutSet)
							.movex(powerKeepawayOffset)	
		double LugX = boardX+(boardConnects*2)+caseOutSet*2
		CSG basicLug = new RoundedCube(LugX,frontCaseDepth,lowerCaseDepth)
						.cornerRadius(caseRounding)
						.toCSG()
						.toZMax()
						.toYMin()
						.movey(-caseOutSet)
						.toXMin()
						.movex(-caseOutSet-boardConnects)
		CSG frontBottom = basicLug	
						.union(	wirekeepaway)	
						.difference(board)	
		CSG fullBoard = CSG.unionAll(board)
		CSG frontTop = basicLug	
						.scalez(1.6)
						.toZMin()
						.movez(-caseRounding*2)
						.minkowskiDifference(fullBoard,0.4)
						.minkowskiDifference(frontBottom,0.4)
						.difference(frontBottom)
		double vexGrid = 1.0/2.0*25.4
		CSG vexMount = Vitamins.get( "vexFlatSheet","Aluminum 1x5")		
						.intersect(new Cube(vexGrid*6).toCSG())
						.rotz(-90)
						.movey(	-caseOutSet+caseRounding)	
						.movex(-caseOutSet+caseRounding-boardConnects)
						.movez(		frontBottom.getMinZ())	
		CSG vexMountB = vexMount.movex(vexGrid*7)
						.union(	vexMount)				
		CSG backVex = vexMountB
						.movey(vexGrid*4)
		double backeOfCaseY = boardY-	cutoutDepth
		CSG usbkeepaway = new RoundedCube(13+caseRounding*2,frontCaseDepth,usbHeight+caseRounding*2+usbThickness/2)
							.cornerRadius(caseRounding)
							.toCSG()
							.toZMin()
							.movez(-caseRounding*2)
							.toYMin()
							.movex(boardX/2)
		
		CSG backBottom = basicLug
						.toYMin()
						.union(usbkeepaway)
						.movey(backeOfCaseY)

						.difference(board)	
						.union(backVex)
		frontBottom=frontBottom.union(vexMountB)
		CSG backTop = basicLug	
						.scalez(2)
						.toZMin()
						.movez(-caseRounding*2)
						.toYMin()
						.movey(backeOfCaseY)
						.minkowskiDifference(fullBoard,0.4)	
						.minkowskiDifference(backBottom,0.4)	
						.difference(backBottom)	
						
		frontBottom.setManufacturing({ toMfg ->
			return toMfg
					.toXMin()
					.toYMin()
					.toZMin()
		})
		backBottom.setManufacturing({ toMfg ->
			return toMfg
					.toXMin()
					.toYMin()
					.toZMin()
		})		
		frontTop.setManufacturing({ toMfg ->
			return toMfg
					.toXMin()
					.toYMin()
					.roty(180)
					.toZMin()
		})		
		backTop.setManufacturing({ toMfg ->
			return toMfg
					.toXMin()
					.toYMin()
					.roty(180)
					.toZMin()
		})										
		def caseParts = [frontBottom,frontTop,backBottom,backTop]
		return caseParts
		board.addAll(caseParts)
		return board
	}
}
return new BoardMaker().makeCase()