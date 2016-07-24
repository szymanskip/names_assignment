package pl.szymanskip.names;

public interface RandomNameView {
  void showWaitingForLocation();

  void showLocation(RegionViewModel region);

  void showProgress();

  void hideProgress();

  void showRandomName(NameViewModel nameViewModel);

  void showError(String message);

  void showSpecialName(String firstName);
}
