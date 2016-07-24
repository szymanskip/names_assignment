package pl.szymanskip.names.domain;

import java.util.ArrayList;
import java.util.List;

public class Name {
  private final String firstName;
  private final String lastName;

  public Name(String firstName, String lastName) {
    if (firstName == null || lastName == null) {
      throw new IllegalArgumentException("firstName and lastName cannot be null");
    }
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  /**
   * Method checks whether firstName of this object is a word that is anagram of palindrome.
   *
   * @return true if firstName is an anagram of palindrome
   */
  public boolean isAnagramOfPalindrome() {
    if (firstName.isEmpty()) {
      return false;
    }
    List<Character> characters = new ArrayList<>();

    // loop adds character to the list and removes if it occurs again the firstName
    for (int i = 0; i < firstName.length(); i++) {
      if (characters.contains(firstName.charAt(i))) {
        characters.remove(Character.valueOf(firstName.charAt(i)));
      } else {
        characters.add(firstName.charAt(i));
      }
    }
    // if is palindrome then at most one character can be left
    return characters.size() <= 1;
  }
}
