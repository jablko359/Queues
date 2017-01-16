package queue.bees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import queue.ClosedNetwork;
import queue.QueueNetwork;
import queue.exceptions.IncorrectUtilizationException;
import queue.exceptions.QueueException;
import queue.systems.FifoSystem;
import queue.systems.QueueSystem;

/**
 * Created by Standy on 2017-01-07.
 */
public class BeeAlgorithm {

    public static int MIN_CHANNEL = 1;
    public static int MAX_CHANNEL = 20;

    public static int TTL = 10;

    //    ilosc iteracji algorytmu
    public static int ITERATIONS_NUMBER = 200;

    public static double AVERAGE_TIME_COEFFICIENT = 100;

    public static double NUMBER_OF_CHANNELS_COEFFICIENT = 30;

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

    public List<Bee> getGlobalBestBees() {
        return globalBestBees;
    }

    public BeeAlgorithm(QueueNetwork network, Integer bestSolutionsNumber, Integer exclusiveSolutionsNumber, Integer totalSolutionsNumber, Integer bestSolutionsNeighberhoodNumber, Integer exclusiveSolutionsNeighberhoodNumber) {
        this.network = network;
        this.bestSolutionsNumber = bestSolutionsNumber;
        this.exclusiveSolutionsNumber = exclusiveSolutionsNumber;
        this.totalSolutionsNumber = totalSolutionsNumber;
        this.bestSolutionsNeighberhoodNumber = bestSolutionsNeighberhoodNumber;
        this.exclusiveSolutionsNeighberhoodNumber = exclusiveSolutionsNeighberhoodNumber;
    }

    public void createBees(int numberOfBees) {
        //utworz wspolrzedne z min i max iloscia kanalow
        for (int i = 0; i < numberOfBees; i++) {
            Map<DziekanatNodeType, Integer> coordinates = new HashMap<>();
            coordinates.put(DziekanatNodeType.DZIENNE, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));
            coordinates.put(DziekanatNodeType.ZAOCZNE, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));
            coordinates.put(DziekanatNodeType.DOKTORANCKIE, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));
            coordinates.put(DziekanatNodeType.SOCJALNE, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));
            coordinates.put(DziekanatNodeType.DZIEKAN, BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL));

            Bee bee = new Bee(TTL, coordinates);

            if (!updateM(bee)) {
                i = i -1;
                continue;
            }

            bee.setQuality(calculateQuality(bee));
            bees.add(bee);
        }

    }

    //metoda initialize utworzy nam listy najlepszych, elitarnych oraz wszystkich pszczol
    public void initialize() {
        bees = new ArrayList<>();
        createBees(totalSolutionsNumber);

        //sort array
        Collections.sort(bees);

        exclusiveBees = new ArrayList<>(bees.subList(0, exclusiveSolutionsNumber));
        bestBees = new ArrayList<>(bees.subList(exclusiveSolutionsNumber, exclusiveSolutionsNumber + bestSolutionsNumber));
    }

    //tutaj właściwe obliczenia algorytmu
    public void calculate() {
        int iterationIndex = 0;
        globalBestBees = new ArrayList<>();

        //Najlepsza z pszczółek początkowych
        globalBestBees.add(exclusiveBees.get(0));
        System.out.println(iterationIndex + ". " + bees.get(0) + "\n");
        while (ITERATIONS_NUMBER > iterationIndex) {
//			System.out.println("Iteracja:" + (iterationIndex + 1));

            updateExclusiveAndBest();

            List<Bee> newExclusiveBees = generateNewBestBee(2, exclusiveBees);
            List<Bee> newBestBees = generateNewBestBee(1, bestBees);

            //Tworzymy nową listę pszczółek do kolejnego kroku algorytmu
            bees.removeAll(exclusiveBees);
            bees.removeAll(bestBees);

            //Decrease TTL
            decreaseTTL();

            bees.addAll(newExclusiveBees);
            bees.addAll(newBestBees);
            Collections.sort(bees);

            iterationIndex++;
            //Jeśli znaleziono lepsze rozwiązanie od najelpszego to dodajemy do listy
            //Index listy odpowiada iteracji algorytmu dlatego w przypadku braku poprawy przepisujemy najlepszą starą wartość
            if (newExclusiveBees.get(0).getQuality() < globalBestBees.get(globalBestBees.size() - 1).getQuality()) {
                globalBestBees.add(newExclusiveBees.get(0));
                System.out.println(iterationIndex + ". " + newExclusiveBees.get(0) + "\n");
            } else {
                globalBestBees.add(globalBestBees.get(globalBestBees.size() - 1));
            }
        }
    }

    private List<Bee> generateNewBestBee(int importanceFactor, List<Bee> beeList) {
        List<Bee> newBestBees = new ArrayList<>();
        for (Bee bee : beeList) {
            List<Bee> newOrdinaryBees = new ArrayList<>();
            while (newOrdinaryBees.size() != bee.getCoordinates().size() * importanceFactor) {
                for (Map.Entry entry : bee.getCoordinates().entrySet()) {
                    Bee newBee = new Bee(bee);
                    newBee.getCoordinates().put((DziekanatNodeType) entry.getKey(), BeeCoordinates.getRandomChannel(MIN_CHANNEL, MAX_CHANNEL, (Integer) entry.getValue()));
                    if (!newOrdinaryBees.stream().anyMatch(b -> b.getCoordinates().get(entry.getKey()).equals(newBee.getCoordinates().get(entry.getKey())))) {
                        if (updateM(newBee)) {
                            newBee.setTimeToLive(TTL);
                            newBee.setQuality(calculateQuality(newBee));
                            newOrdinaryBees.add(newBee);
                        }
                    }
                    if (newOrdinaryBees.size() == bee.getCoordinates().size() * importanceFactor) {
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

    private double calculateQuality(Bee bee) {
        //średni czas oczekiwania w kolejce
        Double averageTime = 0.0;
        int numberOfChannels = 0;

		if (network.isOpen()) {
			for (String customer : network.getActiveCustomerTypes()) {
				averageTime += network.getLambdaT(DziekanatNodeType.DZIENNE.toString().toLowerCase(), customer);
				averageTime += network.getLambdaT(DziekanatNodeType.ZAOCZNE.toString().toLowerCase(), customer);
				averageTime += network.getLambdaT(DziekanatNodeType.SOCJALNE.toString().toLowerCase(), customer);
				averageTime += network.getLambdaT(DziekanatNodeType.DOKTORANCKIE.toString().toLowerCase(), customer);
				averageTime += network.getLambdaT(DziekanatNodeType.DZIEKAN.toString().toLowerCase(), customer);
			}
		} else {
			int counter = 0;
			for (int i = 0; i < ((ClosedNetwork) network).getResidenceTime().getRowDimension(); i++) {
				for (int j = 0; j < ((ClosedNetwork) network).getResidenceTime().getColumnDimension(); j++) {
					if (((ClosedNetwork) network).getResidenceTime().getEntry(i,j) > 0 ) {
						averageTime += ((ClosedNetwork) network).getResidenceTime().getEntry(i,j);
						counter++;
					}
				}
			}
			averageTime = (averageTime / counter) * 10;
		}

        for (Map.Entry entry : bee.getCoordinates().entrySet()) {
            numberOfChannels += (int) entry.getValue();
        }

        //TODO Funkcja celu
        return averageTime * AVERAGE_TIME_COEFFICIENT + numberOfChannels * NUMBER_OF_CHANNELS_COEFFICIENT;
    }

    private boolean updateM(Bee bee) {
        Map<String, QueueSystem> systems = network.getSystemsMap();
		(systems.get(DziekanatNodeType.DZIENNE.toString().toLowerCase())).setM(bee.getCoordinates().get(DziekanatNodeType.DZIENNE));
        (systems.get(DziekanatNodeType.ZAOCZNE.toString().toLowerCase())).setM(bee.getCoordinates().get(DziekanatNodeType.ZAOCZNE));
        (systems.get(DziekanatNodeType.SOCJALNE.toString().toLowerCase())).setM(bee.getCoordinates().get(DziekanatNodeType.SOCJALNE));
        (systems.get(DziekanatNodeType.DZIEKAN.toString().toLowerCase())).setM(bee.getCoordinates().get(DziekanatNodeType.DZIEKAN));
        (systems.get(DziekanatNodeType.DOKTORANCKIE.toString().toLowerCase())).setM(bee.getCoordinates().get(DziekanatNodeType.DOKTORANCKIE));

		try {
			network.calculateParameters(false);
		} catch (IncorrectUtilizationException iue) {
            return false;
        } catch (QueueException e) {
			e.printStackTrace();
		}
		return true;
    }


    private void decreaseTTL() {
        List<Bee> beesToRemove = new ArrayList<>();
        for (Bee bee : bees) {
            bee.setTimeToLive(bee.getTimeToLive() - 1);
            if (bee.getTimeToLive().equals(0)) {
                beesToRemove.add(bee);
            }
        }

        bees.removeAll(beesToRemove);
        createBees(beesToRemove.size());
    }

    private void updateExclusiveAndBest() {
        exclusiveBees = new ArrayList<>(bees.subList(0, exclusiveSolutionsNumber));
        bestBees = new ArrayList<>(bees.subList(exclusiveSolutionsNumber, exclusiveSolutionsNumber + bestSolutionsNumber));
    }

    public QueueNetwork getQueueNetwork() {
        return network;
    }
}
