package queue.bees;

/**
 * Created by Standy on 2017-01-07.
 */
//W tej klasie sa wspolrzedne pszczoły, czyli parametry, wejsciowe algorytmu
public class BeeCoordinates {

	public static Integer getRandomChannel(int minChannelsNumber, int maxChannelsNumber, int currentValue) {
		Integer newValue = minChannelsNumber + (int) (Math.floor(Math.random() * (maxChannelsNumber - minChannelsNumber)));
		if (currentValue == newValue) {
			newValue = getRandomChannel(minChannelsNumber, maxChannelsNumber, currentValue);
		}
		return newValue;
	}

	public static Integer getRandomChannel(int minChannelsNumber, int maxChannelsNumber) {
		return minChannelsNumber + (int) (Math.floor(Math.random() * (maxChannelsNumber - minChannelsNumber)));
	}

}
