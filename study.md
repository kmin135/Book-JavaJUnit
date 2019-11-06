# JUnit 학습

* 자바와 JUnit을 활용한 실용주의 단위 테스트 (초판 2019년 6월 30일) 읽고 알아둘 내용만 정리

## 알아둘 정적 import packages

```java
// fail(), assertTrue(boolean), assertThat(actual, matcher)
org.junit.Assert.*;

// 핵심 Matcher
org.hamcrest.CoreMatchers.*;
```

## 단위 테스트의 기본 구조

* Arrange, Act, Assert
* 준비, 실행, 단언

## JUnit 기본

* 각 단위 테스트 (`@Test` 가 붙은 메서드 하나) 는 독립된 컨텍스트에서 실행된다. 각 테스트를 실행할 때마다 인스턴스가 새롭게 생기는 것이다. 
* 각 테스트는 순서가 보장되지 않으며 다른 테스트의 결과에 영향을 받지 않는다.
* 모든 테스트마다 공통적으로 수행할 작업은 `@Before`로 한다. 이는 모든 `@Test` 메서드가 실행될 때마다 한 번씩 실행된다. 역시 2개 이상의 `@Before`가 있다면 순서는 보장되지 않는다. 후작업은 `@After` 를 사용한다.
* 클래스 레벨로 한 번씩만 실행하고 싶은 작업은 `@BeforeClass`, `@AfterClass` 사용.

## 햄크레스트 매처 예제

```java
assertThat(account.balance(), equalTo(100));
assertThat(account.balance(), not(equalTo(100)));
assertThat(accoutn.name(), notNullValue());
assertThat(account.name(), startsWith("John"));
// 리스트와 같은 컬렉션도 가능
assertThat(new String[]{"a","b"}, equalTo(new String[]{"a","b"}));
// is는 인자로 받은 매처를 그대로 리턴한다. 가독성을 높이는데 활용할 수 있다.
assertThat(account.name(), is(equalTo("Bob")));
// 단언에는 별도의 message를 추가할 수 있다. 하지만 코드의 깔끔함과 trade-off
assertThat("account balance is 100", account.balance(), equalTo(50));

/*
* JUnit에 포함된 hamcrest는 부분집합이며
* org.hamcrest.number.IsCloseTo.* 와 같은 매쳐들은 hamcrest-all이 필요하다.
*/
assertThat(2.32 * 3, closeTo(6.96, 0.0005));
```

## 예외 처리

```java
// 1. 애너테이션
@Test(expected=IllegalArugmentException.class)

// 2. try-catch
// catch 구문에서 예외 발생 후 exception에 대한 단언을 하는 경우 등에서 유용

// 3. ExpectedException 규칙
// 1번과 2번 스타일을 겸할 수 있음
@Rule
public ExpectedException thrown = ExpectedException.none();

@Test
public void exceptionRule() {
    thrown.expect(IllegalArugmentException.class);
    thrown.expectMessage("balance only 0");
    account.withdraw(100);
}
```

## 좋은 테스트 작성 가이드

* 개별 메서드를 테스트하기보다 의미있는 동작 단위로 테스트 작성
* 테스트 파일의 물리적인 위치는 소스 디렉토리는 분리하되 프로덕션 코드와 같은 패키지에 위치한다.
* 내부 동작 (private, default, protected 메서드) 에 대한 테스트의 필요성을 느껴 테스트를 작성할 경우 특정 구현에 종속적인 테스트가 되며 이는 작은 수정에도 쉽게 깨지는 테스트가 된다. 또 그런 메서드들은 일반적으로 단일 책임 원칙 (SRP) 를 어기는 설계 문제를 가지고 있을 가능성이 높다. 따라서 별도의 클래스의 public 메서드로 분리할 수 있을지 검토해봐야한다.
* 하나의 테스트는 하나의 목적을 가지는 것이 좋다. 작성의 편의성 때문에 하나의 테스트에 여러 테스트를 포함할수록 테스트의 의미가 추상적이게 되며 테스트가 깨졌을 때 문제를 파악하기도 어렵다.
* 테스트 메서드 이름은 그 자체로 문서화될 정도로 구체적으로 작성하는 것이 좋다. 양식을 정했으면 일관성을 유지하는 것이 중요하다. 아래는 예이다.

```java
// 약 7개 정의 단어로 구성하는 방법
doingSomeOperationGeneratesSomeResult

// when, then
whenDoingSomeBehaviorThenSomeResultOccurs
```

* 공통적인 사전작업이 많고 논리적으로 분리될 수 있다면 별도의 `@Before` 메서드로 분리하자. 순서가 보장되지 않음에 주의.
* 단위 테스트는 빨라야하며 더 느린 통합 테스트는 별도로 다루는 것이 좋다.
* 테스트 대상에서 제외하고 싶을 때는 주석처리하지말고 `@Ignore` 처리한다. 이는 별도로 수치화되기 때문에 까먹지 않게 된다.

## FIRST : 좋은 테스트 조건

* Fast : 테스트는 빨라야하고 느린 메서드 (DB, 외부I/F 등) 는 Mocking한다.
* Isolated : 각 테스트는 환경과 다른 테스트로부터 독립적이어야한다. 단일 책임 원칙 (SRP)를 기억하자.
* Repeatable : 여러번 실행해도 결과가 늘 같아야한다. 예를 들어 시간을 다루는 테스트라면 이에 주의해야한다.
* Self-Validating : 테스트는 스스로 준비 (Arrange)되고 검증 (Assert) 될 수 있어야한다. 테스트 전에 수동으로 준비 작업을 해야하는 등의 절차가 있어서는 안 된다.
* Timely : 작성되는 코드에 대해서는 미루지 않고 테스트코드도 작성해야한다. 미룰 수록 테스트를 작성하는 것은 더 어려워진다. 다만 레거시 코드에 대해 뒤늦게 테스트를 작성하는 것은 투자 대비 효율이 좋지 못할 가능성이 높다.

## Right-BICEP : 무엇을 테스트할 것인가?

* Right : 결과가 올바른가?
  * 코드가 정상적으로 동작한다면 그것을 어떻게 알 수 있는가?
* B : 경계 조건 (boundary conditions)은 맞는가? 외부에 노출되는 API라면 엄격한 보호가 필요하지만 내부적인 API라면 과도한 검사는 줄일 수도 있다.
  * 모호하고 일관성 없는 입력. 예를 들면 특수문자 `"!*\:&@` 가 포함된 파일 이름
  * 잘못된 양식의 데이터. 예를 들어 최상위 도메인이 빠진 이메일 주소 `kmin@google.`
  * 수치적 오류를 발생시키는 계산. 예를 들어 `overflow`, `나누기 0`
  * 비거나 빠진 값. `0, 0.0, "", null` 등
  * 기대값을 훨씬 벗어나는 값. `150세`의 나이 등.
  * 중복이 불가능한 목록에 중복이 있는 경우
  * 정렬이 안 된 컬렉션 혹은 그 반대의 경우
  * 기대한 시간 순서가 맞지 않는 경우. 예를 들어 HTTP 서버가 OPTIONS 메서드의 결과를 POST 메서드보다 늦게 반환하는 경우.
* I : 역 관계(inverse relationship)를 검사할 수 있는가? 논리적인 역 관계를 적용하여 행동을 검사할 수 있다.
  * 제곱근을 구하는 메서드를 만들었다면 제곱근을 제곱했을 때 원래의 수와 동일하거나 한 없이 가까운 수가 나와야한다.
  * 전체 집합에서 배타적인 두 집합을 구했다면 그 합은 원래의 집합과 같아야한다.
* C : 다른 수단을 통해 교차 검사(cross-check)할 수 있는가?
  * 실제 구현에는 쓰이지 못했지만 논리적으로 동일한 또 다른 구현이 있다면 (성능 문제나 너무 복잡하거나 등의 이유로) 이를 통해 교차 검사할 수 있다.
* E : 오류 조건 (error conditions)을 강제로 일어나게 할 수 있는가? 커버리지만 높인다고 좋은 테스트가 아니며 창의력을 발휘해 다양한 오류 상황을 시뮬레이션해야한다.
  * memory 또는 disk full
  * 서버와 클라이언트간의 시간이 다름
  * 네트워크 오류
  * 매우 높거나 낮은 비디오 해상도
* P : 성능 조건 (performance characteristics)은 기준에 부합하는가?
  * 여러번 반복 테스트하여 실행 타이밍에 따른 오차를 최소화해야한다.
  * 성능 테스트는 실행 환경에 따라 결과가 달라지므로 성공 기준을 정하는데 주의를 기울여야한다. 예를 들어 1초이내에 천 번 실행되야한다고 기준을 세웠을 때 내 컴퓨터와 개발서버에서 실행했을 때의 결과가 다를 수 있다.
  * 실행시간이 길어진다면 빠른 단위 테스트와는 분리하는게 좋다.
  * 위의 내용에 나아가서 성능테스트에 중점을 두고 싶다면 JMeter와 같은 전용 도구로 별도로 테스트하는 것도 좋다.

## 경계 조건: CORRECT 를 기억하라



## 도구

* Infinitest : 백그라운드에서 테스트를 항상 수행
* Jenkins : CI과정에 테스트 포함시키기



// TMP : P139까지 진행