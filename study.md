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
```



// TMP : 191104 까지