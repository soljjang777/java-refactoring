package cleancode.studycafe.tobe.model;

import cleancode.studycafe.tobe.model.StudyCafePassType;

public interface Pass {

    StudyCafePassType getPassType();

    int getDuration();

    int getPrice();
    double getDiscountRate();

    String display();

}
