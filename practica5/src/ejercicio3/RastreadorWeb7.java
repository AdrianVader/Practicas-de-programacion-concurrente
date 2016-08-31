package ejercicio3;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

import java.util.HashSet;


public class RastreadorWeb7 implements ProcesadorEnlaces {

    private final Collection<String> enlacesVisitados = Collections.synchronizedSet(new HashSet<String>());
    private String url;
    private ForkJoinPool execService; // Cambiamos el tipo ExecutorService por ForkJoinPool, para invocar sus métodos 

    public RastreadorWeb7(String inicioURL, int maxHilos) {
        this.url = inicioURL;
        execService = new ForkJoinPool(); // Creamos un ForkJoinPool de Java 7
    }

    @Override
    public void encolarEnlace(String link) throws Exception { // Ya no usaremos esta función
		//execService.invoke(new BuscadorEnlacesAction(link, this));
    }

    @Override
    public int cantidad() {
        return enlacesVisitados.size();
    }

    @Override
    public void anadirVisitado(String s) {
        enlacesVisitados.add(s);
    }

    @Override
    public boolean visitado(String s) {
        return enlacesVisitados.contains(s);
    }

    private void empezarRastreo() throws Exception {
		execService.invoke(new BuscadorEnlacesAction(this.url, this)); // Utilizamos la función invoke con un nuevo objeto BuscadorEnlacesAction (Runnable)
    }

    /**
     * @param args los argumentos de la lÃ­nea de comandos (deberÃ­a pasar la URL)
     */
    public static void main(String[] args) throws Exception {
        new RastreadorWeb7("https://www.google.es/", 16).empezarRastreo(); // Comenzamos el rastreo con empezarRastreo y la web inicial proprcionada en la constructora
    }
}
