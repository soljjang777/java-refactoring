package cleancode.studycafe.tobe.io;

import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudyCafeFileHandler {

    private static final String BASE_FILE_PATH = "src/main/resources/cleancode/studycafe/";
    private static final String PASS_LIST_FILE = BASE_FILE_PATH + "pass-list.csv";
    private static final String LOCKER_PASS_FILE = BASE_FILE_PATH + "locker.csv";

    public List<StudyCafePass> readStudyCafePasses() {
            List<String> lines = getReadAllLines(PASS_LIST_FILE);
        return getStudyCafePasses(lines);
    }

    public List<StudyCafeLockerPass> readLockerPasses() {
        List<String> lines = getReadAllLines(LOCKER_PASS_FILE);
        return getStudyCafeLockerPasses(lines);
    }

    private static List<String> getReadAllLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }

    private static List<StudyCafePass> getStudyCafePasses(List<String> lines) {
        List<StudyCafePass> studyCafePasses = new ArrayList<>();
        for (String line : lines) {
            String[] values = line.split(",");
            StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
            int duration = Integer.parseInt(values[1]);
            int price = Integer.parseInt(values[2]);
            double discountRate = Double.parseDouble(values[3]);

            StudyCafePass studyCafePass = StudyCafePass.of(studyCafePassType, duration, price, discountRate);
            studyCafePasses.add(studyCafePass);
        }

        return studyCafePasses;
    }

    private static List<StudyCafeLockerPass> getStudyCafeLockerPasses(List<String> lines) {
        List<StudyCafeLockerPass> lockerPasses = new ArrayList<>();
        for (String line : lines) {
            String[] values = line.split(",");
            StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
            int duration = Integer.parseInt(values[1]);
            int price = Integer.parseInt(values[2]);

            StudyCafeLockerPass lockerPass = StudyCafeLockerPass.of(studyCafePassType, duration, price);
            lockerPasses.add(lockerPass);
        }

        return lockerPasses;
    }

}
