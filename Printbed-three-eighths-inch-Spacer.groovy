import com.neuronrobotics.bowlerstudio.vitamins.Vitamins

import eu.mihosoft.vrl.v3d.CSG

CSG vitamin_vexSpacer_threeEighth = Vitamins.get("vexSpacer", "threeEighth")

CSG printbed = vitamin_vexSpacer_threeEighth

String filename = "Printbed-three-eighths-inch-Spacer.stl";

def abs_path = Paths.get(filename)

FileUtil.write(abs_path,
		printbed.toStlString());
println "STL EXPORT to "+filename
println abs_path
return printbed;