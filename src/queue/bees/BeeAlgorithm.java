package queue.bees;

import queue.QueueNetwork;
import queue.systems.FifoSystem;
import queue.systems.QueueSystem;

import java.util.*;

/**
 * Created by Standy on 2017-01-07.
 */
public class BeeAlgorithm {

	//    ilosc iteracji algorytmu
	private static Integer ITERATIONS_NUMBER = 1000;

	//siec
	private QueueNetwork network;

	//    najlepsza wartosc w danym kroku
	private Double bestValue;

	//    lista najlepszych pszczol - dodajemy tu najlepsza z danego kroku, aby moc zobaczyc jak sie zmienia funkcja celu
	private LinkedList<Bee> calculatedBees;

	//    liczba najlepszych rozwiazan
	private Integer bestSolutionsNumber;

	//    liczba elitarnych rozwiązan
	private Integer exclusiveSolutionsNumber;

	//    liczba wszystkich rozwiazan
	private Integer totalSolutionsNumber;

	//    wielkosc sasiedztwa dla najlepszych rozwiazan
	private Integer bestSolutionsNeighberhoodNumber;

	//    wielkosc sasiedztwa dla rozwiazan elitarnych
	private Integer exclusiveSolutionsNeighberhoodNumber;

	//    lista aktualnych pszczol
	private List<Bee> bees;

	//    list najlepszych pszczol
	private List<Bee> bestBees;

	//  lista elitarnych pszczol
	private List<Bee> exclusiveBees;

	public BeeAlgorithm(QueueNetwork network, Integer bestSolutionsNumber, Integer exclusiveSolutionsNumber, Integer totalSolutionsNumber, Integer bestSolutionsNeighberhoodNumber, Integer exclusiveSolutionsNeighberhoodNumber) {
		this.network = network;
		this.bestSolutionsNumber = bestSolutionsNumber;
		this.exclusiveSolutionsNumber = exclusiveSolutionsNumber;
		this.totalSolutionsNumber = totalSolutionsNumber;
		this.bestSolutionsNeighberhoodNumber = bestSolutionsNeighberhoodNumber;
		this.exclusiveSolutionsNeighberhoodNumber = exclusiveSolutionsNeighberhoodNumber;
	}

	//metoda initialize utworzy nam listy najlepszych, elitarnych oraz wszystkich pszczol
	public void initialize(Integer beeTimeToLive, Map<String, BeeCoordinates> coordinates) {
		bees = new ArrayList<>();

		for (int i = 0; i < totalSolutionsNumber; i++) {
			Bee bee = new Bee(beeTimeToLive, coordinates);
			Map<String, QueueSystem> systems = network.getSystemsMap();

			((FifoSystem) systems.get("dzienne")).setM(coordinates.get("dzienne").getChannelsNumber());
			((FifoSystem) systems.get("zaoczne")).setM(coordinates.get("zaoczne").getChannelsNumber());
			((FifoSystem) systems.get("socjalne")).setM(coordinates.get("socjalne").getChannelsNumber());
			((FifoSystem) systems.get("dziekan")).setM(coordinates.get("dziekan").getChannelsNumber());
			((FifoSystem) systems.get("doktoranckie")).setM(coordinates.get("doktoranckie").getChannelsNumber());

			//TODO
			//pobrac czas przebywania w kolejce
//            double queeTime = network.getTimeInQuee();
			double queeTime = 1.0;
			bee.setQuality(calculateQuality(queeTime));

			bees.add(bee);
		}
		//sort array
		Collections.sort(bees);

		exclusiveBees = bees.subList(0, exclusiveSolutionsNumber - 1);
		bestBees = bees.subList(exclusiveSolutionsNumber, exclusiveSolutionsNumber + bestSolutionsNumber - 1);
	}

	//tutaj właściwe obliczenia algorytmu
	public void calculate() {
		int iterationIndex = 0;

		while (ITERATIONS_NUMBER < iterationIndex) {
			getBestBeesFromAllPossibilities(exclusiveBees);
			getBestBeesFromAllPossibilities(bestBees);

			iterationIndex++;
		}
	}

	private double calculateQuality(double waitTime) {
		//TODO
		//Obliczyc funkcje celu
		return waitTime;
	}

	private void getBestBeesFromAllPossibilities(List<Bee> oldBees) {
		List<Bee> newBees = new ArrayList<>();
		int numberOfBees = 0;

//		for (Bee bee : oldBees) {
//			for (Map.Entry entry : bee.getCoordinates().entrySet()) {
//				Map<String, BeeCoordinates> newCoordinates = new HashMap<>(bee.getCoordinates());
//
//				for (int i = 0; i < )
//
//			}
//
//			Bee newBee = new Bee(10, coordinates); //TODO TTL
//			newBees.add(newBee);
//		}

	}
}
