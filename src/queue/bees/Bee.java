package queue.bees;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Standy on 2017-01-07.
 */
public class Bee implements Comparable<Bee> {

    //    ta wartosc bedzie sie zmiejszac z kazda iteracja, jesli wyniesie 0 to zabijamy pszczolke
    private Integer timeToLive;

    //    nasz funkcja celu dla pszczolki
    private Double quality;

    //ilość kanałów w systemie
    private Map<DziekanatNodeType, Integer> coordinates;

    public Bee(Bee bee) {
        this.timeToLive = new Integer(bee.getTimeToLive());
        this.coordinates = new HashMap<>(bee.getCoordinates());
    }

    //    constructor
    public Bee(Integer timeToLive, Map<DziekanatNodeType, Integer> coords) {
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

    public Map<DziekanatNodeType, Integer> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Map<DziekanatNodeType, Integer> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int compareTo(Bee o) {
        return quality.compareTo(o.getQuality());
    }

    @Override
    public String toString() {
        return "Bee{" +
                "quality=" + quality +
                ", coordinates=" + coordinates +
                '}';
    }
}
