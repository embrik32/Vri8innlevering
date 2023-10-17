package no.hvl.dat100.prosjekt.kontroll;

import java.util.ArrayList;

import no.hvl.dat100.prosjekt.modell.KortSamling;
import no.hvl.dat100.prosjekt.TODO;
import no.hvl.dat100.prosjekt.kontroll.dommer.Regler;
import no.hvl.dat100.prosjekt.kontroll.spill.Handling;
import no.hvl.dat100.prosjekt.kontroll.spill.HandlingsType;
import no.hvl.dat100.prosjekt.kontroll.spill.Spillere;
import no.hvl.dat100.prosjekt.modell.Kort;
import no.hvl.dat100.prosjekt.modell.KortUtils;

/**
 * Klassen har objektvariaber som er referanser til de spillerne, nord og syd
 * (type ISpiller). Den har ogsÂ en bunke man deler/trekker fra og en bunke man
 * spiller til.
 * 
 */
public class Spill  {

	private ISpiller nord;
	private ISpiller syd;
	private Bord bord;
	
	// antall kort som skal deles ut til hver spiller ved start
	public final static int ANTALL_KORT_START = Regler.ANTALL_KORT_START;
	
	public Spill() {
	
		nord= new NordSpiller(Spillere.NORD);
		syd=new SydSpiller(Spillere.SYD);
		bord=new Bord();
	}
	
	/**
	 * Gir referanse/peker til bord.
	 * 
	 * @return referanse/peker bord objekt.
	 */
	public Bord getBord() {
		
		return bord;
		
	}
	
	/**
	 * Gir referanse/peker til syd spilleren.
	 * 
	 * @return referanse/peker til syd spiller.
	 */
	public ISpiller getSyd() {
		
		return syd;
		
	}

	/**
	 * Gir referanse/peker til nord.
	 * 
	 * @return referanse/peker til nord.
	 */
	public ISpiller getNord() {
		
		return nord;
	}

	/**
	 * Metoden oppretter to spillere, nord og syd. Det opprettes to bunker, fra
	 * og til. Alle kortene legges til fra. Bunken fra stokkes. Deretter deles
	 * det ut kort fra fra-bunken til nord og syd i henhold til regler. Til
	 * slutt tas øverste kortet fra fra-bunken og legges til til-bunken.
	 * 
	 * Nord har type RandomSpiller (som er forhåndefinert). Syd vil være spiller
	 * av en klasse laget av gruppen (implementeres i oppgave 3).
	 */
	public void start() {
		Spiller nord= new NordSpiller(Spillere.NORD);
		Spiller syd= new SydSpiller(Spillere.SYD);
		Bord bordspill=bord;
		delutKort();
		Kort kort=bordspill.getBunkeFra().taSiste();
		bordspill.getBunkeTil().leggTil(kort);
	}
		

	/**
	 * Deler ut kort til nord og syd.
	 * 
	 */
	private void delutKort() {

		for(int i=0; i<ANTALL_KORT_START; i++) {
			nord.leggTilKort(bord.getBunkeFra().taSiste());
			syd.leggTilKort(bord.getBunkeFra().taSiste());
		}
	}

	/**
	 * Trekker et kort fra fra-bunken til spilleren gitt som parameter. Om
	 * fra-bunken er tom, må man "snu" til-bunken. Se info om metoden
	 * snuTilBunken().
	 * 
	 * @param spiller
	 *            spilleren som trekker.
	 * 
	 * @return kortet som trekkes.
	 */
	public Kort trekkFraBunke(ISpiller spiller) {
		
		Kort kort=null;
		bord.snuTilBunken();
		kort=bord.getBunkeFra().taSiste();
		spiller.leggTilKort(kort);
		int k=spiller.getAntallTrekk();
		spiller.setAntallTrekk(k+1);
		return kort;

		//if(bord.getBunkeFra().erTom()) {
			//bord.snuTilBunken();
		//}
		//return bord.getBunkeFra().taSiste();
	}

	/**
	 * Gir neste handling for en spiller (spilt et kort, trekker et kort, forbi)
	 * 
	 * @param spiller
	 *            spiller som skal handle.
	 * 
	 * @return handlingen som skal utføres av kontroll delen.
	 */
	public Handling nesteHandling(ISpiller spiller) {
		
		Kort øverst=bord.getBunkeTil().seSiste();
		return spiller.nesteHandling(øverst);
	}

	/**
	 * Metoden spiller et kort. Den sjekker at spiller har kortet. Dersom det er
	 * tilfelle, fjernes kortet fra spilleren og legges til til-bunken. Metoden
	 * nulltiller også antall ganger spilleren har trukket kort.
	 * 
	 * @param spiller
	 *            den som spiller.
	 * @param kort
	 *            kort som spilles.
	 * 
	 * @return true dersom spilleren har kortet, false ellers.
	 */
	public boolean leggnedKort(ISpiller spiller, Kort kort) {
		
		if(spiller.getHand().har(kort)){
			spiller.fjernKort(kort);
			bord.getBunkeTil().leggTil(kort);
			spiller.setAntallTrekk(0);
			return true;
		}
		return false;
	}

	/**
	 * Metode for å si forbi. Må nullstille antall ganger spilleren har trukket
	 * kort.
	 * 
	 * @param spiller
	 *            spilleren som er i tur.
	 */
	public void forbiSpiller(ISpiller spiller) {
		
		spiller.setAntallTrekk(0);
	}

	/**
	 * Metode for å utføre en handling (trekke, spille, forbi). Dersom handling
	 * er kort, blir kortet også spilt.
	 * 
	 * @param spiller
	 *            spiller som utfører handlingen.
	 * @param handling
	 *            handling som utføres.
	 * 
	 * @return kort som trekkes, kort som spilles eller null ved forbi.
	 */
	public Kort utforHandling(ISpiller spiller, Handling handling) {
		
		Kort kort=null;

		if(handling.getType()==HandlingsType.TREKK) {
			kort=bord.getBunkeFra().taSiste();
			spiller.getHand().leggTil(kort);
			
		}else if(handling.getType()==HandlingsType.LEGGNED) {
			Kort[] hand=spiller.getHand().getSamling();
			kort=hand[0];
			spiller.getHand().fjern(kort);
			bord.getBunkeTil().leggTil(kort);
					
		}else if(handling.getType()==HandlingsType.FORBI) {
			kort=null;
			forbiSpiller(spiller);
			return null;
		}
		return kort;

		// Hint: del opp i de tre mulige handlinger og vurder 
		// om noen andre private metoder i klassen kan brukes
		// til å implementere denne metoden
				
	}

}