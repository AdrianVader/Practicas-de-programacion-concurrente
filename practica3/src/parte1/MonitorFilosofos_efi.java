package parte1;

class MonitorFilosofos_efi {

   private int numFils = 0;
   private int[] estado = null;
   private static final int
      THINKING = 0, HUNGRY = 1, EATING = 2, STARVING = 3;
   private static Object[] cond;

   public MonitorFilosofos_efi(int numFils) {
      this.numFils = numFils;
      estado = new int[numFils];
      for (int i = 0; i < numFils; i++) estado[i] = THINKING;
      cond = new Object[numFils];
      for (int i = 0; i < numFils; i++) cond[i] = new Object();
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

   public void takeForks(int i) {
      estado[i] = HUNGRY;	//pasa a estar hambriento
      prueba(i);	//posibles cambios de estado
      while (estado[i] != EATING){	//mientras no pueda comer espera dentro de su objeto de notificaci�n
         synchronized(cond[i]){
            try {cond[i].wait();} catch (Exception e) {} 
          }
      }
   }

   public void putForks(int i) {
      estado[i] = THINKING;	//pasa a estar pensando
      prueba(izquierda(i));	//posibles cambios de estado en su vecino izquierdo
      prueba(derecha(i));	//posibles cambios de estado en su vecino derecho
      
      synchronized(cond[izquierda(i)]){	//se emplea el objeto de notificaci�n para notificar al vecino izquierdo
         cond[izquierda(i)].notify();
      }
      synchronized(cond[derecha(i)]){	//se emplea el objeto de notificaci�n para notificar al vecino derecho
         cond[derecha(i)].notify();
      }
   }
}
