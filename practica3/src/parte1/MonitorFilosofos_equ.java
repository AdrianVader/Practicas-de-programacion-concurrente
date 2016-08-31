package parte1;

class MonitorFilosofos_equ {

   private int numFils = 0;
   private int[] estado = null;
   private static final int
      THINKING = 0, HUNGRY = 1, EATING = 2, STARVING = 3;

   public MonitorFilosofos_equ(int numFils) {
      this.numFils = numFils;
      estado = new int[numFils];
      for (int i = 0; i < numFils; i++) estado[i] = THINKING;
   }

   private final int izquierda(int i) {
	return (numFils + i - 1) % numFils;
   }

   private final int derecha(int i) {
	return (i + 1) % numFils;
   }   
   
   private void prueba(int k) {
	   if(!(estado[izquierda(k)] == STARVING || estado[derecha(k)] == STARVING)){	//si ninguno de sus vecinos est� fam�lico
		   if (estado[izquierda(k)] != EATING && (estado[k] == HUNGRY || estado[k] == STARVING) && estado[derecha(k)] != EATING)	//y si est� hambriento o fam�lico, y adem�s ninguno de sus vecinos est� comiendo
			   estado[k] = EATING;	//puede comer
		   else if (estado[k] == HUNGRY && (estado[izquierda(k)] != EATING || estado[derecha(k)] != EATING)){	//si no, si est� hambriento pero alguno de sus vecinos est� comiendo
			   System.out.println("Fil�sofo " + k + " est� fam�lico");
			   estado[k] = STARVING;	//pasa a estar fam�lico
		   }
	   }
   }

   public synchronized void takeForks(int i) {
      estado[i] = HUNGRY;	//pasa a estar hambriento
      prueba(i);	//posibles cambios de estado
      while (estado[i] != EATING)	//mientras no pueda comer espera
         try {wait();} catch (InterruptedException e) {}
   }

   public synchronized void putForks(int i) {
      estado[i] = THINKING;	//pasa a estar pensando
      prueba(izquierda(i));	//posibles cambios de estado en su vecino izquierdo
      prueba(derecha(i));	//posibles cambios de estado en su vecino derecho
      notifyAll();	//notifica a los dem�s
   }
}
