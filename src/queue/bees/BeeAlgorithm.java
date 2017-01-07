package queue.bees;

import queue.QueueNetwork;
import queue.systems.FifoSystem;
import queue.systems.QueueSystem;

import java.util.*;

/**
 * Created by Standy on 2017-01-07.
 */
public class BeeAlgorithm {

	private static final int MIN_CHANNEL = 1;
	private static final int MAX_CHANNEL = 15;

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

	private List<Bee> globalBestBees;

	public BeeAlgorithm(QueueNetwork network, Integer bestSolutionsNumber, Integer exclusiveSolutionsNumber, Integer totalSolutionsNumber, Integer bestSolutionsNeighberhoodNumber, Integer exclusiveSolutionsNeighberhoodNumber) {
		this.network = network;
		this.bestSolutionsNumber = bestSolutionsNumber;
		this.exclusiveSolutionsNumber = exclusiveSolutionsNumber;
		this.totalSolutionsNumber = totalSolutionsNumber;
		this.bestSolutionsNeighberhoodNumber = bestSolutionsNeighberhoodNumber;
		this.exclusiveSolutionsNeighberhoodNumber = exclusiveSolutionsNeighberhoodNumber;
	}

	public void createFirstPopulation() {
		//utworz wspolrzedne z min i max iloscia kanalow
		for (int i = 0; i < totalSolutionsNumber; i++) {
			Map<DziekanatNodeType, Integer> coordinates = new HashMap<>();
			coordinates.put(DziekanatNodeType.DZIENNE, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));
			coordinates.put(DziekanatNodeType.ZAOCZNE, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));
			coordinates.put(DziekanatNodeType.DOKTORANCKIE, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));
			coordinates.put(DziekanatNodeType.SOCJALNE, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));
			coordinates.put(DziekanatNodeType.DZIEKAN, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));

			Bee bee = new Bee(0, coordinates);

			Map<String, QueueSystem> systems = network.getSystemsMap();
			((FifoSystem) systems.get(DziekanatNodeType.DZIENNE.toString().toLowerCase())).setM(coordinates.get(DziekanatNodeType.DZIENNE));
			((FifoSystem) systems.get(DziekanatNodeType.ZAOCZNE.toString().toLowerCase())).setM(coordinates.get(DziekanatNodeType.ZAOCZNE));
			((FifoSystem) systems.get(DziekanatNodeType.SOCJALNE.toString().toLowerCase())).setM(coordinates.get(DziekanatNodeType.SOCJALNE));
			((FifoSystem) systems.get(DziekanatNodeType.DZIEKAN.toString().toLowerCase())).setM(coordinates.get(DziekanatNodeType.DZIEKAN));
			((FifoSystem) systems.get(DziekanatNodeType.DOKTORANCKIE.toString().toLowerCase())).setM(coordinates.get(DziekanatNodeType.DOKTORANCKIE));

			//TODO TYMCZASOWE LOSOWANIE
			//pobrac czas przebywania w kolejce
//            double queeTime = network.getTimeInQuee();
			double queeTime = (double) BeeCoordinates.getRandomChannel(0, 100);
			bee.setQuality(calculateQuality(queeTime));
			bees.add(bee);
		}

	}

	//metoda initialize utworzy nam listy najlepszych, elitarnych oraz wszystkich pszczol
	public void initialize(Integer beeTimeToLive) {
		bees = new ArrayList<>();
		createFirstPopulation();

		//sort array
		Collections.sort(bees);

		exclusiveBees = new ArrayList<>(bees.subList(0, exclusiveSolutionsNumber));
		bestBees =  new ArrayList<>(bees.subList(exclusiveSolutionsNumber, exclusiveSolutionsNumber + bestSolutionsNumber));
	}

	//tutaj właściwe obliczenia algorytmu
	public void calculate() {
		int iterationIndex = 0;
		globalBestBees = new ArrayList<>();

		//Najlepsza z pszczółek początkowych
		globalBestBees.add(exclusiveBees.get(0));
		System.out.println("GLOBAL SOLUTION:" + exclusiveBees.get(0).getQuality());
		while (ITERATIONS_NUMBER > iterationIndex) {
//			System.out.println("Iteracja:" + (iterationIndex + 1));
			List<Bee> newExclusiveBees = generateNewBestBee(2, exclusiveBees);
			List<Bee> newBestBees = generateNewBestBee(1, bestBees);

			//Tworzymy nową listę pszczółek do kolejnego kroku algorytmu
			bees.removeAll(exclusiveBees);
			bees.removeAll(bestBees);
			bees.addAll(newExclusiveBees);
			bees.addAll(newBestBees);
			Collections.sort(bees);

			iterationIndex++;
			//Jeśli znaleziono lepsze rozwiązanie od najelpszego to dodajemy do listy
			//Index listy odpowiada iteracji algorytmu dlatego w przypadku braku poprawy przepisujemy najlepszą starą wartość
			if (newExclusiveBees.get(0).getQuality() < globalBestBees.get(globalBestBees.size()-1).getQuality()) {
				globalBestBees.add(newExclusiveBees.get(0));
				System.out.println("GLOBAL SOLUTION:" + newExclusiveBees.get(0).getQuality());
				System.out.println(bees.get(0));
			} else {
				globalBestBees.add(globalBestBees.get(globalBestBees.size()-1));
			}
		}
	}

	private List<Bee> generateNewBestBee(int importanceFactor, List<Bee> beeList) {
		List<Bee> newBestBees = new ArrayList<>();
		for (Bee bee : beeList) {
			List<Bee> newOrdinaryBees = new ArrayList<>();
			while (newOrdinaryBees.size() != bee.getCoordinates().size()*importanceFactor) {
				for (Map.Entry entry : bee.getCoordinates().entrySet()) {
					Bee newBee = new Bee(bee);
					newBee.getCoordinates().put((DziekanatNodeType) entry.getKey(), BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL, (Integer) entry.getValue()));
					if (!newOrdinaryBees.stream().anyMatch(b -> b.getCoordinates().get(entry.getKey()).equals(newBee.getCoordinates().get(entry.getKey())))) {
						//TODO tymczasowy randomowy nowy współczynnik
						double queeTime = (double) BeeCoordinates.getRandomChannel(0, 100);
						newBee.setQuality(queeTime);
						newOrdinaryBees.add(newBee);
					}
					if (newOrdinaryBees.size() == bee.getCoordinates().size()*importanceFactor) {
						break;
					}
				}
			}
			Collections.sort(newOrdinaryBees);
			newBestBees.add(newOrdinaryBees.get(0));
		}
		Collections.sort(newBestBees);

		//zwracamy nowe pszczółki niezależnie od tego czy dają ona lepsze rozwiązania od pszczółek bazowych
		return newBestBees;
	}

	private double calculateQuality(double waitTime) {
		//TODO
		//Obliczyc funkcje celu
		return waitTime;
	}
}
