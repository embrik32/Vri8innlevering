package no.hvl.dat100.prosjekt.modell;

import java.util.Random;

import no.hvl.dat100.prosjekt.TODO;

public class KortUtils {

	/**
	 * Sorterer en samling. Rekkefølgen er bestemt av compareTo() i Kort-klassen.
	 * 
	 * @see Kort
	 * 
	 * @param samling
	 * 			samling av kort som skal sorteres. 
	 */
	
	public static void sorter(KortSamling samling) {
		Kort[] kortene=samling.getSamling();
		int antallKort=samling.getAntalKort();
		
		for(int i=0; i<antallKort-1; i++) {
			for(int j=0; j<antallKort-i-1; j++) {
				if(kortene[j].compareTo(kortene[j+1])>0) {
					
					Kort midlertidig= kortene[j];
					kortene[j]=kortene[j+1];
					kortene[j+1]=midlertidig;
				}
			}
		}
	}
	
	/**
	 * Stokkar en kortsamling. 
	 * 
	 * @param samling
	 * 			samling av kort som skal stokkes. 
	 */
	public static void stokk(KortSamling samling) {
		Kort[] kortene = samling.getSamling();
		int antallKort = samling.getAntalKort();

		Random random = new Random();

		for (int i = antallKort - 1; i > 0; i--) {
			int tilfeldigKort = random.nextInt(i + 1);

			Kort midlertidig = kortene[i];
			kortene[i] = kortene[tilfeldigKort];
			kortene[tilfeldigKort] = midlertidig;

		}

	}

}
