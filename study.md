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
* 도구 : Infinitest (백그라운드에서 테스트를 항상 수행), CI과정에 테스트 포함시키기.



// TMP : P97까지 진행