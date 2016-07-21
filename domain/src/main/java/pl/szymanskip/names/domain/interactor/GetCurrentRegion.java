package pl.szymanskip.names.domain.interactor;

import pl.szymanskip.names.domain.Region;
import pl.szymanskip.names.domain.RegionProvider;
import rx.Observable;

public class GetCurrentRegion implements UseCase<Void, Observable<Region>> {
  private final RegionProvider regionProvider;

  public GetCurrentRegion(RegionProvider regionProvider) {
    this.regionProvider = regionProvider;
  }

  @Override
  public Observable<Region> execute(Void param) {
    return regionProvider.getCurrentLocation();
  }
}
