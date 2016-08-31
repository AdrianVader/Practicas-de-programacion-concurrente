package ejercicio5;




public class Utils {
	public static final String CODEBASE="java.rmi.server.codebase";
	public static void setCodeBase(Class<?> c){ //Una clase cualquiera
		//Obtenemos la ruta de la clase "c"
		String ruta = c.getProtectionDomain().getCodeSource().getLocation().toString();
		
		String path = System.getProperty(CODEBASE); //Comprobamos por si alguien ya seteo el Path
		//Si no esta seteado
		if(path!=null && !path.isEmpty()){
			ruta=path+" "+ruta;
		}
		//Hora si configuramos el sistema
		System.setProperty(CODEBASE, ruta);
	}
}