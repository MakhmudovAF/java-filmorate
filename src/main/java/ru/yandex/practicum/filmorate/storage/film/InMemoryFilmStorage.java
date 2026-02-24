package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @Override
    public Film create(Film film) {
        film.setId(currentId++);
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм не найден");
        }

        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film getById(int id) {
        Film film = films.get(id);

        if (film == null) {
            throw new ValidationException("Фильм не найден");
        }

        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }
}