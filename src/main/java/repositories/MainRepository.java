package repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class MainRepository<T> {
    private final List<T> elements = new ArrayList<>();

    public void add(T element) {
        elements.add(element);
    }

    public int getSize() {
        return elements.size();
    }

    public T getElement(int index) {
        return elements.get(index);
    }

//    public List<T> findBy(Predicate<T> predicate) {
//        return elements.stream()
//                .filter(predicate)
//                .toList();
//    }
}