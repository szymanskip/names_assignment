package pl.szymanskip.names;

public class NameViewModel {
  private final String fullName;
  private final boolean isAnagramPalindrome;

  public NameViewModel(String fullName, boolean isAnagramPalindrome) {
    this.fullName = fullName;
    this.isAnagramPalindrome = isAnagramPalindrome;
  }

  public String getFullName() {
    return fullName;
  }

  public boolean isAnagramPalindrome() {
    return isAnagramPalindrome;
  }
}
