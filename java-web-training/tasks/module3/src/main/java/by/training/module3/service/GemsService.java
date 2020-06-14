package by.training.module3.service;

import by.training.module3.entity.Gem;
import by.training.module3.repository.GemsRepository;
import by.training.module3.repository.GemsRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GemsService {
    private GemsRepository repository;

    public GemsService(GemsRepositoryImpl repository) {
        this.repository = repository;
    }

    public GemsService() {
        repository = new GemsRepositoryImpl();
    }

    public void add(Gem gem) {
        repository.add(gem);
    }

    public Optional<Gem> read(long id) {
        return repository.read(id);
    }

    public List<Gem> getAll() {
        return new ArrayList<>(repository.getAll());
    }

}
