package view;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 07 sty 2017.
 * 00 : 04
 */
public interface DataCallback<T> {
    void onReceive(T data);
}
