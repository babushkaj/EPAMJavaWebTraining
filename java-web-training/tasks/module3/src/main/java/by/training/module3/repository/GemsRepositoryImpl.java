package by.training.module3.repository;

import by.training.module3.entity.Gem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GemsRepositoryImpl implements GemsRepository<Gem> {
    private List<Gem> gems;

    public GemsRepositoryImpl() {
        this.gems = new ArrayList<>();
    }

    @Override
    public void add(Gem gem) {
        this.gems.add(gem);
    }

    @Override
    public Optional<Gem> read(long id) {
        return this.gems.stream().filter((g) -> g.getId() == id).findFirst();
    }

    @Override
    public List<Gem> getAll() {
        return this.gems;
    }
}
