package pl.szymanskip.names.domain;

public class Name {
  private final String firstName;
  private final String lastName;

  public Name(String firstName, String lastName) {
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

  public boolean isAnagramOfPalindrome() {
    return false; // TODO implement
  }
}
