package cleancode.studycafe.tobe.io;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

import java.util.List;
import java.util.Scanner;

public class InputHandler {

    private static final Scanner SCANNER = new Scanner(System.in);

    public StudyCafePassType getPassTypeSelectingUserAction() {
        String userInput = SCANNER.nextLine();

        if (isPassTypeHourly(userInput)) {
            return StudyCafePassType.HOURLY;
        }
        if (isPassTypeWeekly(userInput)) {
            return StudyCafePassType.WEEKLY;
        }
        if (isPassTypeFixed(userInput)) {
            return StudyCafePassType.FIXED;
        }
        throw new AppException("잘못된 입력입니다.");
    }

    private static boolean isPassTypeHourly(String userInput) {
        return "1".equals(userInput);
    }

    private static boolean isPassTypeWeekly(String userInput) {
        return "2".equals(userInput);
    }

    private static boolean isPassTypeFixed(String userInput) {
        return "3".equals(userInput);
    }

    public StudyCafePass getSelectPass(List<StudyCafePass> passes) {
        String userInput = SCANNER.nextLine();
        int selectedIndex = Integer.parseInt(userInput) - 1;
        return passes.get(selectedIndex);
    }

    public boolean getLockerSelection() {
        String userInput = SCANNER.nextLine();
        return isPassTypeHourly(userInput);
    }

}
