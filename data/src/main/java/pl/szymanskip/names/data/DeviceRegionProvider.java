package pl.szymanskip.names.data;

import android.content.Context;
import android.location.Address;
import android.location.Location;

import java.util.List;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import pl.szymanskip.names.domain.Region;
import pl.szymanskip.names.domain.RegionProvider;
import rx.Observable;
import rx.functions.Func1;

public class DeviceRegionProvider implements RegionProvider {
  private static final int MAX_RESULTS = 1;

  private final ReactiveLocationProvider reactiveLocationProvider;

  public DeviceRegionProvider(Context context) {
    reactiveLocationProvider = new ReactiveLocationProvider(context);
  }

  @Override
  public Observable<Region> getCurrentLocation() {
    return reactiveLocationProvider.getLastKnownLocation()
        .flatMap(new Func1<Location, Observable<List<Address>>>() {
          @Override
          public Observable<List<Address>> call(Location location) {
            return reactiveLocationProvider.getReverseGeocodeObservable(location.getLatitude(),
                location.getLongitude(), MAX_RESULTS);
          }
        })
        .filter(new Func1<List<Address>, Boolean>() {
          @Override
          public Boolean call(List<Address> addresses) {
            return addresses.size() > 0;
          }
        })
        .map(new Func1<List<Address>, Region>() {
          @Override
          public Region call(List<Address> addresses) {
            Address firstAddress = addresses.get(1);
            return new Region(firstAddress.getCountryName());
          }
        });
  }
}
