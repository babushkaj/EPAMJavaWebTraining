package by.training.module1.service;

import by.training.module1.model.*;
import by.training.module1.repository.ByDurationFromRecordSpecification;
import by.training.module1.repository.ByDurationToRecordSpecification;
import by.training.module1.repository.ByStyleComparator;
import by.training.module1.repository.RecordRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class RecordServiceTest {

    public static final int EXPECTED_DURATION = 2497;

    public static final int REC_COUNT_BY_DURATION = 5;
    public static final int DURATION_FROM = 240;
    public static final int DURATION_TO = 260;

    public static Record expectedFirstRecord;
    private RecordService service;
    private RecordRepository repository;

    @Before
    public void setUp() {

        repository = new RecordRepository();
        service = new RecordService(repository);

        service.save(new CDDARecord("Tech N9ne", "Uralya", 248, Style.RAP, Compression.LOW));
        service.save(new CDDARecord("2PAC", "Baby don't cry", 259, Style.RAP, Compression.LOW));
        service.save(new CDDARecord("50Cent", "Candy shop", 208, Style.RAP, Compression.LOW));
        Map<String, String> metallicaID3Tags = new HashMap<>();
        metallicaID3Tags.put("year", "1986");
        service.save(new MP3Record("Metallica", "Master of puppets", 513, Style.ROCK, Compression.MID, metallicaID3Tags));
        Map<String, String> ironMaidenID3Tags = new HashMap<>();
        ironMaidenID3Tags.put("year", "1983");
        service.save(new MP3Record("Iron Maiden", "The Trooper", 251, Style.ROCK, Compression.MID, ironMaidenID3Tags));
        Map<String, String> dopeID3Tags = new HashMap<>();
        dopeID3Tags.put("year", "2009");
        service.save(new MP3Record("Dope", "Best for me", 201, Style.ROCK, Compression.MID, dopeID3Tags));
        Map<String, String> spearsID3Tags = new HashMap<>();
        spearsID3Tags.put("year", "2000");
        service.save(new MP3Record("Britney Spears", "Oops!...I Did It Again", 251,Style.POP, Compression.HIGH, spearsID3Tags));
        Map<String, String> madonnaID3Tags = new HashMap<>();
        madonnaID3Tags.put("year", "2005");
        service.save(new MP3Record("Madonna", "Hung Up", 326, Style.POP, Compression.HIGH, madonnaID3Tags));
        Map<String, String> perryID3Tags = new HashMap<>();
        perryID3Tags.put("year", "2005");
        service.save(new MP3Record("Katy Perry", "Never Really Over", 240, Style.POP, Compression.HIGH, perryID3Tags));

        expectedFirstRecord = new MP3Record("Britney Spears", "Oops!...I Did It Again",
                                        251, Style.POP, Compression.HIGH, spearsID3Tags);

    }

    @Test
    public void sortByStyle() {

        List<Record> recordsList = service.sortBy(new ByStyleComparator());

        Assert.assertEquals(recordsList.get(0), expectedFirstRecord);

    }

    @Test
    public void returnAllRecordsDuration() {

        int duration = service.getDuration();

        Assert.assertEquals(duration, EXPECTED_DURATION);
    }

    @Test
    public void findMethodReturnsRecordsByDuration(){

        List<Record> recordsList = service.find(new ByDurationFromRecordSpecification(DURATION_FROM)
                                                .and(new ByDurationToRecordSpecification(DURATION_TO)));

        Assert.assertEquals(recordsList.size(), REC_COUNT_BY_DURATION);

    }

}
