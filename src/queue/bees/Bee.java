package queue.bees;

import java.util.Map;

/**
 * Created by Standy on 2017-01-07.
 */
public class Bee implements Comparable<Bee> {

    //    ta wartosc bedzie sie zmiejszac z kazda iteracja, jesli wyniesie 0 to zabijamy pszczolke
    private Integer timeToLive;

    //    nasz funkcja celu dla pszczolki
    private Double quality;

    //        wspolrzedne dla systemu
    private Map<String, BeeCoordinates> coordinates;

    //    constructor
    public Bee(Integer timeToLive, Map<String, BeeCoordinates> coords) {
        this.timeToLive = timeToLive;
        this.coordinates = coords;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

	public void setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    public Double getQuality() {
        return quality;
    }

    public void setQuality(Double quality) {
        this.quality = quality;
    }

    public Map<String, BeeCoordinates> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Map<String, BeeCoordinates> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int compareTo(Bee o) {
        return quality.compareTo(o.getQuality());
    }
}
