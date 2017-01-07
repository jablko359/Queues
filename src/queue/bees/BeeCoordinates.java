package queue.bees;

/**
 * Created by Standy on 2017-01-07.
 */
//W tej klasie sa wspolrzedne pszczoły, czyli parametry, wejsciowe algorytmu
public class BeeCoordinates {

	//dlugosc kolejki
	private Integer channelsNumber;
	private Integer minChannelsNumber;
	private Integer maxChannelsNumber;

	public void initialize() {
		if (minChannelsNumber != null && maxChannelsNumber != null) {
			this.channelsNumber = minChannelsNumber + (int) (Math.floor(Math.random() * (maxChannelsNumber - minChannelsNumber)));
		}
	}

	public void setMinChannelsNumber(Integer minChannelsNumber) {
		this.minChannelsNumber = minChannelsNumber;
	}

	public void setMaxChannelsNumber(Integer maxChannelsNumber) {
		this.maxChannelsNumber = maxChannelsNumber;
	}

	public BeeCoordinates(Integer minChannelsNumber, Integer maxChannelsNumber) {
		this.minChannelsNumber = minChannelsNumber;
		this.maxChannelsNumber = maxChannelsNumber;
		initialize();
	}

	public Integer getChannelsNumber() {
		return channelsNumber;
	}
}
