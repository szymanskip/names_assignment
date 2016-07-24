package pl.szymanskip.names;

import android.util.Log;

import pl.szymanskip.names.domain.ErrorHandler;
import pl.szymanskip.names.domain.Name;
import pl.szymanskip.names.domain.Region;
import pl.szymanskip.names.domain.interactor.GetCurrentRegion;
import pl.szymanskip.names.domain.interactor.GetRandomName;
import rx.Subscription;
import rx.functions.Action1;

class RandomNamePresenter {
  private static final String TAG = "RandomNamePresenter";

  private final GetCurrentRegion getCurrentRegion;
  private final GetRandomName getRandomName;
  private ErrorHandler errorHandler;

  private RandomNameView view;

  private Region cachedRegion = null;
  private Subscription locationSubscription;

  RandomNamePresenter(GetCurrentRegion getCurrentRegion, GetRandomName getRandomName,
      ErrorHandler errorHandler) {
    this.getCurrentRegion = getCurrentRegion;
    this.getRandomName = getRandomName;
    this.errorHandler = errorHandler;
  }

  void loadLocation() {
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
            Log.e(TAG, "Error obtaining location", throwable);
            if (view != null) {
              view.showError(errorHandler.handle(throwable));
            }
          }
        });

  }

  void attach(RandomNameView view) {
    this.view = view;
    this.view.showWaitingForLocation();
    if (cachedRegion != null) {
      view.showLocation(new RegionViewModel(cachedRegion.getName()));
    }
  }

  void detach() {
    this.view = null;
    if (locationSubscription != null && !locationSubscription.isUnsubscribed()) {
      locationSubscription.unsubscribe();
    }
  }

  void loadRandomName() {
    if (view != null) {
      view.showProgress();
    }
    getRandomName.execute(cachedRegion)
        .subscribe(new Action1<Name>() {
          @Override
          public void call(Name name) {
            if (view != null) {
              view.hideProgress();
              view.showRandomName(new NameViewModel(name.getFullName()));
              if (name.isAnagramOfPalindrome()) {
                view.showSpecialName(name.getFirstName());
              }
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Log.e(TAG, "Error getting random name", throwable);
            if (view != null) {
              view.hideProgress();
              view.showError(errorHandler.handle(throwable));
            }
          }
        });
  }

}
