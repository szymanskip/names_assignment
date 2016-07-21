package pl.szymanskip.names.domain;

import rx.Observable;

public interface RegionProvider {

  Observable<Region> getCurrentLocation();
}
