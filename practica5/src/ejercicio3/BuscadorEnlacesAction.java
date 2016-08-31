package ejercicio3;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

public class BuscadorEnlacesAction extends RecursiveAction implements Runnable {

	private static final long serialVersionUID = 1L;
	private String url;
    private ProcesadorEnlaces procesadorEnlaces;
    /**
     * Usado para las estadisticas
     */
    private static final long t0 = System.nanoTime();

    public BuscadorEnlacesAction(String url, ProcesadorEnlaces procesador) {
        this.url = url;
        this.procesadorEnlaces = procesador;
    }

    @Override
    public void run() {
        compute(); // Llamamos a la función compute, que se encargará de generar más enlaces
    }

	@Override
	protected void compute() {
		// si no lo hemos visitado ya
        if (!procesadorEnlaces.visitado(url)) {
            try {
                URL uriLink = new URL(url);
                Parser parser = new Parser(uriLink.openConnection());
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
                List<RecursiveAction> urls = new ArrayList<RecursiveAction>(); // Creamos una lista de RecursiveAction, para llamar a la función invokeAll con más objetos BuscadorEnlacesAction

                 for (int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);

                    if (!extracted.getLink().isEmpty() && !procesadorEnlaces.visitado(extracted.getLink())) {
                        urls.add(new BuscadorEnlacesAction(extracted.getLink(), this.procesadorEnlaces)); // Añadimos a la lista todas las nuevas direcciones no visitadas
                    }

                }
                // hemos visitado este URL
                procesadorEnlaces.anadirVisitado(url); // Ya hemos visitado la actual

                if (procesadorEnlaces.cantidad() == 15) { // Hemos reducido el número de webs a visitar hasta el aviso para reducir el tiempo de ejecución
                    System.out.println("Time to visit 15 distinct links = " + (System.nanoTime() - t0));                   
                }

                invokeAll(urls); // Llamamos a invokeAll para visitar recursivamente las webs de la lista 

             } catch (Exception e) {
                //ignorar todos los errores de momento
            }
        }
		
	}
}
