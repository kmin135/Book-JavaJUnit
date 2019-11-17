/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
***/
package iloveyouboss;

import java.io.*;
import org.json.simple.parser.*;
import org.junit.*;
import org.mockito.*;
import util.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AddressRetrieverTest {
   // mock을 합성하고자 하는 객체 
   @Mock private Http http;
   // mock을 주입하고자 하는 대상
   @InjectMocks private AddressRetriever retriever;
   
   @Before
   public void createRetriever() {
      retriever = new AddressRetriever();
      /* 
       * mockito는 테스트 클래스에서 @Mock 이 붙은 필드를 가져와
       * 각각의 mock 인스턴스를 합성한다. (org.mockito.Mockito.mock(Http.class) 와 동일)
       * 그 후 @InjectMocks 가 붙은 필드를 가져와 mock 객체들을 거기에 주입한다.
       *
       * mock을 주입할 때 mockito는 
       * 1. 생성자를 통해 주입
       * 2. 없으면 setter로 주입
       * 3. 없으면 필드를 찾아서 주입
       */
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void answersAppropriateAddressForValidCoordinates() 
         throws IOException, ParseException {
      when(http.get(contains("lat=38.000000&lon=-104.000000")))
         .thenReturn("{\"address\":{"
                        + "\"house_number\":\"324\","
         // ...
                        + "\"road\":\"North Tejon Street\","
                        + "\"city\":\"Colorado Springs\","
                        + "\"state\":\"Colorado\","
                        + "\"postcode\":\"80903\","
                        + "\"country_code\":\"us\"}"
                        + "}");

      Address address = retriever.retrieve(38.0,-104.0);
      
      assertThat(address.houseNumber, equalTo("324"));
      assertThat(address.road, equalTo("North Tejon Street"));
      assertThat(address.city, equalTo("Colorado Springs"));
      assertThat(address.state, equalTo("Colorado"));
      assertThat(address.zip, equalTo("80903"));
   }
}
