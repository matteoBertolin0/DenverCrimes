package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		m.creaGrafo("drug-alcohol", 2);
//		System.out.println("archi richiesti: " + m.getArchiMaggioriPesoMedio());
		System.out.println("\n\n\nPercorso: " + m.calcolaPercorso("drug-cocaine-sell", "drug-cocaine-possess"));
	}

}
