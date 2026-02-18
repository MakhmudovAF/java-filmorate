package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Запрос на создание пользователя: {}", user);

        validateUser(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя установлено как логин: {}", user.getLogin());
        }

        user.setId(currentId++);
        users.put(user.getId(), user);

        log.info("Пользователь успешно создан с id: {}", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Запрос на обновление пользователя: {}", user);

        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с id {} не найден", user.getId());
            throw new ValidationException("Пользователь с id " + user.getId() + " не найден");
        }

        validateUser(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя установлено как логин: {}", user.getLogin());
        }

        users.put(user.getId(), user);

        log.info("Пользователь с id {} успешно обновлен", user.getId());
        return user;
    }

    private void validateUser(User user) {
        // Проверка email
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Ошибка валидации: email не может быть пустым");
            throw new ValidationException("Электронная почта не может быть пустой");
        }

        if (!user.getEmail().contains("@")) {
            log.error("Ошибка валидации: email должен содержать символ @");
            throw new ValidationException("Электронная почта должна содержать символ @");
        }

        // Проверка логина
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("Ошибка валидации: логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        }

        if (user.getLogin().contains(" ")) {
            log.error("Ошибка валидации: логин не может содержать пробелы");
            throw new ValidationException("Логин не может содержать пробелы");
        }

        // Проверка даты рождения
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации: дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}