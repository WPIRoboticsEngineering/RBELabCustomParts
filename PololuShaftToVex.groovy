CSG pshaft =new Cylinder(2,2,11,(int)30).toCSG() // a one line Cylinder
CSG flat = new Cube(	4,// X dimention
			3.4,// Y dimention
			11//  Z dimention
			).toCSG()
			.toYMin()
			.toZMin()
			.movey(pshaft.getMinY())

CSG xshaft =  new RoundedCube(	3.12,// X dimention
				3.12,// Y dimention
				11//  Z dimention
				)
				.cornerRadius(0.5)// sets the radius of the corner
				.toCSG()// converts it to a CSG tor display
				.toZMax()
				.movez(-1)
pshaft=pshaft.intersect(flat)

CSG coupler = new Cube(	7.5,// X dimention
			7.5,// Y dimention
			20//  Z dimention
			).toCSG()
			.difference([pshaft,xshaft])
			.rotx(90)
			.toZMin()
return [coupler]