package pl.szymanskip.names;

import pl.szymanskip.names.domain.Name;
import pl.szymanskip.names.domain.Region;
import pl.szymanskip.names.domain.interactor.GetCurrentRegion;
import pl.szymanskip.names.domain.interactor.GetRandomName;
import rx.Subscription;
import rx.functions.Action1;

public class RandomNamePresenter {

  private final GetCurrentRegion getCurrentRegion;
  private final GetRandomName getRandomName;

  private RandomNameView view;

  private Region cachedRegion = null;
  private Subscription locationSubscription;

  public RandomNamePresenter(GetCurrentRegion getCurrentRegion, GetRandomName getRandomName) {
    this.getCurrentRegion = getCurrentRegion;
    this.getRandomName = getRandomName;
  }

  public void loadLocation() {
    locationSubscription = getCurrentRegion.execute(null)
        .subscribe(new Action1<Region>() {
          @Override
          public void call(Region region) {
            cachedRegion = region;
            if (view != null) {
              view.showLocation(new RegionViewModel(region.getName()));
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            throwable.printStackTrace();
          }
        });

  }

  public void attach(RandomNameView view) {
    this.view = view;
    this.view.showWaitingForLocation();
    if (cachedRegion != null) {
      view.showLocation(new RegionViewModel(cachedRegion.getName()));
    }
  }

  public void detach() {
    this.view = null;
    if (locationSubscription != null && !locationSubscription.isUnsubscribed()) {
      locationSubscription.unsubscribe();
    }
  }

  public void loadRandomName() {
    getRandomName.execute(cachedRegion)
        .subscribe(new Action1<Name>() {
          @Override
          public void call(Name name) {
            if (view != null) {
              view.showRandomName(
                  new NameViewModel(name.getFullName(), name.isAnagramOfPalindrome()));
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            throwable.printStackTrace();
          }
        });
  }

}
