package repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class MainRepository<T> {

    private final List<T> elements = new ArrayList<>();

    public void addElement(T element) {
        if (element == null) {
            throw new RuntimeException("element jest nullem");
        }
        elements.add(element);
    }


    public void removeElement(T element) {
        if (element == null) {
            throw new RuntimeException("element jest nullem");
        }

        boolean removed = elements.remove(element);
        if (!removed) {
            throw new RuntimeException("Nie znaleziono takiego elementu");
        }
    }


    public int getSize() {
        return elements.size();
    }


    public T getElement(int index) {
        if (index < 0 || index >= elements.size()) {
            throw new RuntimeException("Niewłaściwy indeks");
        }
        return elements.get(index);
    }

    public List<T> findBy(Predicate<T> predicate) {
        List<T> found = new ArrayList<>();
        for (T element : elements) {
            if (predicate.test(element)) {
                found.add(element);
            }
        }
        return found;
    }


    public List<T> findAll() {
        return elements;
    }
}