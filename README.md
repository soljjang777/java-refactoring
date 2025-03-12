# ✨ 스터디 카페 이용권 선택 시스템 


### ✅ 사용자는 시간권, 주단위 이용권, 1인 고정석 중 선택할 수 있음

- 시간권: 2, 4, 6, 8, 10, 12시간

- 주당 이용권: 1, 2, 3, 4, 12주

- 1인 고정석: 4주, 12주

### ✅ 사물함 선택

- 고정석 이용자만 가능

- 추가 금액이 반영됨

### ✅ 할인 정책 (오픈 이벤트)

- 2주권 이상: 10% 할인

- 12주권: 15% 할인

<br>

## ⚙️ 리팩토링 포인트

✔️ 추상화 레벨

✔️ 객체로 묶어볼만한 것은 없는지

✔️ 객체지향 패러다임에 맞게 객체들이 상호 협력하고 있는지

✔️ SRP : 책임에 따라 응집도 있게 객체가 잘 나뉘어져 있는지

✔️ DIP : 의존관계 역전을 적용할만한 곳은 없는지

✔️ 일급 컬렉션


## 🤔 내가 고민한 리펙토링
1. SRP관점에서 `readStudyCafePasses()`와`readLockerPasses()` 메소드안에 파일을 읽는 기능을 분리해서 `readStudyCafePasses`와 `readLockerPasses`가 파일 읽기에 대한 책임을 가지지 않도록 해야겠다고 생각함. <br>
[**리펙토링 전**]
```java
// StudyCafeFileHandler.java
    public List<StudyCafePass> readStudyCafePasses() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/cleancode/studycafe/pass-list.csv"));
            List<StudyCafePass> studyCafePasses = new ArrayList<>();
            for (String line : lines) {
              ...(생략)
            }
            return studyCafePasses;
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }
```
[**리펙토링 후**]
```java
// StudyCafeFileHandler.java
    public List<StudyCafePass> readStudyCafePasses() {
            List<String> lines = getReadAllLines(PASS_LIST_FILE);
            List<StudyCafePass> studyCafePasses = new ArrayList<>();
            for (String line : lines) {
             ...(생략)
            }

            return studyCafePasses;
    }

    private static List<String> getReadAllLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }
```

<br>
<br>

2. `StudyCafeFileHandler.java`에서 파일경로를 매직스트링(상수로 추출)으로 사용해서 가독성도 올리고 파일경로가 수정되더라도 여기 한곳만 수정하면 편하겠다고 생각함. <br>

[**리펙토링 전**]
```java
    public List<StudyCafePass> readStudyCafePasses() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/cleancode/studycafe/pass-list.csv"));
              ...(생략)
            return studyCafePasses;
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }

    public List<StudyCafePass> readLockerPasses() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/cleancode/studycafe/locker.csv"));
              ...(생략)
            return lockerPasses;
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }
```

[**리펙토링 후**]
```java
    private static final String BASE_FILE_PATH = "src/main/resources/cleancode/studycafe/";
    private static final String PASS_LIST_FILE = BASE_FILE_PATH + "pass-list.csv";
    private static final String LOCKER_PASS_FILE = BASE_FILE_PATH + "locker.csv";

    public List<StudyCafePass> readStudyCafePasses() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PASS_LIST_FILE));
              ...(생략)
            return studyCafePasses;
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }

    public List<StudyCafePass> readLockerPasses() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(LOCKER_PASS_FILE));
              ...(생략)
            return lockerPasses;
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }
```

<br>
<br>

3. SRP관점에서 `readStudyCafePasses()`와`readLockerPasses()` 메소드안에 데이터 변환 기능을 분리해서 `readStudyCafePasses`와 `readLockerPasses`가 데이터 변환에 대한 책임을 가지지 않도록 해야겠다고 생각함. <br>
[**리펙토링 전**]
```java
// StudyCafeFileHandler.java
    public List<StudyCafePass> readStudyCafePasses() {
        List<String> lines = getReadAllLines(PASS_LIST_FILE);
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

    public List<StudyCafeLockerPass> readLockerPasses() {
        List<String> lines = getReadAllLines(LOCKER_PASS_FILE);
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
```
[**리펙토링 후**]
```java
// StudyCafeFileHandler.java
    public List<StudyCafePass> readStudyCafePasses() {
            List<String> lines = getReadAllLines(PASS_LIST_FILE);
        return getStudyCafePasses(lines);
    }

    public List<StudyCafeLockerPass> readLockerPasses() {
        List<String> lines = getReadAllLines(LOCKER_PASS_FILE);
        return getStudyCafeLockerPasses(lines);
    }

    private static List<StudyCafePass> getStudyCafePasses(List<String> lines) {
        List<StudyCafePass> studyCafePasses = new ArrayList<>();
        for (String line : lines) {
            ...(생략)
        }

        return studyCafePasses;
    }

    private static List<StudyCafeLockerPass> getStudyCafeLockerPasses(List<String> lines) {
        List<StudyCafeLockerPass> lockerPasses = new ArrayList<>();
        for (String line : lines) {
            ...(생략)
        }

        return lockerPasses;
    }
```
