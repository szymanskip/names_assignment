package pl.szymanskip.names.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NameTest {
  @org.junit.Test
  public void isAnagramOfPalindrome() throws Exception {

    Name name = new Name("", "");
    assertFalse(name.isAnagramOfPalindrome());

    name = new Name("ab", "");
    assertFalse(name.isAnagramOfPalindrome());

    name = new Name("aab", "");
    assertTrue(name.isAnagramOfPalindrome());

    name = new Name("abcba", "");
    assertTrue(name.isAnagramOfPalindrome());

    name = new Name("aaabc", "");
    assertFalse(name.isAnagramOfPalindrome());

    name = new Name("tangent", "");
    assertFalse(name.isAnagramOfPalindrome());

    name = new Name("lemonomelon", "");
    assertTrue(name.isAnagramOfPalindrome());
  }

}