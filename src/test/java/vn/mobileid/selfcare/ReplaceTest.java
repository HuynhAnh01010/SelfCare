package vn.mobileid.selfcare;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReplaceTest {

    @Test
     void testReplace(){
         String EXAMPLE_TEST = "this[_0x388bf0('0x8a3a') + _0x388bf0('0x6d5f') + 'DK'] = function(_0xcca0dc)";

//        System.out.println(EXAMPLE_TEST.matches("\\w.*"));
//        String[] splitString = (EXAMPLE_TEST.split("\\s+"));
//        System.out.println(splitString.length);// should be 14
//        for (String string : splitString) {
//            System.out.println(string);
//        }
        // replace all whitespace with tabs
//        System.out.println(EXAMPLE_TEST.replaceAll("_0x.{3,6}\\('0x8a3a'\\)", "`Face`"));
//        String line ="${env1}sojods${env2}${env3}";
//
//        System.out.println( line.replaceAll("\\$\\{env([0-9]+)\\}", "3") );
    }
}
