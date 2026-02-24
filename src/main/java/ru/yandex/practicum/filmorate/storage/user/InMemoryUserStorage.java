package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @Override
    public User create(User user) {
        user.setId(currentId++);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь не найден");
        }

        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User getById(int id) {
        User user = users.get(id);

        if (user == null) {
            throw new ValidationException("Пользователь не найден");
        }

        return user;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}
